package com.astro.sott.modelClasses;

public class OtpModel {
    private String msisdn;
    private int pin;
    private String message;
    private String a_t;
    private int responseCode;
    private String txnId;


    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getmPin() {
        return pin;
    }

    public void setmPin(int mPin) {
        this.pin = mPin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return a_t;
    }

    public void setToken(String token) {
        this.a_t = token;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
