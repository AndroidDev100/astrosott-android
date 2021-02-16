package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse

interface EvergentGetContactCallback {

    fun onSuccess(createUserResponse: GetContactResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}