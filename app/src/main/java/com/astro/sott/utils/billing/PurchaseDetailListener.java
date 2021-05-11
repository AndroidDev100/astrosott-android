package com.astro.sott.utils.billing;

import com.android.billingclient.api.Purchase;

public interface PurchaseDetailListener {
    void response(Purchase purchaseObject);
}
