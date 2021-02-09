package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse
import com.astro.sott.usermanagment.modelClasses.resetPassword.ResetPasswordResponse

interface EvergentResetPasswordCallBack {

    fun onSuccess(resetPasswordResponse: ResetPasswordResponse)

    fun onFailure(errorMessage: String,errorCode:String)
}