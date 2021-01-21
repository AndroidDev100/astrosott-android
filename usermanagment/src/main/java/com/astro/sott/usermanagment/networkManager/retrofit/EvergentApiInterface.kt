package com.astro.sott.usermanagment.networkManager.retrofit

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EvergentApiInterface {

    @POST("searchAccountV2")
    fun searchAccountV2(@Body jsonObject: JsonObject?): Call<SearchAccountv2Response?>?

}