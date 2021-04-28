package com.astro.sott.usermanagment.modelClasses.checkCredential

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class CheckCredentialResponse(

        @field:SerializedName("CheckCredentialsResponseMessage")
        val checkCredentialsResponseMessage: CheckCredentialsResponseMessage? = null
)


data class CheckCredentialsResponseMessage(

        @field:SerializedName("success")
        val success: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null
)
