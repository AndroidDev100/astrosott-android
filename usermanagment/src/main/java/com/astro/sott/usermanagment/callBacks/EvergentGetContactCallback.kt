package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse

interface EvergentGetContactCallback {

    fun onSuccess(createUserResponse: GetContactResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}