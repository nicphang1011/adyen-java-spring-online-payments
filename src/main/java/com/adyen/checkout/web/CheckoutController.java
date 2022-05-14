package com.adyen.checkout.web;

import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController {

    private final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    public CheckoutController(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;

        if(this.applicationProperty.getClientKey() == null) {
            log.warn("ADYEN_CLIENT_KEY is undefined ");
        }
    }

    @Autowired
    private ApplicationProperty applicationProperty;


    @GetMapping("/checkout")
    public String checkout(@RequestParam String type, String regid, String memberid, String email, String languagesite, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("regid", regid);
        model.addAttribute("memberid", memberid);
        model.addAttribute("email", email);
        model.addAttribute("languagesite", languagesite);
        model.addAttribute("clientKey", this.applicationProperty.getClientKey());
        return "checkout";
    }

    @GetMapping("/refund")
    public String refund(@RequestParam String regid, String email, String languagesite, Model model) {
        model.addAttribute("regid", regid);
        model.addAttribute("email", email);
        model.addAttribute("languagesite", languagesite);
        model.addAttribute("clientKey", this.applicationProperty.getClientKey());
        return "refund";
    }

    @GetMapping("/result/{type}")
    public String result(@PathVariable String type, Model model) {
        model.addAttribute("type", type);
        return "result";
    }

    @GetMapping("/redirect")
    public String redirect(@RequestParam String orderRef, String redirectResult, Model model) {
        model.addAttribute("clientKey", this.applicationProperty.getClientKey());
        model.addAttribute("orderRef", orderRef);
        model.addAttribute("redirectResult", redirectResult);
        return "redirect";
    }
}
