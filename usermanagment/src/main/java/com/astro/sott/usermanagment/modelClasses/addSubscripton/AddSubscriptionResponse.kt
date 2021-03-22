package com.astro.sott.usermanagment.modelClasses.addSubscripton

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class AddSubscriptionResponse(

	@field:SerializedName("AddSubscriptionResponseMessage")
	val addSubscriptionResponseMessage: AddSubscriptionResponseMessage? = null
)

data class AddSubscriptionResponseMessage(

		@field:SerializedName("orderedProductIds")
	val orderedProductIds: List<String?>? = null,

		@field:SerializedName("orderID")
	val orderID: String? = null,

		@field:SerializedName("validityTill")
	val validityTill: Long? = null,

		@field:SerializedName("message")
	val message: String? = null,
		@field:SerializedName("failureMessage")
	val failureMessage: List<FailureMessageItem?>? = null,
		@field:SerializedName("responseCode")
	val responseCode: String? = null
)
