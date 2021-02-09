package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse

interface EvergentLoginUserCallback {
    fun onSuccess(createUserResponse: LoginResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}