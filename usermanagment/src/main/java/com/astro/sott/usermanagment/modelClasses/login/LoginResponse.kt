package com.astro.sott.usermanagment.modelClasses.login

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class LoginResponse(

        @field:SerializedName("GetOAuthAccessTokenv2ResponseMessage")
        val getOAuthAccessTokenv2ResponseMessage: GetOAuthAccessTokenv2ResponseMessage? = null
)

data class GetOAuthAccessTokenv2ResponseMessage(

        @field:SerializedName("externalSessionToken")
        val externalSessionToken: String? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("accessToken")
        val accessToken: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("refreshToken")
        val refreshToken: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)
