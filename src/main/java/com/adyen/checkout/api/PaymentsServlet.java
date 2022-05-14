package com.adyen.checkout.api;

import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.adyen.checkout.util.Constants.APPROVED_PAYMENT_STATUS;
import static com.adyen.checkout.util.Constants.APPROVED_REG_STATUS;

@WebServlet(urlPatterns = "/payment_process/", loadOnStartup = 1)
public class PaymentsServlet extends HttpServlet {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String regId = request.getParameter("regid");
        String memberid = request.getParameter("memberid");
        String email = request.getParameter("email");
        String languageSite = request.getParameter("languagesite");

        Customer customer = customerRepository.findByRegIdAndEmail(regId, email);
//
//        /**Added by Simon*/
//        String languageSite = customer.getLanguageSite();

        if(customer.getPaymentStatus().equals(APPROVED_PAYMENT_STATUS) && customer.getRegStatus().equals(APPROVED_REG_STATUS)) {
            response.sendRedirect("https://www.emicrosite.com/" + languageSite); /**Added by Simon*/ /**To update*/
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("type", "dropin");
        session.setAttribute("regid", regId);
        session.setAttribute("memberid", memberid);
        session.setAttribute("email", email);

        /**Added by Simon*/
        session.setAttribute("languagesite", languageSite);

        response.sendRedirect("http://localhost:8080/checkout?type=dropin&regid=" + regId + "&memberid=" + memberid + "&email=" + email + "&languagesite=" + languageSite); /**Added by Simon*/

    }

}
