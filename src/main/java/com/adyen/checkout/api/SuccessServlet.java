package com.adyen.checkout.api;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/payment_success/", loadOnStartup = 1)
public class SuccessServlet extends HttpServlet {

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
