package com.adyen.checkout.entity;

import com.adyen.checkout.data.CustomerRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reg")
public class Customer {

    @Id
    @Column(name = "regid")
    private String regId;

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegId() {
        return this.regId;
    }

    @Column(name = "email")
    private String email;

    public void setEmail(String email) {
        this.email= email;
    }

    public String getEmail() {
        return this.email;
    }

    @Column(name = "regstatus")
    private String regStatus;

    public String getRegStatus() { return this.regStatus; }

    @Column(name = "paymentstatus")
    private String paymentStatus;

    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus;}

    public Customer(){

    }

    public Customer(String regId, String email, String regStatus, String paymentStatus){
        this.regId = regId;
        this.email = email;
        this.regStatus = regStatus;
        this.paymentStatus = paymentStatus;
    }

    public Customer(String regId, String email){
        this.regId = regId;
        this.email = email;
    }

}
