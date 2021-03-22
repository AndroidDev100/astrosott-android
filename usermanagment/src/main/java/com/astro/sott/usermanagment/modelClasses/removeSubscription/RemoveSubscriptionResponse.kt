package com.astro.sott.usermanagment.modelClasses.removeSubscription

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class RemoveSubscriptionResponse(

        @field:SerializedName("RemoveSubscriptionResponseMessage")
        val removeSubscriptionResponseMessage: RemoveSubscriptionResponseMessage? = null
)

data class RemoveSubscriptionResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("responseCode")
        val responseCode: String? = null
)
