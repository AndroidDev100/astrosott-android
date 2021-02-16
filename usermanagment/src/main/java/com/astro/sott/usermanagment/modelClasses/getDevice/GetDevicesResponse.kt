package com.astro.sott.usermanagment.modelClasses.getDevice

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class GetDevicesResponse(

        @field:SerializedName("GetAccountDevicesResponseMessage")
        val getAccountDevicesResponseMessage: GetAccountDevicesResponseMessage? = null
)

data class AccountDeviceDetailsItem(

        @field:SerializedName("deviceType")
        val deviceType: String? = null,

        @field:SerializedName("owner")
        val owner: String? = null,

        @field:SerializedName("lastLoginTime")
        val lastLoginTime: String? = null,

        @field:SerializedName("sendCarton")
        val sendCarton: Boolean? = null,

        @field:SerializedName("returnLater")
        val returnLater: Boolean? = null,

        @field:SerializedName("modelNo")
        val modelNo: String? = null,

        @field:SerializedName("deviceName")
        val deviceName: String? = null,

        @field:SerializedName("deviceId")
        val deviceId: String? = null,

        @field:SerializedName("startDate")
        val startDate: Long? = null,

        @field:SerializedName("serialNo")
        val serialNo: String? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("userAgent")
        val userAgent: String? = null
)

data class GetAccountDevicesResponseMessage(

        @field:SerializedName("AccountDeviceDetails")
        val accountDeviceDetails: List<AccountDeviceDetailsItem?>? = null,

        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("responseCode")
        val responseCode: String? = null
)
