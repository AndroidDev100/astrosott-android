package com.astro.sott.usermanagment.modelClasses.createUser

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class CreateUserResponse(

        @field:SerializedName("CreateUserResponseMessage")
        val createUserResponseMessage: CreateUserResponseMessage? = null
)

data class CreateUserResponseMessage(

        @field:SerializedName("cpCustomerID")
        val cpCustomerID: String? = null,

        @field:SerializedName("spAccountID")
        val spAccountID: String? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("externalSessionToken")
        val externalSessionToken: String? = null,

        @field:SerializedName("refreshToken")
        val refreshToken: String? = null,

        @field:SerializedName("accessToken")
        val accessToken: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null
)
