package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse

interface EvergentConfirmOtpCallBack {

    fun onSuccess(confirmOtpResponse: ConfirmOtpResponse)

    fun onFailure(errorMessage: String,errorCode:String)
}