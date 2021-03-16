package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse

interface EvergentResponseCallBack<T> {
    fun onSuccess(response: T)

    fun onFailure(errorMessage: String, errorCode: String)
}