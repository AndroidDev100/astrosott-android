package com.astro.sott.usermanagment.modelClasses.confirmOtp

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class ConfirmOtpResponse(

        @field:SerializedName("ConfirmOTPResponseMessage")
        val confirmOTPResponseMessage: ConfirmOTPResponseMessage? = null
)

data class ConfirmOTPResponseMessage(

        @field:SerializedName("cpCustomerID")
        val cpCustomerID: String? = null,

        @field:SerializedName("sessionToken")
        val sessionToken: String? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("isProfileComplete")
        val isProfileComplete: Boolean? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("token")
        val token: String? = null
)
