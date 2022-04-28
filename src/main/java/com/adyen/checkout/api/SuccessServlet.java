package com.adyen.checkout.api;


import com.adyen.checkout.data.CustomerController;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.adyen.checkout.util.Constants.APPROVED_PAYMENT_STATUS;
import static com.adyen.checkout.util.Constants.APPROVED_REG_STATUS;

@WebServlet(urlPatterns = "/payment_success/", loadOnStartup = 1)
public class SuccessServlet extends HttpServlet {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String regId = request.getParameter("regid");
        String email = request.getParameter("email");

        Customer customer = customerController.process(new Customer(regId, email));
        customer.setPaymentStatus(APPROVED_PAYMENT_STATUS);
        customer.setRegStatus(APPROVED_REG_STATUS);
        customerRepository.save(customer);

    }
}
