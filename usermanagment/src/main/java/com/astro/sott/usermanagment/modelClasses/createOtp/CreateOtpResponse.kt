package com.astro.sott.usermanagment.modelClasses.createOtp

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class CreateOtpResponse(

    @field:SerializedName("CreateOTPResponseMessage")
    val createOTPResponseMessage: CreateOTPResponseMessage? = null
)

data class CreateOTPResponseMessage(

    @field:SerializedName("responseCode")
    val responseCode: String? = null,
    @field:SerializedName("failureMessage")
    val failureMessage: List<FailureMessageItem?>? = null,
    @field:SerializedName("currentOTPCount")
    val currentOTPCount: Int? = null,

    @field:SerializedName("maxOTPCount")
    val maxOTPCount: Int? = null

)
