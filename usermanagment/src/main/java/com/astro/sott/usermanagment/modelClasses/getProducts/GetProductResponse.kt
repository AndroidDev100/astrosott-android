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
        val frequency: String? = null,

        @field:SerializedName("attributes")
        val attributes: List<Attribute>? = null,
        @field:SerializedName("promotions")
        val promotions: List<Promotion>? = null
)

data class Promotion(
        @field:SerializedName("isFreeTrail")
        val isFreeTrail: Boolean? = null,
        @field:SerializedName("promotionId")
        val promotionId: String? = null,
        @field:SerializedName("promotionName")
        val promotionName: String? = null,
        @field:SerializedName("promotionType")
        val promotionType: String? = null,
        @field:SerializedName("amount")
        val amount: Double? = null,
        @field:SerializedName("promotionalPrice")
        val promotionalPrice: Double? = null,
        @field:SerializedName("isVODPromotion")
        val isVODPromotion: Boolean? = null,
        @field:SerializedName("promoDescrip")
        val promoDescrip: String? = null,
        @field:SerializedName("promotionExpiry")
        val promotionExpiry: Long? = null,
        @field:SerializedName("promotionDuration")
        val promotionDuration: Int? = null,
        @field:SerializedName("promotionPeriod")
        val promotionPeriod: String? = null,
        @field:SerializedName("promoCpDescription")
        val promoCpDescription: String? = null
)

data class Attribute(
        @field:SerializedName("attributeLabel")
        val attributeLabel: String? = null,
        @field:SerializedName("attributeType")
        val attributeType: String? = null,
        @field:SerializedName("attributeValue")
        val attributeValue: String? = null,
        @field:SerializedName("attributeName")
        val attributeName: String? = null
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
