package com.astro.sott.usermanagment.modelClasses.invoice

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.google.gson.annotations.SerializedName

data class InvoiceResponse(

        @field:SerializedName("GetInvoicePDFResponseMessage")
        val getInvoicePDFResponseMessage: GetInvoicePDFResponseMessage? = null
)

data class GetInvoicePDFResponseMessage(

        @field:SerializedName("responseType")
        val responseType: String? = null,

        @field:SerializedName("responseData")
        val responseData: String? = null,

        @field:SerializedName("failureMessage")
        val failureMessage: List<FailureMessageItem?>? = null,

        @field:SerializedName("responseCode")
        val responseCode: String? = null,

        @field:SerializedName("status")
        val status: String? = null
)
