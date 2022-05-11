package com.adyen.checkout.api;

import com.adyen.Client;
import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerController;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import com.adyen.enums.Environment;
import com.adyen.model.Amount;
import com.adyen.model.checkout.CreateCheckoutSessionRequest;
import com.adyen.model.checkout.CreateCheckoutSessionResponse;
import com.adyen.service.Checkout;
import com.adyen.service.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * REST controller for using Adyen checkout API
 */
@RestController
@RequestMapping("/api")
public class CheckoutResource {
    private final Logger log = LoggerFactory.getLogger(CheckoutResource.class);

    private ApplicationProperty applicationProperty;

    private final Checkout checkout;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    public CheckoutResource(ApplicationProperty applicationProperty) {

        this.applicationProperty = applicationProperty;

        if(applicationProperty.getApiKey() == null) {
            log.warn("ADYEN_KEY is UNDEFINED");
            throw new RuntimeException("ADYEN_KEY is UNDEFINED");
        }

        var client = new Client(applicationProperty.getApiKey(), Environment.TEST);
        this.checkout = new Checkout(client);
    }

    @PostMapping("/sessions")
    public ResponseEntity<CreateCheckoutSessionResponse> sessions(@RequestParam String type, String regid, String memberid, String email) throws IOException, ApiException {

        Customer customer = customerController.process(new Customer(regid, email));

        var orderRef = memberid+'-'+regid;
        var amount = new Amount()
            .currency("SGD")
            .value(Long.valueOf(customer.getFee()));

        customer.setTransactionref(orderRef);
        customerRepository.save(customer);

        var checkoutSession = new CreateCheckoutSessionRequest();
        checkoutSession.merchantAccount(this.applicationProperty.getMerchantAccount());
        // (optional) set WEB to filter out payment methods available only for this platform
        checkoutSession.setChannel(CreateCheckoutSessionRequest.ChannelEnum.WEB);
        checkoutSession.setReference(orderRef); // required
        checkoutSession.setReturnUrl(this.applicationProperty.getReturnUrl() + "/redirect?orderRef=" + orderRef);

        checkoutSession.setAmount(amount);

        log.info("REST request to create Adyen Payment Session {}", checkoutSession);
        var response = checkout.sessions(checkoutSession);
        return ResponseEntity.ok().body(response);
    }
}
