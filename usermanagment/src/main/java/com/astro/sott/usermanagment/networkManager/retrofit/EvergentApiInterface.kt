package com.astro.sott.usermanagment.networkManager.retrofit

import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse
import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse
import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse
import com.astro.sott.usermanagment.modelClasses.resetPassword.ResetPasswordResponse
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EvergentApiInterface {

    @POST("searchAccountV2")
    fun searchAccountV2(@Body jsonObject: JsonObject?): Call<SearchAccountv2Response?>?

    @POST("createOTP")
    fun createOtp(@Body jsonObject: JsonObject?): Call<CreateOtpResponse?>?

    @POST("confirmOTP")
    fun confirmOtp(@Body jsonObject: JsonObject?): Call<ConfirmOtpResponse?>?

    @POST("resetPassword")
    fun resetPassword(@Body jsonObject: JsonObject?): Call<ResetPasswordResponse?>?

    @POST("createUser")
    fun createUser(@Body jsonObject: JsonObject?): Call<CreateUserResponse?>?

    @POST("getOAuthAccessTokenv2")
    fun login(@Body jsonObject: JsonObject?): Call<LoginResponse?>?

    @POST("getContact")
    fun getContact(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<GetContactResponse?>?

    @POST("refreshToken")
    fun refreshToken(@Body jsonObject: JsonObject?): Call<RefreshTokenResponse?>?

}