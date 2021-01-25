package com.astro.sott.usermanagment.modelClasses.resetPassword

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(

        @field:SerializedName("ResetPasswordResponseMessage")
        val resetPasswordResponseMessage: ResetPasswordResponseMessage? = null
)

data class ResetPasswordResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)
