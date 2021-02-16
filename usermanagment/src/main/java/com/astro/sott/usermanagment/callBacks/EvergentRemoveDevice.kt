package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getDevice.GetDevicesResponse
import com.astro.sott.usermanagment.modelClasses.removeDevice.RemoveDeviceResponse

interface EvergentRemoveDevice {

    fun onSuccess(createUserResponse: RemoveDeviceResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}