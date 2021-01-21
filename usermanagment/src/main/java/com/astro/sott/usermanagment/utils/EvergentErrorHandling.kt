package com.astro.sott.usermanagment.utils

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.FailureMessageItem

class EvergentErrorHandling {

    fun getErrorMessage(errorBody: List<FailureMessageItem?>?): String {
        var message = ""
        if (errorBody != null && errorBody.size!! > 0) {
            message = errorBody[0]?.errorMessage!!
        }


        return message
    }
}