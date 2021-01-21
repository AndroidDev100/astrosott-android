package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response

interface EvergentSearchAccountCallBack {

    fun onSuccess(searchAccountv2Response: SearchAccountv2Response)

    fun onFailure(errorMessage: String)
}