package com.adyen.checkout;

import com.adyen.checkout.data.CustomerRepository;
import com.adyen.checkout.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

import static com.adyen.checkout.util.CONSTANTS.PENDING_PAYMENT_STATUS;
import static com.adyen.checkout.util.CONSTANTS.PENDING_REG_STATUS;

@SpringBootApplication
public class OnlinePaymentsApplication {

    private static final Logger log = LoggerFactory.getLogger(OnlinePaymentsApplication.class);

    @Autowired
    private ApplicationProperty applicationProperty;

    public static void main(String[] args) {
        SpringApplication.run(OnlinePaymentsApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running on http://localhost:" + applicationProperty.getServerPort() +
            "\n----------------------------------------------------------");
    }

}
