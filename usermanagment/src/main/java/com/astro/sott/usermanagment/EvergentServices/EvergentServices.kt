package com.astro.sott.usermanagment.EvergentServices

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Base64
import com.astro.sott.usermanagment.callBacks.*
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse
import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse
import com.astro.sott.usermanagment.modelClasses.changePassword.ChangePasswordResponse
import com.astro.sott.usermanagment.modelClasses.checkCredential.CheckCredentialResponse
import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse
import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse
import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse
import com.astro.sott.usermanagment.modelClasses.getDevice.GetDevicesResponse
import com.astro.sott.usermanagment.modelClasses.getPaymentV2.PaymentV2Response
import com.astro.sott.usermanagment.modelClasses.getProducts.GetProductResponse
import com.astro.sott.usermanagment.modelClasses.invoice.InvoiceResponse
import com.astro.sott.usermanagment.modelClasses.lastSubscription.LastSubscriptionResponse
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse
import com.astro.sott.usermanagment.modelClasses.removeDevice.RemoveDeviceResponse
import com.astro.sott.usermanagment.modelClasses.removeSubscription.RemoveSubscriptionResponse
import com.astro.sott.usermanagment.modelClasses.resetPassword.ResetPasswordResponse
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response
import com.astro.sott.usermanagment.modelClasses.updateProfile.UpdateProfileResponse
import com.astro.sott.usermanagment.networkManager.retrofit.EvergentApiInterface
import com.astro.sott.usermanagment.networkManager.retrofit.EvergentNetworkClass
import com.astro.sott.usermanagment.utils.EvergentErrorHandling
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EvergentServices {

    companion object {
        val instance = EvergentServices()
    }

    private val CHANNEL_PARTNER_ID: String = "channelPartnerID"
    private val CHANNEL_PARTNER_ID_VALUE: String = "ASTRO"
    private val API_USER_VALUE: String = "astrosottapiuser"
    private val MOBILE_NUMBER: String = "mobileNumber"
    private val FIRST_NAME: String = "firstName"


    private val SERVICE_TYPE: String = "serviceType"
    private val SERVICE_TYPE_VALUE: String = "PRODUCT"

    private val EMAIL: String = "email"
    private val API_USER: String = "apiUser"
    private val API_PASSWORD: String = "apiPassword"


    fun searchAccountv2(context: Context, type: String, emailMobile: String, searchAccountCallBack: EvergentSearchAccountCallBack) {

        var searchAccountJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)

        if (type.equals("mobile", true)) {
            json.addProperty("alternateUserName", emailMobile)
        } else if (type.equals("email", true)) {
            json.addProperty("userName", emailMobile)

        }

        searchAccountJson.add("SearchAccountV2RequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.searchAccountV2(searchAccountJson)
        call?.enqueue(object : Callback<SearchAccountv2Response?> {
            override fun onFailure(call: Call<SearchAccountv2Response?>, t: Throwable) {
                searchAccountCallBack.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<SearchAccountv2Response?>, response: Response<SearchAccountv2Response?>) {
                if (response.body() != null && response.body()?.searchAccountV2ResponseMessage != null && response.body()?.searchAccountV2ResponseMessage?.responseCode != null) {

                    if (response.body()?.searchAccountV2ResponseMessage?.responseCode.equals("1", true)) {
                        searchAccountCallBack.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.searchAccountV2ResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.searchAccountV2ResponseMessage?.failureMessage, context)
                            searchAccountCallBack.onFailure(errorModel.errorMessage, errorModel.errorCode)
                        } else {
                            searchAccountCallBack.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    searchAccountCallBack.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun createOtp(context: Context, type: String, emailMobile: String, evergentCreateOtpCallBack: EvergentCreateOtpCallBack) {

        var searchAccountJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("country", "MY")
        if (type.equals("email", true) || type.equals("Google", true) || type.equals("Facebook", true)) {
            json.addProperty(EMAIL, emailMobile)

        } else {
            json.addProperty(MOBILE_NUMBER, emailMobile)

        }
        searchAccountJson.add("CreateOTPRequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.createOtp(searchAccountJson)
        call?.enqueue(object : Callback<CreateOtpResponse?> {
            override fun onFailure(call: Call<CreateOtpResponse?>, t: Throwable) {
                evergentCreateOtpCallBack.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<CreateOtpResponse?>, response: Response<CreateOtpResponse?>) {
                if (response.body() != null && response.body()?.createOTPResponseMessage != null && response.body()?.createOTPResponseMessage?.responseCode != null) {

                    if (response.body()?.createOTPResponseMessage?.responseCode.equals("1", true)) {
                        evergentCreateOtpCallBack.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.createOTPResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.createOTPResponseMessage?.failureMessage, context)
                            evergentCreateOtpCallBack.onFailure(errorModel.errorMessage, errorModel.errorCode)
                        } else {
                            evergentCreateOtpCallBack.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentCreateOtpCallBack.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun checkCredential(context: Context, password: String, emailMobile: String, type: String, evergentUpdateProfileCallback: EvergentResponseCallBack<CheckCredentialResponse>) {

        var updateProfileJson = JsonObject()
        var json = JsonObject()

        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)

        if (type.equals("mobile", true)) {
            json.addProperty("alternateUserName", emailMobile)
        } else {
            json.addProperty("contactUserName", emailMobile)
        }
        json.addProperty("contactPassword", password)
        updateProfileJson.add("CheckCredentialsRequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.checkCredentials(updateProfileJson)
        call?.enqueue(object : Callback<CheckCredentialResponse?> {
            override fun onFailure(call: Call<CheckCredentialResponse?>, t: Throwable) {
                evergentUpdateProfileCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<CheckCredentialResponse?>, response: Response<CheckCredentialResponse?>) {
                if (response.body() != null && response.body()?.checkCredentialsResponseMessage != null && response.body()?.checkCredentialsResponseMessage?.responseCode != null) {

                    if (response.body()?.checkCredentialsResponseMessage?.responseCode.equals("1", true)) {
                        evergentUpdateProfileCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.checkCredentialsResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.checkCredentialsResponseMessage?.failureMessage, context)
                            evergentUpdateProfileCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)
                        } else {
                            evergentUpdateProfileCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentUpdateProfileCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun updateProfile(context: Context, type: String, emailMobile: String, acessToken: String, evergentUpdateProfileCallback: EvergentResponseCallBack<UpdateProfileResponse>) {

        var updateProfileJson = JsonObject()
        var json = JsonObject()
        if (type.equals("email", true)) {
            json.addProperty(EMAIL, emailMobile)
            json.addProperty("customerUsername", emailMobile)


        } else if (type.equals("name", true)) {
            json.addProperty(FIRST_NAME, emailMobile)
        } else if (type.equals("mobile", true)) {
            json.addProperty(MOBILE_NUMBER, emailMobile)
            json.addProperty("alternateUserName", emailMobile)


        }
        updateProfileJson.add("UpdateProfileRequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.updateProfile("Bearer $acessToken", updateProfileJson)
        call?.enqueue(object : Callback<UpdateProfileResponse?> {
            override fun onFailure(call: Call<UpdateProfileResponse?>, t: Throwable) {
                evergentUpdateProfileCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<UpdateProfileResponse?>, response: Response<UpdateProfileResponse?>) {
                if (response.body() != null && response.body()?.updateProfileResponseMessage != null && response.body()?.updateProfileResponseMessage?.responseCode != null) {

                    if (response.body()?.updateProfileResponseMessage?.responseCode.equals("1", true)) {
                        evergentUpdateProfileCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.updateProfileResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.updateProfileResponseMessage?.failureMessage, context)
                            evergentUpdateProfileCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)
                        } else {
                            evergentUpdateProfileCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentUpdateProfileCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun confirmOtp(context: Context, type: String, emailMobile: String, otp: String, evergentConfirmOtpCallBack: EvergentConfirmOtpCallBack) {

        var searchAccountJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("country", "MY")
        if (type.equals("email", true)) {
            json.addProperty("email", emailMobile)
        } else {
            json.addProperty("mobileNumber", emailMobile)

        }
        json.addProperty("otp", otp)
        json.addProperty("isGenerateToken", "true")
        searchAccountJson.add("ConfirmOTPRequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.confirmOtp(searchAccountJson)
        call?.enqueue(object : Callback<ConfirmOtpResponse?> {
            override fun onFailure(call: Call<ConfirmOtpResponse?>, t: Throwable) {
                evergentConfirmOtpCallBack.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<ConfirmOtpResponse?>, response: Response<ConfirmOtpResponse?>) {
                if (response.body() != null && response.body()?.confirmOTPResponseMessage != null && response.body()?.confirmOTPResponseMessage?.responseCode != null) {

                    if (response.body()?.confirmOTPResponseMessage?.responseCode.equals("1", true)) {
                        evergentConfirmOtpCallBack.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.confirmOTPResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.confirmOTPResponseMessage?.failureMessage, context)
                            evergentConfirmOtpCallBack.onFailure(errorModel.errorMessage, errorModel.errorCode)
                        } else {
                            evergentConfirmOtpCallBack.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentConfirmOtpCallBack.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun resetPassword(token: String, context: Context, password: String, evergentResetPasswordCallBack: EvergentResetPasswordCallBack) {

        var searchAccountJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("contactPassword", password)
        json.addProperty("userToken", token)
        searchAccountJson.add("ResetPasswordRequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.resetPassword(searchAccountJson)
        call?.enqueue(object : Callback<ResetPasswordResponse?> {
            override fun onFailure(call: Call<ResetPasswordResponse?>, t: Throwable) {
                evergentResetPasswordCallBack.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<ResetPasswordResponse?>, response: Response<ResetPasswordResponse?>) {
                if (response.body() != null && response.body()?.resetPasswordResponseMessage != null && response.body()?.resetPasswordResponseMessage?.responseCode != null) {

                    if (response.body()?.resetPasswordResponseMessage?.responseCode.equals("1", true)) {
                        evergentResetPasswordCallBack.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.resetPasswordResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.resetPasswordResponseMessage?.failureMessage, context)
                            evergentResetPasswordCallBack.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentResetPasswordCallBack.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentResetPasswordCallBack.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun createUser(context: Context, type: String, emailMobile: String, password: String, name: String, evergentCreateUserCallback: EvergentCreateUserCallback) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        var devicejson = JsonObject()
        var accountAttributes = JsonArray();
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("isGenerateJWT", true)

        if (type.equals("email", true)) {
            json.addProperty("email", emailMobile)
            json.addProperty("customerUsername", emailMobile)
            json.addProperty("customerPassword", password)

        } else if (type.equals("mobile", true)) {
            json.addProperty("mobileNumber", emailMobile)
            json.addProperty("alternateUserName", emailMobile)
            json.addProperty("customerPassword", password)

        } else if (type.equals("Facebook", true) || type.equals("Google", true)) {
            json.addProperty("email", emailMobile)
            json.addProperty("customerUsername", emailMobile)

            json.addProperty("socialLoginID", password)
            json.addProperty("socialLoginType", type)
        }
        if (name.equals("", true)) {
            json.addProperty("firstName", "Sooka Superstar")
        } else {
            json.addProperty("firstName", name)
        }

        devicejson.addProperty("serialNo", getDeviceId(context.contentResolver))
        devicejson.addProperty("deviceName", Build.DEVICE)
        devicejson.addProperty("deviceType", "Android")
        devicejson.addProperty("modelNo", Build.MODEL)
        devicejson.addProperty("os", "Android")
        json.add("deviceMessage", devicejson)
        var termJson = JsonObject()
        termJson.addProperty("acceptTerms", true)
        var privacyJson = JsonObject()
        privacyJson.addProperty("acceptPrivacy", true)
        accountAttributes.add(termJson)
        accountAttributes.add(privacyJson)
        json.add("accountAttributes", accountAttributes)
        createUserJson.add("CreateUserRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.createUser(createUserJson)
        call?.enqueue(object : Callback<CreateUserResponse?> {
            override fun onFailure(call: Call<CreateUserResponse?>, t: Throwable) {
                evergentCreateUserCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<CreateUserResponse?>, response: Response<CreateUserResponse?>) {
                if (response.body() != null && response.body()?.createUserResponseMessage != null && response.body()?.createUserResponseMessage?.responseCode != null) {

                    if (response.body()?.createUserResponseMessage?.responseCode.equals("1", true)) {
                        evergentCreateUserCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.createUserResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.createUserResponseMessage?.failureMessage, context)
                            evergentCreateUserCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentCreateUserCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentCreateUserCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun loginUser(context: Context, type: String, emailMobile: String, password: String, evergentCreateUserCallback: EvergentLoginUserCallback) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        var devicejson = JsonObject()
        var accountAttributes = JsonArray();
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)

        if (type.equals("email", true)) {
            json.addProperty("contactUserName", emailMobile)
            json.addProperty("contactPassword", password)

        } else if (type.equals("mobile", true)) {
            json.addProperty("alternateUserName", emailMobile)
            json.addProperty("contactPassword", password)


        } else if (type.equals("Facebook", true) || type.equals("Google", true)) {
            json.addProperty("socialLoginID", password)
            json.addProperty("socialLoginType", type)
        }
        devicejson.addProperty("serialNo", getDeviceId(context.contentResolver))
        devicejson.addProperty("deviceName", Build.DEVICE)
        devicejson.addProperty("deviceType", "Android")
        devicejson.addProperty("modelNo", Build.MODEL)
        devicejson.addProperty("os", "Android")
        json.add("deviceMessage", devicejson)
        createUserJson.add("GetOAuthAccessTokenv2RequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.login(createUserJson)
        call?.enqueue(object : Callback<LoginResponse?> {
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                evergentCreateUserCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                if (response.body() != null && response.body()?.getOAuthAccessTokenv2ResponseMessage != null && response.body()?.getOAuthAccessTokenv2ResponseMessage?.responseCode != null) {

                    if (response.body()?.getOAuthAccessTokenv2ResponseMessage?.responseCode.equals("1", true)) {
                        evergentCreateUserCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.getOAuthAccessTokenv2ResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getOAuthAccessTokenv2ResponseMessage?.failureMessage, context)
                            evergentCreateUserCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentCreateUserCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentCreateUserCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun getDeviceId(contentResolver: ContentResolver?): String? {
        return Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID)
    }

    fun getContact(context: Context, acessToken: String, evergentCreateUserCallback: EvergentGetContactCallback) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        createUserJson.add("GetContactRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getContact("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<GetContactResponse?> {
            override fun onFailure(call: Call<GetContactResponse?>, t: Throwable) {
                evergentCreateUserCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<GetContactResponse?>, response: Response<GetContactResponse?>) {
                if (response.body() != null && response.body()?.getContactResponseMessage != null && response.body()?.getContactResponseMessage?.responseCode != null) {

                    if (response.body()?.getContactResponseMessage?.responseCode.equals("1", true)) {
                        evergentCreateUserCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.getContactResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getContactResponseMessage?.failureMessage, context)
                            evergentCreateUserCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentCreateUserCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentCreateUserCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun changePassword(context: Context, acessToken: String, oldPassword: String, newPassword: String, evergentGetDeviceCallback: EvergentResponseCallBack<ChangePasswordResponse>) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("oldPassword", oldPassword)
        json.addProperty("newPassword", newPassword)
        json.addProperty("confirmNewpassword", newPassword)


        createUserJson.add("ChangePasswordRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.changePassword("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<ChangePasswordResponse?> {
            override fun onFailure(call: Call<ChangePasswordResponse?>, t: Throwable) {
                evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<ChangePasswordResponse?>, response: Response<ChangePasswordResponse?>) {
                if (response.body() != null && response.body()?.changePasswordResponseMessage != null && response.body()?.changePasswordResponseMessage?.responseCode != null) {

                    if (response.body()?.changePasswordResponseMessage?.responseCode.equals("1", true)) {
                        evergentGetDeviceCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.changePasswordResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.changePasswordResponseMessage?.failureMessage, context)
                            evergentGetDeviceCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentGetDeviceCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun getActiveSubscripton(context: Context, acessToken: String, from: String, evergentGetDeviceCallback: EvergentResponseCallBack<GetActiveResponse>) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        if (from.equals("profile", true)) {
            json.addProperty("returnLiveEvents", "F")
        }
        createUserJson.add("GetActiveSubscriptionsRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getActiveSubscription("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<GetActiveResponse?> {
            override fun onFailure(call: Call<GetActiveResponse?>, t: Throwable) {
                evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<GetActiveResponse?>, response: Response<GetActiveResponse?>) {
                if (response.body() != null && response.body()?.getActiveSubscriptionsResponseMessage != null && response.body()?.getActiveSubscriptionsResponseMessage?.responseCode != null) {

                    if (response.body()?.getActiveSubscriptionsResponseMessage?.responseCode.equals("1", true)) {
                        evergentGetDeviceCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.getActiveSubscriptionsResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getActiveSubscriptionsResponseMessage?.failureMessage, context)
                            evergentGetDeviceCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentGetDeviceCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun getLastSubscripton(context: Context, acessToken: String, evergentGetDeviceCallback: EvergentResponseCallBack<LastSubscriptionResponse>) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        createUserJson.add("GetLastSubscriptionsRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getLastSubscription("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<LastSubscriptionResponse?> {
            override fun onFailure(call: Call<LastSubscriptionResponse?>, t: Throwable) {
                evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<LastSubscriptionResponse?>, response: Response<LastSubscriptionResponse?>) {
                if (response.body() != null && response.body()?.getLastSubscriptionsResponseMessage != null && response.body()?.getLastSubscriptionsResponseMessage?.responseCode != null) {

                    if (response.body()?.getLastSubscriptionsResponseMessage?.responseCode.equals("1", true)) {
                        evergentGetDeviceCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.getLastSubscriptionsResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getLastSubscriptionsResponseMessage?.failureMessage, context)
                            evergentGetDeviceCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentGetDeviceCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun addSubscription(context: Context, productId: String, token: String, acessToken: String, orderId: String, evergentGetDeviceCallback: EvergentResponseCallBack<AddSubscriptionResponse>) {

        var purchaseTokenKJson = JsonObject()
        purchaseTokenKJson.addProperty("purchaseToken", token)
        purchaseTokenKJson.addProperty("orderId", orderId)

        var txId = Base64.encodeToString(purchaseTokenKJson.toString().toByteArray(), 1)

        var createUserJson = JsonObject()
        var json = JsonObject()
        var paymentMethodJson = JsonObject()
        var transactionRefMsgJson = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("cpCustomerID", "")
        json.addProperty("rateType", "")
        json.addProperty(SERVICE_TYPE, SERVICE_TYPE_VALUE)
        json.addProperty("serviceID", "")
        json.addProperty("appServiceID", productId)
        json.addProperty("priceCharged", "")
        paymentMethodJson.addProperty("label", "Google Wallet")
        transactionRefMsgJson.addProperty("amount", "")
        transactionRefMsgJson.addProperty("txID", txId)

        paymentMethodJson.add("transactionReferenceMsg", transactionRefMsgJson)
        json.add("paymentmethodInfo", paymentMethodJson)
        createUserJson.add("AddSubscriptionRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.addSubscription("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<AddSubscriptionResponse?> {
            override fun onFailure(call: Call<AddSubscriptionResponse?>, t: Throwable) {
                evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<AddSubscriptionResponse?>, response: Response<AddSubscriptionResponse?>) {
                if (response.body() != null && response.body()?.addSubscriptionResponseMessage != null && response.body()?.addSubscriptionResponseMessage?.responseCode != null) {
                    if (response.body()?.addSubscriptionResponseMessage?.responseCode.equals("1", true)) {
                        evergentGetDeviceCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.addSubscriptionResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.addSubscriptionResponseMessage?.failureMessage, context)
                            evergentGetDeviceCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentGetDeviceCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )

    }


    fun getDevice(context: Context, acessToken: String, evergentGetDeviceCallback: EvergentGetDeviceCallback) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        createUserJson.add("GetAccountDevicesRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getDevice("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<GetDevicesResponse?> {
            override fun onFailure(call: Call<GetDevicesResponse?>, t: Throwable) {
                evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<GetDevicesResponse?>, response: Response<GetDevicesResponse?>) {
                if (response.body() != null && response.body()?.getAccountDevicesResponseMessage != null && response.body()?.getAccountDevicesResponseMessage?.responseCode != null) {

                    if (response.body()?.getAccountDevicesResponseMessage?.responseCode.equals("1", true)) {
                        evergentGetDeviceCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.getAccountDevicesResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getAccountDevicesResponseMessage?.failureMessage, context)
                            evergentGetDeviceCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentGetDeviceCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun removeDevice(context: Context, acessToken: String, serial: String, evergentGetDeviceCallback: EvergentRemoveDevice) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        var devicejson = JsonObject()
        devicejson.addProperty("serialNo", serial)
        json.add("deviceMessage", devicejson)
        createUserJson.add("RemoveDevicesRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.removeDevice("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<RemoveDeviceResponse?> {
            override fun onFailure(call: Call<RemoveDeviceResponse?>, t: Throwable) {
                evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<RemoveDeviceResponse?>, response: Response<RemoveDeviceResponse?>) {
                if (response.body() != null && response.body()?.removeDevicesResponseMessage != null && response.body()?.removeDevicesResponseMessage?.responseCode != null) {

                    if (response.body()?.removeDevicesResponseMessage?.responseCode.equals("1", true)) {
                        evergentGetDeviceCallback.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.removeDevicesResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.removeDevicesResponseMessage?.failureMessage, context)
                            evergentGetDeviceCallback.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentGetDeviceCallback.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentGetDeviceCallback.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun getProduct(context: Context, evergentRefreshToken: EvergentGetProductsCallBack) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        var appChannelJson = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("dmaID", "001");
        json.addProperty("returnAppChannels", "T");
        appChannelJson.addProperty("appChannel", "Google Wallet")
        json.add("appChannels", appChannelJson)
        createUserJson.add("GetProductsRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getProducts(createUserJson)
        call?.enqueue(object : Callback<GetProductResponse?> {
            override fun onFailure(call: Call<GetProductResponse?>, t: Throwable) {
                evergentRefreshToken.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<GetProductResponse?>, response: Response<GetProductResponse?>) {
                if (response.body() != null && response.body()?.getProductsResponseMessage != null && response.body()?.getProductsResponseMessage?.responseCode != null) {

                    if (response.body()?.getProductsResponseMessage?.responseCode.equals("1", true)) {
                        evergentRefreshToken.onSuccess(response.body()!!)
                    } else {
                        if (response.body()?.getProductsResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getProductsResponseMessage?.failureMessage, context)
                            evergentRefreshToken.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentRefreshToken.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentRefreshToken.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun getProductforLogin(subscriptionId: JsonArray, context: Context, accessToken: String, evergentRefreshToken: EvergentGetProductsCallBack) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        var appChannelJson = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("dmaID", "001");
        json.addProperty("returnAppChannels", "T");
        appChannelJson.addProperty("appChannel", "Google Wallet")
        json.add("appChannels", appChannelJson)
        json.add("ovpSKUs", subscriptionId)
        createUserJson.add("GetProductsRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getProducts("Bearer $accessToken", createUserJson)
        call?.enqueue(object : Callback<GetProductResponse?> {
            override fun onFailure(call: Call<GetProductResponse?>, t: Throwable) {
                evergentRefreshToken.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<GetProductResponse?>, response: Response<GetProductResponse?>) {
                if (response.body() != null && response.body()?.getProductsResponseMessage != null && response.body()?.getProductsResponseMessage?.responseCode != null) {

                    if (response.body()?.getProductsResponseMessage?.responseCode.equals("1", true)) {
                        evergentRefreshToken.onSuccess(response.body()!!)
                    } else {
                        if (response.body()?.getProductsResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getProductsResponseMessage?.failureMessage, context)
                            evergentRefreshToken.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentRefreshToken.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentRefreshToken.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun getPaymentV2(context: Context, acessToken: String, evergentRefreshToken: EvergentPaymentV2Callback) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        createUserJson.add("GetPaymentsV2RequestMessage", json)

        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getPaymentV2("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<PaymentV2Response?> {
            override fun onFailure(call: Call<PaymentV2Response?>, t: Throwable) {
                evergentRefreshToken.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<PaymentV2Response?>, response: Response<PaymentV2Response?>) {
                if (response.body() != null && response.body()?.getPaymentsV2ResponseMessage != null && response.body()?.getPaymentsV2ResponseMessage?.responseCode != null) {

                    if (response.body()?.getPaymentsV2ResponseMessage?.responseCode.equals("1", true)) {
                        evergentRefreshToken.onSuccess(response.body()!!)
                    } else {
                        if (response.body()?.getPaymentsV2ResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getPaymentsV2ResponseMessage?.failureMessage, context)
                            evergentRefreshToken.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentRefreshToken.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentRefreshToken.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun removeSubscription(context: Context, productId: String, acessToken: String, evergentRefreshToken: EvergentResponseCallBack<RemoveSubscriptionResponse>) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        json.addProperty(SERVICE_TYPE, SERVICE_TYPE_VALUE)
        json.addProperty("serviceID", productId)
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        createUserJson.add("RemoveSubscriptionRequestMessage", json)

        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getRemoveSubscription("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<RemoveSubscriptionResponse?> {
            override fun onFailure(call: Call<RemoveSubscriptionResponse?>, t: Throwable) {
                evergentRefreshToken.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<RemoveSubscriptionResponse?>, response: Response<RemoveSubscriptionResponse?>) {
                if (response.body() != null && response.body()?.removeSubscriptionResponseMessage != null && response.body()?.removeSubscriptionResponseMessage?.responseCode != null) {

                    if (response.body()?.removeSubscriptionResponseMessage?.responseCode.equals("1", true)) {
                        evergentRefreshToken.onSuccess(response.body()!!)
                    } else {
                        if (response.body()?.removeSubscriptionResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.removeSubscriptionResponseMessage?.failureMessage, context)
                            evergentRefreshToken.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentRefreshToken.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentRefreshToken.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


    fun getInvoice(context: Context, transactionId: String, acessToken: String, evergentRefreshToken: EvergentResponseCallBack<InvoiceResponse>) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        json.addProperty("paymentID", transactionId)
        createUserJson.add("GetInvoicePDFRequestMessage", json)

        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.getInvoice("Bearer $acessToken", createUserJson)
        call?.enqueue(object : Callback<InvoiceResponse?> {
            override fun onFailure(call: Call<InvoiceResponse?>, t: Throwable) {
                evergentRefreshToken.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<InvoiceResponse?>, response: Response<InvoiceResponse?>) {
                if (response.body() != null && response.body()?.getInvoicePDFResponseMessage != null && response.body()?.getInvoicePDFResponseMessage?.responseCode != null) {

                    if (response.body()?.getInvoicePDFResponseMessage?.responseCode.equals("1", true)) {
                        evergentRefreshToken.onSuccess(response.body()!!)
                    } else {
                        if (response.body()?.getInvoicePDFResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.getInvoicePDFResponseMessage?.failureMessage, context)
                            evergentRefreshToken.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentRefreshToken.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentRefreshToken.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }

    fun refreshToken(context: Context, refreshToken: String, evergentRefreshToken: EvergentRefreshTokenCallBack) {

        var createUserJson = JsonObject()
        var json = JsonObject()
        json.addProperty(CHANNEL_PARTNER_ID, CHANNEL_PARTNER_ID_VALUE)
        json.addProperty("refreshToken", refreshToken)
        createUserJson.add("RefreshTokenRequestMessage", json)


        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.refreshToken(createUserJson)
        call?.enqueue(object : Callback<RefreshTokenResponse?> {
            override fun onFailure(call: Call<RefreshTokenResponse?>, t: Throwable) {
                evergentRefreshToken.onFailure("Something Went Wrong", "")

            }

            override fun onResponse(call: Call<RefreshTokenResponse?>, response: Response<RefreshTokenResponse?>) {
                if (response.body() != null && response.body()?.refreshTokenResponseMessage != null && response.body()?.refreshTokenResponseMessage?.responseCode != null) {

                    if (response.body()?.refreshTokenResponseMessage?.responseCode.equals("1", true)) {
                        evergentRefreshToken.onSuccess(response.body()!!)
                    } else {
                        if (response.body()?.refreshTokenResponseMessage?.failureMessage != null) {
                            var errorModel = EvergentErrorHandling().getErrorMessage(response.body()?.refreshTokenResponseMessage?.failureMessage, context)
                            evergentRefreshToken.onFailure(errorModel.errorMessage, errorModel.errorCode)

                        } else {
                            evergentRefreshToken.onFailure("Something Went Wrong", "")
                        }
                    }

                } else {
                    evergentRefreshToken.onFailure("Something Went Wrong", "")

                }
            }
        }

        )


    }


}