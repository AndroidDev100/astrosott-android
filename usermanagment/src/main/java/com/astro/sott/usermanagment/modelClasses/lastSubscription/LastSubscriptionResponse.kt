package com.astro.sott.usermanagment.modelClasses.lastSubscription

import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class LastSubscriptionResponse(

        @field:SerializedName("GetLastSubscriptionsResponseMessage")
        val getLastSubscriptionsResponseMessage: GetLastSubscriptionsResponseMessage? = null
)


data class GetLastSubscriptionsResponseMessage(

        @field:SerializedName("AccountServiceMessage")
        val accountServiceMessage: AccountServiceMessageItem? = null,

        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("responseCode")
        val responseCode: String? = null
)
