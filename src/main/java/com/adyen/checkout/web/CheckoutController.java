package com.adyen.checkout.web;

import com.adyen.checkout.ApplicationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CheckoutController {

    private final Logger log = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    public CheckoutController(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;

        if(this.applicationProperty.getClientKey() == null) {
            log.warn("ADYEN_CLIENT_KEY is undefined ");
        }
    }

    @Autowired
    private ApplicationProperty applicationProperty;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam String type, String regid, String email, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("regid", regid);
        model.addAttribute("email", email);
        model.addAttribute("clientKey", this.applicationProperty.getClientKey());
        return "checkout";
    }

    @GetMapping("/result/{type}")
    public String result(@PathVariable String type, String regid, String email, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("regid", regid);
        model.addAttribute("email", email);
        return "result";
    }

    @GetMapping("/redirect")
    public String redirect(Model model) {
        model.addAttribute("clientKey", this.applicationProperty.getClientKey());
        return "redirect";
    }
}
