package com.astro.sott.utils.billing;

import com.android.billingclient.api.SkuDetails;

public interface SkuDetailsListener {
    void response(SkuDetails skuDetails);
}
