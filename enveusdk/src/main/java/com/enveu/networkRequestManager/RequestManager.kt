package com.enveu.networkRequestManager

import android.util.Log
import com.enveu.BaseClient.BaseConfiguration
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory
import com.enveu.BaseCollection.BaseCategoryModel.ModelGenerator
import com.enveu.Bookmarking.bean.BookmarkingResponse
import com.enveu.Bookmarking.bean.GetBookmarkResponse
import com.enveu.Bookmarking.bean.continuewatching.GetContinueWatchingBean
import com.enveu.CallBacks.EnveuCallBacks
import com.enveu.UserManagement.bean.LoginResponse.LoginResponseModel
import com.enveu.UserManagement.bean.UserProfile.UserProfileResponse
import com.enveu.UserManagement.callBacks.*
import com.enveu.UserManagement.params.UserManagement
import com.enveu.WatchHistory.beans.ResponseWatchHistoryAssetList
import com.enveu.WatchHistory.callbacks.GetWatchHistoryCallBack
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.watcho.enveu.bean.EnveuCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class RequestManager {

    companion object {
        val instance = RequestManager()
    }

    fun categoryCall(screenId: String, enveuCallBacks: EnveuCallBacks) {
        val endPoint = NetworkSetup().client?.create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint?.categoryService(BaseConfiguration.instance.clients?.getDeviceType().toString(), BaseConfiguration.instance.clients?.getPlatform().toString()!!, BaseConfiguration.instance.clients?.getApiKey().toString(), screenId)
        call?.enqueue(object : Callback<EnveuCategory> {
            override fun onResponse(call: Call<EnveuCategory>, response: Response<EnveuCategory>) {

                try {
                    val model = ModelGenerator.instance.setGateWay(BaseConfiguration.instance.clients?.getGateway()).createModel(response)
                    enveuCallBacks.success(true, sortModelBasedOnDisplayOrder(model))
                }catch (ignored : Exception){
                   // Log.e("Response", ignored.message)
                }

            }

            override fun onFailure(call: Call<EnveuCategory>, t: Throwable) {
                t.message?.let { enveuCallBacks.failure(false, 0, it) }
                val model = ModelGenerator.instance.setGateWay(BaseConfiguration.instance.clients?.getGateway()).createModel(t)
                enveuCallBacks.success(false, model)
            }
        })
    }

    private fun sortModelBasedOnDisplayOrder(model: List<BaseCategory>): List<BaseCategory> {
        val comparator = compareBy<BaseCategory> { it.displayOrder }
        val anotherComparator = comparator.thenBy { it.displayOrder }
        val models=model.sortedWith(anotherComparator)
        return ArrayList(models);
    }

    fun loginCall(userName: String,password: String, loginCallBacks: LoginCallBack) {
        val endPoint = NetworkSetup().userMngmtClient?.create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val requestParam = JsonObject()
        requestParam.addProperty(UserManagement.email.name, userName)
        requestParam.addProperty(UserManagement.password.name, password)

        val call = endPoint?.getLogin(requestParam)
        call?.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun registerCall(userName: String, email: String,password: String, loginCallBacks: LoginCallBack) {
        val endPoint = NetworkSetup().userMngmtClient?.create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val requestParam = JsonObject()
        requestParam.addProperty("name", userName)
        requestParam.addProperty(UserManagement.email.name, email)
        requestParam.addProperty(UserManagement.password.name, password)


        val call = endPoint?.getSignUp(requestParam)
        call?.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun bookmarkVideo(token: String, assetId: Int, position: Int, bookmarkingCallback: BookmarkingCallback) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val requestParam = JsonObject()
        val call = endPoint?.bookmarkVideo(assetId, position)
        call?.enqueue(object : Callback<BookmarkingResponse> {
            override fun onResponse(call: Call<BookmarkingResponse>, response: Response<BookmarkingResponse>) {
                bookmarkingCallback.success(true, response)
            }

            override fun onFailure(call: Call<BookmarkingResponse>, t: Throwable) {
                t.message?.let { bookmarkingCallback.failure(false, 0, it) }
                bookmarkingCallback.failure(false, 0, "")
            }
        })
    }

    fun getBookmarkByVideoId(token: String, videoId: Int, getBookmarkCallback: GetBookmarkCallback) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint.getBookmarkByVideoId(videoId)
        call.enqueue(object : Callback<GetBookmarkResponse> {

            override fun onResponse(call: Call<GetBookmarkResponse>, response: Response<GetBookmarkResponse>) {
                getBookmarkCallback.success(true, response)
            }
            override fun onFailure(call: Call<GetBookmarkResponse>, t: Throwable) {
                getBookmarkCallback.failure(false, 0, "")
            }
        })
    }

    fun finishBookmark(token: String, assestId: Int, bookmarkingCallback: BookmarkingCallback) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint.finishBookmark(assestId)
        call.enqueue(object : Callback<BookmarkingResponse> {
            override fun onResponse(call: Call<BookmarkingResponse>, response: Response<BookmarkingResponse>) {
                bookmarkingCallback.success(true, response)
            }
            override fun onFailure(call: Call<BookmarkingResponse>, t: Throwable) {
                bookmarkingCallback.failure(false, 0, "")
            }
        })
    }

    fun fbLoginCall(params: JsonObject, loginCallBacks: LoginCallBack) {
        val endPoint = NetworkSetup().userMngmtClient?.create<EnveuEndpoints>(EnveuEndpoints::class.java)

        val call = endPoint?.getFbLogin(params)
        call?.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun fbForceLoginCall(params: JsonObject, loginCallBacks: LoginCallBack) {
        val endPoint = NetworkSetup().userMngmtClient?.create<EnveuEndpoints>(EnveuEndpoints::class.java)

        val call = endPoint?.getForceFbLogin(params)
        call?.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun changePasswordCall(params: JsonObject,token:String, loginCallBacks: LoginCallBack) {
        val endPoint = NetworkSetup().subscriptionClient(token)?.create<EnveuEndpoints>(EnveuEndpoints::class.java)

        val call = endPoint?.getChangePassword(params)
        call?.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun userProfileCall(loginCallBacks: UserProfileCallBack,token: String) {
        val endPoint = NetworkSetup().subscriptionClient(token)?.create<EnveuEndpoints>(EnveuEndpoints::class.java)

        val call = endPoint.getUserProfile()
        call?.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun userUpdateProfileCall(loginCallBacks: UserProfileCallBack,token: String,name: String) {
        val endPoint = NetworkSetup().subscriptionClient(token)?.create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val requestParam = JsonObject()
        requestParam.addProperty("name", name)

        val call = endPoint?.getUserUpdateProfile(requestParam)
        call?.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }



    fun logoutCall(token: String, loginCallBacks: LogoutCallBack) {
        val endPoint = NetworkSetup().subscriptionClient(token)?.create<EnveuEndpoints>(EnveuEndpoints::class.java)

        val call = endPoint?.getLogout()
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
              loginCallBacks.success(true,response)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { loginCallBacks.failure(false, 0, it) }
                loginCallBacks.failure(false, 0,"")
            }
        })
    }

    fun getContinueWatchingData(token: String,pageNumber:Int,pageSize:Int, getBookmarkCallback: GetContinueWatchingCallback) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint.getAllBookmarks(pageNumber,pageSize)
        call.enqueue(object : Callback<GetContinueWatchingBean> {
            override fun onResponse(call: Call<GetContinueWatchingBean>, response: Response<GetContinueWatchingBean>) {
                getBookmarkCallback.success(true, response)
            }
            override fun onFailure(call: Call<GetContinueWatchingBean>, t: Throwable) {
                getBookmarkCallback.failure(false, 0, "")
            }
        })
    }
    fun getWatchHistory(token: String, pageNumber:Int, pageSize:Int, getWatchHistoryCallBack: GetWatchHistoryCallBack) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint.getWatchHistoryList(pageNumber,pageSize)
        call.enqueue(object : Callback<ResponseWatchHistoryAssetList> {
            override fun onResponse(call: Call<ResponseWatchHistoryAssetList>, response: Response<ResponseWatchHistoryAssetList>) {
                getWatchHistoryCallBack.success(true, response)
            }
            override fun onFailure(call: Call<ResponseWatchHistoryAssetList>, t: Throwable) {
                getWatchHistoryCallBack.failure(false, 0, "")
            }
        })
    }

    fun addToWatchHistory(token: String, assestId: Int, bookmarkingCallback: BookmarkingCallback) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint.addToWatchHistory(assestId)
        call.enqueue(object : Callback<BookmarkingResponse> {
            override fun onResponse(call: Call<BookmarkingResponse>, response: Response<BookmarkingResponse>) {
                bookmarkingCallback.success(true, response)
            }

            override fun onFailure(call: Call<BookmarkingResponse>, t: Throwable) {
                t.message?.let { bookmarkingCallback.failure(false, 0, it) }
                bookmarkingCallback.failure(false, 0, "")
            }
        })
    }

    fun deleteFromWatchHistory(token: String, assetId: Int, bookmarkingCallback: BookmarkingCallback) {
        val endPoint = NetworkSetup().subscriptionClient(token).create<EnveuEndpoints>(EnveuEndpoints::class.java)
        val call = endPoint.deleteFromWatchHistory(assetId)
        call.enqueue(object : Callback<BookmarkingResponse> {
            override fun onResponse(call: Call<BookmarkingResponse>, response: Response<BookmarkingResponse>) {
                bookmarkingCallback.success(true, response)
            }

            override fun onFailure(call: Call<BookmarkingResponse>, t: Throwable) {
                t.message?.let { bookmarkingCallback.failure(false, 0, it) }
                bookmarkingCallback.failure(false, 0, "")
            }
        })
    }
}