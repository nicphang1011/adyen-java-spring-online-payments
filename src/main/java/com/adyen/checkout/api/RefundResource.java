package com.adyen.checkout.api;

import com.adyen.Client;
import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.CreateCheckoutSessionResponse;
import com.adyen.model.checkout.CreatePaymentRefundRequest;
import com.adyen.model.checkout.PaymentRefundResource;
import com.adyen.service.Checkout;
import com.adyen.service.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


public class RefundResource {

    private final Logger log = LoggerFactory.getLogger(RefundResource.class);

    private ApplicationProperty applicationProperty;

    private final Checkout checkout;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    public RefundResource(ApplicationProperty applicationProperty) {

        this.applicationProperty = applicationProperty;

        if(applicationProperty.getApiKey() == null) {
            log.warn("ADYEN_KEY is UNDEFINED");
            throw new RuntimeException("ADYEN_KEY is UNDEFINED");
        }

        var client = new Client(applicationProperty.getApiKey(), Environment.TEST);
        this.checkout = new Checkout(client);
    }

    @PostMapping("/sessions")
    public ResponseEntity<PaymentRefundResource> refunds(@PathVariable String paymentPspReference, @RequestParam String regid, String email) throws IOException, ApiException {

        Customer customer = customerRepository.findByRegIdAndEmail(regid, email);

        if (customer.getRegStatus().equals("Approved") && customer.getPaymentStatus().equals("Approved")) {
            var paymentRefundRequest = new CreatePaymentRefundRequest();
            var amount = new Amount();
            amount.setCurrency("SGD");
            amount.setValue(Long.valueOf(customer.getRefundAmount()));
            paymentRefundRequest.setAmount(amount);
//        paymentRefundRequest.setReference("YOUR_UNIQUE_REFERENCE");
            var response = checkout.paymentsRefunds(paymentPspReference, paymentRefundRequest);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.ok().body(null);
    }
}
