package com.astro.sott.usermanagment.networkManager.retrofit

import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse
import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse
import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse
import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse
import com.astro.sott.usermanagment.modelClasses.getDevice.GetDevicesResponse
import com.astro.sott.usermanagment.modelClasses.getPaymentV2.PaymentV2Response
import com.astro.sott.usermanagment.modelClasses.getProducts.GetProductResponse
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse
import com.astro.sott.usermanagment.modelClasses.removeDevice.RemoveDeviceResponse
import com.astro.sott.usermanagment.modelClasses.removeSubscription.RemoveSubscriptionResponse
import com.astro.sott.usermanagment.modelClasses.resetPassword.ResetPasswordResponse
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EvergentApiInterface {

    @POST("astro/searchAccountV2")
    fun searchAccountV2(@Body jsonObject: JsonObject?): Call<SearchAccountv2Response?>?

    @POST("astro/createOTP")
    fun createOtp(@Body jsonObject: JsonObject?): Call<CreateOtpResponse?>?

    @POST("astro/confirmOTP")
    fun confirmOtp(@Body jsonObject: JsonObject?): Call<ConfirmOtpResponse?>?

    @POST("astro/resetPassword")
    fun resetPassword(@Body jsonObject: JsonObject?): Call<ResetPasswordResponse?>?

    @POST("astro/createUser")
    fun createUser(@Body jsonObject: JsonObject?): Call<CreateUserResponse?>?

    @POST("astro/getOAuthAccessTokenv2")
    fun login(@Body jsonObject: JsonObject?): Call<LoginResponse?>?

    @POST("astro/getContact")
    fun getContact(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<GetContactResponse?>?

    @POST("astro/refreshToken")
    fun refreshToken(@Body jsonObject: JsonObject?): Call<RefreshTokenResponse?>?

    @POST("astro/removeDevices")
    fun removeDevice(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<RemoveDeviceResponse?>?

    @POST("astro/getProducts")
    fun getProducts(@Body jsonObject: JsonObject?): Call<GetProductResponse?>?

    @POST("astro/removeSubscription")
    fun getRemoveSubscription(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<RemoveSubscriptionResponse?>?

    @POST("astro/getPaymentsV2")
    fun getPaymentV2(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<PaymentV2Response?>?

    @POST("astro/getAccountDevices")
    fun getDevice(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<GetDevicesResponse?>?

    @POST("astro/getActiveSubscriptions")
    fun getActiveSubscription(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<GetActiveResponse?>?

    @POST("astro/addSubscription")
    fun addSubscription(@Header("Authorization") key: String, @Body jsonObject: JsonObject?): Call<AddSubscriptionResponse?>?


}