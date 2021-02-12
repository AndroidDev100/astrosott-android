package com.astro.sott.usermanagment.modelClasses.getContact

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class GetContactResponse(

        @field:SerializedName("GetContactResponseMessage")
        val getContactResponseMessage: GetContactResponseMessage? = null
)

data class GetContactResponseMessage(

        @field:SerializedName("lastLoginTime")
        val lastLoginTime: Long? = null,

        @field:SerializedName("cpCustomerID")
        val cpCustomerID: String? = null,

        @field:SerializedName("spAccountID")
        val spAccountID: String? = null,

        @field:SerializedName("verificationStatus")
        val verificationStatus: Boolean? = null,

        @field:SerializedName("contactMessage")
        val contactMessage: List<ContactMessageItem?>? = null,

        @field:SerializedName("isMobileVerified")
        val isMobileVerified: Boolean? = null,

        @field:SerializedName("accountRegistrationDate")
        val accountRegistrationDate: Long? = null,

        @field:SerializedName("message")
        val message: String? = null,
        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,
        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("isProfileComplete")
        val isProfileComplete: Boolean? = null
)

data class ContactMessageItem(

        @field:SerializedName("lastName")
        val lastName: String? = null,

        @field:SerializedName("contactID")
        val contactID: String? = null,

        @field:SerializedName("parentalControl")
        val parentalControl: Boolean? = null,

        @field:SerializedName("emailIsVerified")
        val emailIsVerified: Boolean? = null,

        @field:SerializedName("externalId")
        val externalId: String? = null,

        @field:SerializedName("main")
        val main: Boolean? = null,

        @field:SerializedName("userName")
        val userName: String? = null,

        @field:SerializedName("allowTracking")
        val allowTracking: Boolean? = null,

        @field:SerializedName("isVIP")
        val isVIP: Boolean? = null,

        @field:SerializedName("firstName")
        val firstName: String? = null,

        @field:SerializedName("alertNotificationEmail")
        val alertNotificationEmail: Boolean? = null,

        @field:SerializedName("alertNotificationPush")
        val alertNotificationPush: Boolean? = null,

        @field:SerializedName("isPasswordExists")
        val isPasswordExists: Boolean? = null,

        @field:SerializedName("pin")
        val pin: Boolean? = null,

        @field:SerializedName("isPrimaryContact")
        val isPrimaryContact: Boolean? = null,

        @field:SerializedName("email")
        val email: String? = null
)
