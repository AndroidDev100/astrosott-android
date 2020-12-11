package com.astro.sott.activities.subscription.model;

public class PurchaseResponse {
    private boolean status;
    private String errorCode;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentGatewayReferenceId() {
        return PaymentGatewayReferenceId;
    }

    public void setPaymentGatewayReferenceId(String paymentGatewayReferenceId) {
        PaymentGatewayReferenceId = paymentGatewayReferenceId;
    }

    private String PaymentGatewayReferenceId;
}
