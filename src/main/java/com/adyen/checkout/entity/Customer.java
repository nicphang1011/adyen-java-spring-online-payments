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

    @Column(name = "memberid")
    private String memberid;

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getMemberid() {
        return this.memberid;
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


    @Column(name = "languagesite")
    private String languageSite;

    public void setLanguageSite(String languageSite) {
        this.languageSite = languageSite;
    }

    public String getLanguageSite() {
        return this.languageSite;
    }


    @Column(name = "fee")
    private String fee;

    public Integer getFee() {
        double fee = Double.parseDouble(this.fee) * 100;
        return (int) fee ;
    }

    @Column(name = "transactionref")
    private String transactionref;

    public void setTransactionref(String transactionref) {
        this.transactionref = transactionref;
    }

    public String getTransactionref() {
        return this.transactionref;
    }

    @Column(name = "transactionid")
    private String transactionid;

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getTransactionid() {
        return this.transactionid;
    }

    @Column(name = "paymentmessage")
    private String paymentmessage;

    public void setPaymentmessage(String paymentmessage) {
        this.paymentmessage = paymentmessage;
    }

    public String getPaymentmessage() {
        return this.paymentmessage;
    }

    @Column(name = "refundamount")
    private String refundamount;

    public Integer getRefundAmount() {
        double fee = Double.parseDouble(this.refundamount) * 100;
        return (int) fee ;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundamount = refundAmount;
    }

    @Column(name = "paymentdate")
    private String paymentDate;

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentDate() {
        return this.paymentDate;
    }

    @Column(name = "refunddate")
    private String refundDate;

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundDate() {
        return this.refundDate;
    }

    public Customer() {
    }

    public Customer(String regId, String email) {
        this.regId = regId;
        this.email = email;
    }

}
