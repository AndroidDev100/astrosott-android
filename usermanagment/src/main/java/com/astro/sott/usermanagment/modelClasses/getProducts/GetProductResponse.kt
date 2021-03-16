package com.astro.sott.usermanagment.modelClasses.getProducts

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class GetProductResponse(

        @field:SerializedName("GetProductsResponseMessage")
        val getProductsResponseMessage: GetProductsResponseMessage? = null
)

data class ProductsResponseMessageItem(

        @field:SerializedName("serviceType")
        val serviceType: String? = null,

        @field:SerializedName("period")
        val period: String? = null,

        @field:SerializedName("displayName")
        val displayName: String? = null,

        @field:SerializedName("dmaName")
        val dmaName: String? = null,

        @field:SerializedName("displayOrder")
        val displayOrder: Int? = null,

        @field:SerializedName("appChannels")
        val appChannels: List<AppChannelsItem?>? = null,

        @field:SerializedName("basicService")
        val basicService: Boolean? = null,

        @field:SerializedName("productName")
        val productName: String? = null,

        @field:SerializedName("isInterstitialAdsEnabled")
        val isInterstitialAdsEnabled: Boolean? = null,

        @field:SerializedName("productCategory")
        val productCategory: String? = null,

        @field:SerializedName("duration")
        val duration: Int? = null,

        @field:SerializedName("suggestedPrice")
        val suggestedPrice: Int? = null,

        @field:SerializedName("isAdsEnabled")
        val isAdsEnabled: Boolean? = null,

        @field:SerializedName("renewable")
        val renewable: Boolean? = null,

        @field:SerializedName("skuORQuickCode")
        val skuORQuickCode: String? = null,

        @field:SerializedName("isFreemium")
        val isFreemium: Boolean? = null,

        @field:SerializedName("retailPrice")
        val retailPrice: Double? = null,

        @field:SerializedName("currencyCode")
        val currencyCode: String? = null,

        @field:SerializedName("isBannerAdsEnabled")
        val isBannerAdsEnabled: Boolean? = null,

        @field:SerializedName("productDescription")
        val productDescription: String? = null,

        @field:SerializedName("frequency")
        val frequency: String? = null
)

data class GetProductsResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("productsResponseMessage")
        val productsResponseMessage: List<ProductsResponseMessageItem?>? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null
)

data class AppChannelsItem(

        @field:SerializedName("appName")
        val appName: String? = null,

        @field:SerializedName("appID")
        val appID: String? = null,

        @field:SerializedName("appChannel")
        val appChannel: String? = null
)
