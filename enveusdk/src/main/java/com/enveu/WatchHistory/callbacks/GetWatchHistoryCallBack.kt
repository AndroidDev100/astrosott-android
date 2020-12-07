package com.enveu.WatchHistory.callbacks

import com.enveu.WatchHistory.beans.ResponseWatchHistoryAssetList
import retrofit2.Response

interface GetWatchHistoryCallBack {

    fun success(status: Boolean, loginResponse: Response<ResponseWatchHistoryAssetList>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}