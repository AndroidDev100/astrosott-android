package com.astro.sott.utils.billing;

import androidx.annotation.Nullable;

import com.android.billingclient.api.SkuDetails;

import java.util.List;

public interface SKUsListListener {
    void onListOfSKU(@Nullable List<SkuDetails> purchases);
}
