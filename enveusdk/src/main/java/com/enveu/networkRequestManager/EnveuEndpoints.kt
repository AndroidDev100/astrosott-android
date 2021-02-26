package com.enveu.networkRequestManager


import com.enveu.Bookmarking.bean.BookmarkingResponse
import com.enveu.Bookmarking.bean.GetBookmarkResponse
import com.enveu.Bookmarking.bean.continuewatching.GetContinueWatchingBean
import com.enveu.UserManagement.bean.LoginResponse.LoginResponseModel
import com.enveu.UserManagement.bean.UserProfile.UserProfileResponse
import com.enveu.WatchHistory.beans.ResponseWatchHistoryAssetList
import com.google.gson.JsonObject
import com.watcho.enveu.bean.EnveuCategory
import retrofit2.Call
import retrofit2.http.*

interface EnveuEndpoints {

    @GET("enveu_prod/screen?")
    fun categoryService(@Header("x-device") device: String, @Header("x-platform") platform: String, @Header("x-api-key") key: String, @Query("screenId") screenId: String): Call<EnveuCategory>

    @Headers("x-platform: android")
    @POST("user/login/manual")
    fun getLogin(@Body apiLogin: JsonObject): Call<LoginResponseModel>

    @Headers("x-platform: android")
    @POST("user/register/manual")
    fun getSignUp(@Body apiSignUp: JsonObject): Call<LoginResponseModel>


    @Headers("x-platform: android")
    @POST("user/login/fb")
    fun getFbLogin(@Body user: JsonObject): Call<LoginResponseModel>

    @Headers("x-platform: android")
    @POST("user/login/fb/force")
    fun getForceFbLogin(@Body user: JsonObject): Call<LoginResponseModel>

    @Headers("x-platform: android")
    @POST("user/logout/false")
    fun getLogout(): Call<JsonObject>

    @Headers("x-platform: android")
    @POST("user/changePassword")
    fun getChangePassword(@Body user: JsonObject): Call<LoginResponseModel>

    @Headers("x-platform: android")
    @POST("content/continueWatching/bookmark/{assetId}/{position}")
    fun bookmarkVideo(@Path("assetId") assetId: Int, @Path("position") position: Int): Call<BookmarkingResponse>

    @Headers("x-platform: android")
    @GET("content/continueWatching/bookmark/{assetId}")
    fun getBookmarkByVideoId(@Path("assetId") videoId: Int): Call<GetBookmarkResponse>

    @Headers("x-platform: android")
    @POST("content/continueWatching/finishedWatching/{assetId}")
    fun finishBookmark(@Path("assetId") assestId: Int): Call<BookmarkingResponse>

    @Headers("x-platform: android")
    @GET("content/continueWatching/bookmarks")
    fun getAllBookmarks(@Query("page") pageNumber: Int, @Query("size") pageSize: Int): Call<GetContinueWatchingBean>

    @Headers("x-platform: android")
    @GET("user/profile")
    fun getUserProfile(): Call<UserProfileResponse>

    @Headers("x-platform: android")
    @POST("user/profile/update")
    fun getUserUpdateProfile(@Body user: JsonObject): Call<UserProfileResponse>

    @Headers("x-platform: android")
    @GET("user/watchHistory/get")
    fun getWatchHistoryList(@Query("page") page: Int, @Query("size") size: Int): Call<ResponseWatchHistoryAssetList>

    @Headers("x-platform: android")
    @POST("user/watchHistory/add/VOD/{assetId}")
    fun addToWatchHistory(@Path("assetId") assestId: Int): Call<BookmarkingResponse>

    @Headers("x-platform: android")
    @DELETE("user/watchHistory/delete/{assetId}")
    fun deleteFromWatchHistory(@Path("assetId") assetId: Int): Call<BookmarkingResponse>

}