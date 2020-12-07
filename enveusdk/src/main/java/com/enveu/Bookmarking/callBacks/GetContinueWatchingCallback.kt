package com.enveu.UserManagement.callBacks

import com.enveu.Bookmarking.bean.BookmarkingResponse
import com.enveu.Bookmarking.bean.GetBookmarkResponse
import com.enveu.Bookmarking.bean.continuewatching.GetContinueWatchingBean
import retrofit2.Response

interface GetContinueWatchingCallback {

    fun success(status: Boolean, loginResponse: Response<GetContinueWatchingBean>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}