package com.adyen.checkout.data;

import com.adyen.checkout.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    Customer findByRegId(String regId);

    Customer findByRegIdAndEmail(String regId, String email);
}
