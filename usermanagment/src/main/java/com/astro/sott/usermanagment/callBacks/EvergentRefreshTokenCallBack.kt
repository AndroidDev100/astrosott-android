package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse

interface EvergentRefreshTokenCallBack {

    fun onSuccess(createUserResponse: RefreshTokenResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}