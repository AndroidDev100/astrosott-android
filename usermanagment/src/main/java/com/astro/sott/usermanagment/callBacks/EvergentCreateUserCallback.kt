package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response

interface EvergentCreateUserCallback {

    fun onSuccess(createUserResponse: CreateUserResponse)

    fun onFailure(errorMessage: String,errorCode:String)
}