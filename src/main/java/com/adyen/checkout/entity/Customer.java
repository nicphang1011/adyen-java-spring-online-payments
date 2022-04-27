package com.adyen.checkout.entity;

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
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    @Column(name = "regstatus")
    private String regStatus;

    public String getRegStatus() {
        return this.regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    @Column(name = "paymentstatus")
    private String paymentStatus;

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }


    @Column(name = "fee")
    private String fee;

    public Integer getFee() {
        double fee = Double.parseDouble(this.fee) * 100;
        return (int) fee ;
    }


    public Customer() {
    }

    public Customer(String regId, String email) {
        this.regId = regId;
        this.email = email;
    }

}
