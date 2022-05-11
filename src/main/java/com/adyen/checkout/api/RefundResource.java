package com.adyen.checkout.api;

import com.adyen.Client;
import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.CreatePaymentRefundRequest;
import com.adyen.service.Checkout;
import com.adyen.service.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

        var client = new Client(applicationProperty.getApiKey(), Environment.LIVE);
        this.checkout = new Checkout(client);
    }

    @PostMapping("/payments/{paymentPspReference}/refunds")
    public void refunds(@PathVariable String paymentPspReference) throws IOException, ApiException {

        Customer customer = customerRepository.findByTransactionid(paymentPspReference);

        var paymentRefundRequest = new CreatePaymentRefundRequest();
        var amount = new Amount();
        amount.setCurrency("SGD");
        amount.setValue(Long.valueOf(customer.getFee()));
        paymentRefundRequest.setAmount(amount);
//        paymentRefundRequest.setReference("YOUR_UNIQUE_REFERENCE");
        var response = checkout.paymentsRefunds(paymentPspReference, paymentRefundRequest);
    }
}
