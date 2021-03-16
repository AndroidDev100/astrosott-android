package com.astro.sott.usermanagment.callBacks

import com.astro.sott.usermanagment.modelClasses.getProducts.GetProductResponse
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse

interface EvergentGetProductsCallBack {

    fun onSuccess(createUserResponse: GetProductResponse)

    fun onFailure(errorMessage: String, errorCode: String)
}