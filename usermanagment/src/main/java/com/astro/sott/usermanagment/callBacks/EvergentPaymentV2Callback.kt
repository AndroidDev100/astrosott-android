package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse
import com.astro.sott.usermanagment.modelClasses.getPaymentV2.PaymentV2Response

interface EvergentPaymentV2Callback {
    fun onSuccess(createUserResponse: PaymentV2Response)

    fun onFailure(errorMessage: String, errorCode: String)
}