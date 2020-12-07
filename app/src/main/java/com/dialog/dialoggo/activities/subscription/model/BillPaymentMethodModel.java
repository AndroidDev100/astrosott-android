package com.dialog.dialoggo.activities.subscription.model;

public class BillPaymentMethodModel {


    public static final int PAYMENT_TITLE_TYPE=0;
    public static final int PAYMENT_DESC_TYPE=1;

    public int type;
    private String title;
    private String paymentMethod,number;

    public BillPaymentMethodModel(int type, String title, String paymentMethod, String number) {
        this.type = type;
        this.title = title;
        this.paymentMethod = paymentMethod;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
