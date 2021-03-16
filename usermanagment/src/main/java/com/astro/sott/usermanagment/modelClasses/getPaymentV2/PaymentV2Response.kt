package com.astro.sott.usermanagment.modelClasses.getPaymentV2

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class PaymentV2Response(

        @field:SerializedName("GetPaymentsV2ResponseMessage")
        val getPaymentsV2ResponseMessage: GetPaymentsV2ResponseMessage? = null
)

data class OrderItem(

        @field:SerializedName("totalTaxCharged")
        val totalTaxCharged: Double? = null,

        @field:SerializedName("orderID")
        val orderID: Int? = null,

        @field:SerializedName("totalPriceCharged")
        val totalPriceCharged: Double? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("paymentsInfo")
        val paymentsInfo: List<PaymentsInfoItem?>? = null,

        @field:SerializedName("currencyCode")
        val currencyCode: String? = null,

        @field:SerializedName("orderProductInfo")
        val orderProductInfo: List<OrderProductInfoItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)

data class OrderProductInfoItem(

        @field:SerializedName("chargedPrice")
        val chargedPrice: Double? = null,

        @field:SerializedName("endDate")
        val endDate: Long? = null,

        @field:SerializedName("displayName")
        val displayName: String? = null,

        @field:SerializedName("serviceId")
        val serviceId: String? = null,

        @field:SerializedName("retailPrice")
        val retailPrice: Double? = null,

        @field:SerializedName("taxCharged")
        val taxCharged: Double? = null,

        @field:SerializedName("startDate")
        val startDate: Long? = null
)

data class NextBillInformation(

        @field:SerializedName("nextBillingAmount")
        val nextBillingAmount: Double? = null,

        @field:SerializedName("subscriptionsInfo")
        val subscriptionsInfo: List<SubscriptionsInfoItem?>? = null,

        @field:SerializedName("nextBillingDateTime")
        val nextBillingDateTime: Long? = null
)

data class GetPaymentsV2ResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("nextBillInformation")
        val nextBillInformation: NextBillInformation? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("order")
        val order: List<OrderItem?>? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("previousBalance")
        val previousBalance: Double? = null
)

data class PaymentsInfoItem(

        @field:SerializedName("postingStatus")
        val postingStatus: String? = null,

        @field:SerializedName("amount")
        val amount: Double? = null,

        @field:SerializedName("paymentCategory")
        val paymentCategory: String? = null,

        @field:SerializedName("gatewayResponse")
        val gatewayResponse: String? = null,

        @field:SerializedName("paymentID")
        val paymentID: String? = null,

        @field:SerializedName("paymentMethodID")
        val paymentMethodID: String? = null,

        @field:SerializedName("creditCardNumber")
        val creditCardNumber: String? = null,

        @field:SerializedName("receivedDate")
        val receivedDate: Long? = null,

        @field:SerializedName("paymentType")
        val paymentType: String? = null
)

data class SubscriptionsInfoItem(

        @field:SerializedName("originalPrice")
        val originalPrice: Double? = null,

        @field:SerializedName("displayName")
        val displayName: String? = null
)
