package com.astro.sott.usermanagment.modelClasses.removeDevice

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class RemoveDeviceResponse(

        @field:SerializedName("RemoveDevicesResponseMessage")
        val removeDevicesResponseMessage: RemoveDevicesResponseMessage? = null
)

data class RemoveDevicesResponseMessage(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)
