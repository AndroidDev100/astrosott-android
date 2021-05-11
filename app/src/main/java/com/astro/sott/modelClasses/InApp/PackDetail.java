package com.astro.sott.modelClasses.InApp;

import com.android.billingclient.api.SkuDetails;
import com.astro.sott.usermanagment.modelClasses.getProducts.ProductsResponseMessageItem;

public class PackDetail {
   private SkuDetails skuDetails;

    public SkuDetails getSkuDetails() {
        return skuDetails;
    }

    public void setSkuDetails(SkuDetails skuDetails) {
        this.skuDetails = skuDetails;
    }

    public ProductsResponseMessageItem getProductsResponseMessageItem() {
        return productsResponseMessageItem;
    }

    public void setProductsResponseMessageItem(ProductsResponseMessageItem productsResponseMessageItem) {
        this.productsResponseMessageItem = productsResponseMessageItem;
    }

    private ProductsResponseMessageItem productsResponseMessageItem;
}
