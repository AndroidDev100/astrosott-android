package com.astro.sott.usermanagment.modelClasses.updateProfile

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

        @field:SerializedName("UpdateProfileResponseMessage")
        val updateProfileResponseMessage: UpdateProfileResponseMessage? = null
)

data class UpdateProfileResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)
