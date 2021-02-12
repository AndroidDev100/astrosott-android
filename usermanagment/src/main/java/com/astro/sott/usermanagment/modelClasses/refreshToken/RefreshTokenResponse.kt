package com.astro.sott.usermanagment.modelClasses.refreshToken

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

        @field:SerializedName("RefreshTokenResponseMessage")
        val refreshTokenResponseMessage: RefreshTokenResponseMessage? = null
)

data class RefreshTokenResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("accessToken")
        val accessToken: String? = null,

        @field:SerializedName("tokenType")
        val tokenType: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("refreshToken")
        val refreshToken: String? = null
)
