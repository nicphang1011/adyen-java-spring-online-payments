package com.adyen.checkout.api;


import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerController;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.adyen.checkout.util.Constants.APPROVED_PAYMENT_STATUS;
import static com.adyen.checkout.util.Constants.APPROVED_REG_STATUS;

@WebServlet(urlPatterns = "/payment_success/", loadOnStartup = 1)
public class SuccessServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(SuccessServlet.class);

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    private Customer customer;

    private ApplicationProperty applicationProperty;

    @Autowired
    public SuccessServlet(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;

        if(this.applicationProperty.getHmacKey() == null) {
            log.warn("ADYEN_HMAC_KEY is UNDEFINED (Webhook cannot be authenticated)");
            throw new RuntimeException("ADYEN_HMAC_KEY is UNDEFINED");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String regId = request.getParameter("regid");
        String email = request.getParameter("email");
        String orderRef = request.getParameter("orderRef");
        String redirectResult = request.getParameter("redirectResult");

        if(regId.equals("undefined") && email.equals("undefined")){
            customer = customerRepository.findByTransactionref(orderRef);

            URL url = new URL("https://checkout-test.adyen.com/v68/payments/details");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("x-API-key", applicationProperty.getApiKey());
            http.setRequestProperty("content-type", "application/json");
            String data = "{\n       \"details\": {\n         \"redirectResult\": \""+ redirectResult + "\"\n     }\n  }";

            DataOutputStream wr = new DataOutputStream(http.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();

            int responseCode = http.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader iny = new BufferedReader(
                new InputStreamReader(http.getInputStream()));
            String output;
            StringBuffer paymentDetailsResponse = new StringBuffer();

            while ((output = iny.readLine()) != null) {
                paymentDetailsResponse.append(output);
            }
            iny.close();

            log.info(paymentDetailsResponse.toString());
            Gson g = new Gson();
            JsonElement pspReference = g.fromJson(paymentDetailsResponse.toString(), JsonObject.class).get("pspReference");
            if(customer.getTransactionid().isBlank()){
                customer.setTransactionid(pspReference.getAsString());
            }else {
                customer.setTransactionid(customer.getTransactionid() + " " + pspReference.getAsString());
            }

        } else {
            customer = customerController.process(new Customer(regId, email));
        }
        customer.setPaymentStatus(APPROVED_PAYMENT_STATUS);
        customer.setRegStatus(APPROVED_REG_STATUS);

        /**Added by Simon*/
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime singaporeDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Singapore"));
        customer.setPaymentDate(dtf.format(singaporeDateTime));
        customerRepository.save(customer);

    }
}
