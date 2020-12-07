package com.enveu.UserManagement.callBacks

import com.enveu.UserManagement.bean.UserProfile.UserProfileResponse
import retrofit2.Response

interface UserProfileCallBack {

    fun success(status: Boolean, loginResponse : Response<UserProfileResponse>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}