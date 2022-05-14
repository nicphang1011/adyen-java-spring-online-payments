package com.adyen.checkout.api;

import com.adyen.checkout.ApplicationProperty;
import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import com.adyen.model.notification.NotificationRequest;
import com.adyen.util.HMACValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.adyen.checkout.util.Constants.CANCELLED_REG_STATUS;
import static com.adyen.checkout.util.Constants.REFUNDED_PAYMENT_STATUS;

/**
 * REST controller for receiving Adyen webhook notifications
 */
@RestController
@RequestMapping("/api")
public class WebhookResource {
    private final Logger log = LoggerFactory.getLogger(WebhookResource.class);

    @Autowired
    CustomerRepository customerRepository;

    private ApplicationProperty applicationProperty;

    @Autowired
    public WebhookResource(ApplicationProperty applicationProperty) {
        this.applicationProperty = applicationProperty;

        if(this.applicationProperty.getHmacKey() == null) {
            log.warn("ADYEN_HMAC_KEY is UNDEFINED (Webhook cannot be authenticated)");
            throw new RuntimeException("ADYEN_HMAC_KEY is UNDEFINED");
        }
    }

    /**
     * Process incoming Webhook notifications
     * @param notificationRequest
     * @return
     */
    @PostMapping("/webhooks/notifications")
    public ResponseEntity<String> webhooks(@RequestBody NotificationRequest notificationRequest){

        notificationRequest.getNotificationItems().forEach(
          item -> {
              // We recommend validate HMAC signature in the webhooks for security reasons
              try {
                  if (new HMACValidator().validateHMAC(item, this.applicationProperty.getHmacKey())) {
                      log.info("Received webhook with event {} : \n" +
                              "Merchant Reference: {}\n" +
                              "Alias : {}\n" +
                              "Refusal reason : {}\n" +
                              "PSP reference : {}"
                          , item.getEventCode(), item.getMerchantReference(), item.getAdditionalData().get("alias"), item.getAdditionalData().get("refusalReasonRaw"), item.getPspReference());
                      log.info(item.toString());
                      if (item.getEventCode().equals("REFUND")) {
                          Customer customer = customerRepository.findByTransactionref(item.getMerchantReference());
                          if(customer.getTransactionid().isBlank()){
                              customer.setTransactionid(item.getPspReference());
                          }else {
                              customer.setTransactionid(customer.getTransactionid() + " " + item.getPspReference());
                          }
                          customer.setPaymentmessage(item.getAdditionalData().get("refusalReasonRaw"));
                          if ( false /*insert logic to check success*/) {
                              customer.setRegStatus(CANCELLED_REG_STATUS);
                              customer.setPaymentStatus(REFUNDED_PAYMENT_STATUS);
                              customer.setRefundAmount("0");

                              DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
                              ZonedDateTime now = ZonedDateTime.now();
                              ZonedDateTime singaporeDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Singapore"));
                              customer.setRefundDate(dtf.format(singaporeDateTime));
                          }

                          customerRepository.save(customer);
                      } else {
                          Customer customer = customerRepository.findByTransactionref(item.getMerchantReference());
                          if(customer.getTransactionid().isBlank()){
                              customer.setTransactionid(item.getPspReference());
                          }else {
                              customer.setTransactionid(customer.getTransactionid() + " " + item.getPspReference());
                          }
                          customer.setPaymentmessage(item.getAdditionalData().get("refusalReasonRaw"));
                          customerRepository.save(customer);
                      }

                  } else {
                      // invalid HMAC signature: do not send [accepted] response
                      log.warn("Could not validate HMAC signature for incoming webhook message: {}", item);
                      throw new RuntimeException("Invalid HMAC signature");
                  }
              } catch (SignatureException e) {
                  log.error("Error while validating HMAC Key", e);
              }
          }
        );

        // Notifying the server we're accepting the payload
        return ResponseEntity.ok().body("[accepted]");
    }
}
