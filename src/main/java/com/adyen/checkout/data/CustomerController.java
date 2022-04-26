package com.adyen.checkout.data;

import com.adyen.checkout.entity.Customer;
import com.adyen.checkout.web.CheckoutController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.adyen.checkout.util.CONSTANTS.PENDING_REG_STATUS;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    CheckoutController checkoutController;

    public boolean process (Customer customer) {
        customer = customerRepository.findByRegIdAndEmail(customer.getRegId(), customer.getEmail());
        if (customer.getRegStatus().equals("Pending")) {
            System.out.println(customer.getRegId() + " " + customer.getEmail());
            return false;
        }

        return true;
    }

}
