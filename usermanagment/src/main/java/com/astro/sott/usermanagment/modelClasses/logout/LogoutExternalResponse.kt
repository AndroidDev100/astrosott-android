package com.astro.sott.usermanagment.modelClasses.logout

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class LogoutExternalResponse(

    @field:SerializedName("LogOutUserResponseMessage")
    val logOutUserResponseMessage: LogOutUserResponseMessage? = null
)

data class LogOutUserResponseMessage(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("failureMessage")
    val failureMessage: List<FailureMessageItem?>? = null,

    @field:SerializedName("responseCode")
    val responseCode: String? = null
)
