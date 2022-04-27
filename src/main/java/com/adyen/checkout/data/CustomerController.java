package com.adyen.checkout.data;

import com.adyen.checkout.entity.Customer;
import com.adyen.checkout.web.CheckoutController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    CheckoutController checkoutController;

    @GetMapping
    public Customer process (Customer customer) {
        customer = customerRepository.findByRegIdAndEmail(customer.getRegId(), customer.getEmail());
        if (customer != null) {
            return customer;
        }
        return null;
    }

}
