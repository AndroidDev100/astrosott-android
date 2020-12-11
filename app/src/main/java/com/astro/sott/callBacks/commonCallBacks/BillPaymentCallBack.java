package com.astro.sott.callBacks.commonCallBacks;

import com.astro.sott.activities.subscription.model.BillPaymentModel;

import java.util.List;

public interface BillPaymentCallBack {
    void response(List<BillPaymentModel> billPaymentModels);
    void failure();
}
