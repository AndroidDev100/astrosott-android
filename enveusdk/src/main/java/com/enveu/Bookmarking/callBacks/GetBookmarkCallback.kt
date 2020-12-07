package com.enveu.UserManagement.callBacks

import com.enveu.Bookmarking.bean.BookmarkingResponse
import com.enveu.Bookmarking.bean.GetBookmarkResponse
import retrofit2.Response

interface GetBookmarkCallback {

    fun success(status: Boolean, loginResponse: Response<GetBookmarkResponse>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}