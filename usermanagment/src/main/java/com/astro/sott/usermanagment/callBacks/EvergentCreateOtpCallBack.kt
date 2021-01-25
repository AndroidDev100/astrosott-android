package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response

interface EvergentCreateOtpCallBack {

    fun onSuccess(createOtpResponse: CreateOtpResponse)

    fun onFailure(errorMessage: String)
}