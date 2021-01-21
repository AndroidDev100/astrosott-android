package com.astro.sott.usermanagment.modelClasses.searchAccountv2

import com.google.gson.annotations.SerializedName

data class SearchAccountv2Response(

        @field:SerializedName("SearchAccountV2ResponseMessage")
        val searchAccountV2ResponseMessage: SearchAccountV2ResponseMessage? = null
)

data class SearchAccountV2ResponseMessage(

        @field:SerializedName("contactUserName")
        val contactUserName: String? = null,

        @field:SerializedName("accountID")
        val accountID: String? = null,

        @field:SerializedName("cpCustomerID")
        val cpCustomerID: String? = null,

        @field:SerializedName("channelPartnerID")
        val channelPartnerID: String? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)

data class FailureMessageItem(

        @field:SerializedName("errorMessage")
        val errorMessage: String? = null,

        @field:SerializedName("errorCode")
        val errorCode: String? = null
)