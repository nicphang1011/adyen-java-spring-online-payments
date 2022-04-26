package com.adyen.checkout.api;


import com.adyen.checkout.data.CustomerController;
import com.adyen.checkout.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/payment_process/", loadOnStartup = 1)
public class PaymentsServlet extends HttpServlet {

    @Autowired
    private CustomerController customerController;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        String regId = request.getParameter("regid");
        String email = request.getParameter("email");

        Customer customer = new Customer(regId, email);
        customerController.process(customer);

    }
}
