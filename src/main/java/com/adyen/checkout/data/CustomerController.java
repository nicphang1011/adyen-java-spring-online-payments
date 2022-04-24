package com.adyen.checkout.data;

import com.adyen.checkout.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.adyen.checkout.util.CONSTANTS.PENDING_REG_STATUS;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    public void process (Customer customer) {
        customer = customerRepository.findByRegIdAndEmail(customer.getRegId(), customer.getEmail());
        customer.getRegStatus().equals("Pending");// ? redirectToPayment() : redirectToSuccess();
    }

}
