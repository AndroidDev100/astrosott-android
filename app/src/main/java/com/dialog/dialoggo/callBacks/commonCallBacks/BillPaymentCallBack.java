package com.dialog.dialoggo.callBacks.commonCallBacks;

import com.dialog.dialoggo.activities.subscription.model.BillPaymentModel;

import java.util.List;

public interface BillPaymentCallBack {
    void response(List<BillPaymentModel> billPaymentModels);
    void failure();
}
