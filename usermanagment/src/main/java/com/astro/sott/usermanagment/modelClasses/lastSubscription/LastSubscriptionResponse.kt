package com.astro.sott.usermanagment.modelClasses.lastSubscription

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class LastSubscriptionResponse(

    @field:SerializedName("GetLastSubscriptionResponseMessage")
    val getLastSubscriptionResponseMessage: GetLastSubscriptionResponseMessage? = null
)

data class GetLastSubscriptionResponseMessage(

    @field:SerializedName("AccountServiceMessage")
    val accountServiceMessage: AccountServiceMessage? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("failureMessage")
    val failureMessage: List<FailureMessageItem?>? = null,
    @field:SerializedName("responseCode")
    val responseCode: String? = null
)

data class AccountServiceMessage(

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

    @field:SerializedName("uiDisplayText")
    val uiDisplayText: String? = null,

    @field:SerializedName("cancellable")
    val cancellable: Boolean? = null,

    @field:SerializedName("productCategory")
    val productCategory: String? = null,

    @field:SerializedName("hasAction")
    val hasAction: Boolean? = null,

    @field:SerializedName("duration")
    val duration: Int? = null,

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

    @field:SerializedName("productAttributes")
    val productAttributes: List<ProductAttributesItem?>? = null,

    @field:SerializedName("opId")
    val opId: String? = null,

    @field:SerializedName("isPackage")
    val isPackage: Boolean? = null,

    @field:SerializedName("serviceName")
    val serviceName: String? = null,

    @field:SerializedName("basicService")
    val basicService: Boolean? = null,

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

data class ProductAttributesItem(

    @field:SerializedName("attributeValue")
    val attributeValue: String? = null,

    @field:SerializedName("attributeName")
    val attributeName: String? = null
)
