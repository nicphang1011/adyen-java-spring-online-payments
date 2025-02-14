package com.adyen.checkout.api;


import com.adyen.checkout.data.CustomerController;
import com.adyen.checkout.entity.Customer;
import com.adyen.checkout.web.CheckoutController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/payment_process/", loadOnStartup = 1)
public class PaymentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String regId = request.getParameter("regid");
        String email = request.getParameter("email");

        HttpSession session = request.getSession(true);
        session.setAttribute("type", "dropin");
        session.setAttribute("regid", regId);
        session.setAttribute("email", email);
        response.sendRedirect("/checkout?type=dropin&regid=" + regId + "&email=" + email);

    }
}
