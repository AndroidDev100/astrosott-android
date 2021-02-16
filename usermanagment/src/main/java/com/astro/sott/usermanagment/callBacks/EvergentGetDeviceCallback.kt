package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getDevice.GetDevicesResponse

interface EvergentGetDeviceCallback {

    fun onSuccess(createUserResponse: GetDevicesResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}