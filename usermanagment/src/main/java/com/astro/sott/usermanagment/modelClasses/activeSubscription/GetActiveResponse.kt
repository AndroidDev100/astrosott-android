package com.astro.sott.usermanagment.modelClasses.activeSubscription

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class GetActiveResponse(

        @field:SerializedName("GetActiveSubscriptionsResponseMessage")
        val getActiveSubscriptionsResponseMessage: GetActiveSubscriptionsResponseMessage? = null
)

data class PromotionsItem(

        @field:SerializedName("isDefaultPromo")
        val isDefaultPromo: Boolean? = null,

        @field:SerializedName("promotionName")
        val promotionName: String? = null,

        @field:SerializedName("promotionType")
        val promotionType: String? = null,

        @field:SerializedName("amount")
        val amount: Double? = null,

        @field:SerializedName("endDate")
        val endDate: String? = null,

        @field:SerializedName("percentage")
        val percentage: Double? = null,

        @field:SerializedName("isVODPromotion")
        val isVODPromotion: Boolean? = null,

        @field:SerializedName("promotionalPrice")
        val promotionalPrice: Double? = null,

        @field:SerializedName("isFreeTrail")
        val isFreeTrail: Boolean? = null,

        @field:SerializedName("promotionId")
        val promotionId: String? = null,

        @field:SerializedName("startDate")
        val startDate: String? = null
)

data class GetActiveSubscriptionsResponseMessage(

        @field:SerializedName("nextBillingAmount")
        val nextBillingAmount: Int? = null,

        @field:SerializedName("AccountServiceMessage")
        val accountServiceMessage: List<AccountServiceMessageItem?>? = null,

        @field:SerializedName("nextBillingDateTime")
        val nextBillingDateTime: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("responseCode")
        val responseCode: String? = null
)

data class AccountServiceMessageItem(

        @field:SerializedName("serviceType")
        val serviceType: String? = null,

        @field:SerializedName("displayName")
        val displayName: String? = null,

        @field:SerializedName("isContent")
        val isContent: Boolean? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("cancellable")
        val cancellable: Boolean? = null,

        @field:SerializedName("productCategory")
        val productCategory: String? = null,

        @field:SerializedName("hasAction")
        val hasAction: Boolean? = null,

        @field:SerializedName("duration")
        val duration: Int? = null,

        @field:SerializedName("priceCharged")
        val priceCharged: Double? = null,

        @field:SerializedName("validityPeriod")
        val validityPeriod: String? = null,

        @field:SerializedName("startDateInMillis")
        val startDateInMillis: Long? = null,

        @field:SerializedName("isInFreeTrail")
        val isInFreeTrail: Boolean? = null,

        @field:SerializedName("planPrice")
        val planPrice: Double? = null,

        @field:SerializedName("isRenewal")
        val isRenewal: Boolean? = null,

        @field:SerializedName("period")
        val period: String? = null,

        @field:SerializedName("orderID")
        val orderID: String? = null,

        @field:SerializedName("opId")
        val opId: String? = null,

        @field:SerializedName("isPackage")
        val isPackage: Boolean? = null,

        @field:SerializedName("serviceName")
        val serviceName: String? = null,

        @field:SerializedName("basicService")
        val basicService: Boolean? = null,

        @field:SerializedName("promotions")
        val promotions: List<PromotionsItem?>? = null,

        @field:SerializedName("subscriptionType")
        val subscriptionType: String? = null,

        @field:SerializedName("validityDuration")
        val validityDuration: Int? = null,

        @field:SerializedName("validityTill")
        val validityTill: Long? = null,

        @field:SerializedName("paymentMethod")
        val paymentMethod: String? = null,

        @field:SerializedName("isFreemium")
        val isFreemium: Boolean? = null,

        @field:SerializedName("planPriceWithTax")
        val planPriceWithTax: Double? = null,

        @field:SerializedName("serviceID")
        val serviceID: String? = null,

        @field:SerializedName("retailPrice")
        val retailPrice: Double? = null,

        @field:SerializedName("currencyCode")
        val currencyCode: String? = null,

        @field:SerializedName("startDate")
        val startDate: Long? = null,

        @field:SerializedName("status")
        val status: String? = null
)
