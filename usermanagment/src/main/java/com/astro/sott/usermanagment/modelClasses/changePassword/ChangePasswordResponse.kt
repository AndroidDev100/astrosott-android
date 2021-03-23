package com.astro.sott.usermanagment.modelClasses.changePassword

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(

        @field:SerializedName("ChangePasswordResponseMessage")
        val changePasswordResponseMessage: ChangePasswordResponseMessage? = null
)

data class ChangePasswordResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("responseCode")
        val responseCode: String? = null
)
