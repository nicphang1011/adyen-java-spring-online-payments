package com.adyen.checkout.api;

import com.adyen.Client;
import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.CreatePaymentRefundRequest;
import com.adyen.service.Checkout;
import com.adyen.service.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.adyen.checkout.util.Constants.APPROVED_PAYMENT_STATUS;
import static com.adyen.checkout.util.Constants.APPROVED_REG_STATUS;

@WebServlet(urlPatterns = "/process_refund/", loadOnStartup = 1)
public class RefundServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(RefundServlet.class);

    @Autowired
    CustomerRepository customerRepository;

    private ApplicationProperty applicationProperty;

    @Autowired
    public RefundServlet(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;

        if(this.applicationProperty.getHmacKey() == null) {
            log.warn("ADYEN_HMAC_KEY is UNDEFINED (Webhook cannot be authenticated)");
            throw new RuntimeException("ADYEN_HMAC_KEY is UNDEFINED");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String regId = request.getParameter("regid");
        String email = request.getParameter("email");
        String languageSite = request.getParameter("languagesite");

        Customer customer = customerRepository.findByRegIdAndEmail(regId, email);

        if (customer.getRegStatus().equals(APPROVED_PAYMENT_STATUS) && customer.getPaymentStatus().equals(APPROVED_PAYMENT_STATUS)) {
            String xApiKey = applicationProperty.getApiKey();
            Client client = new Client(xApiKey, Environment.TEST);
            Checkout checkout = new Checkout(client);
            var paymentRefundRequest = new CreatePaymentRefundRequest();
            var amount = new Amount();
            amount.setCurrency("USD");
            amount.setValue(Long.valueOf(customer.getRefundAmount()));
            paymentRefundRequest.setAmount(amount);
            paymentRefundRequest.setMerchantAccount(applicationProperty.getMerchantAccount());
//        paymentRefundRequest.setReference("YOUR_UNIQUE_REFERENCE");
            String paymentPspReference = customer.getTransactionid();
            try {
                var paymentRefundResponse = checkout.paymentsRefunds(paymentPspReference, paymentRefundRequest);
                log.info(paymentRefundResponse.toString());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("https://www.emicrosite.com/" + languageSite); /**Added by Simon*/ /**To update*/

    }
}
