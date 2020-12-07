package com.dialog.dialoggo.activities.subscription.model;

import java.util.List;

public class BillPaymentModel {
    private String headerTitle;
    private List<BillPaymentDetails> billPaymentDetails;

    public List<BillPaymentDetails> getBillPaymentDetails() {
        return billPaymentDetails;
    }

    public void setBillPaymentDetails(List<BillPaymentDetails> billPaymentDetails) {
        this.billPaymentDetails = billPaymentDetails;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }




}
