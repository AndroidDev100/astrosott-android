package com.astro.sott.usermanagment.EvergentServices

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.astro.sott.usermanagment.callBacks.EvergentSearchAccountCallBack
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response
import com.astro.sott.usermanagment.networkManager.retrofit.EvergentApiInterface
import com.astro.sott.usermanagment.networkManager.retrofit.EvergentNetworkClass
import com.astro.sott.usermanagment.utils.EvergentErrorHandling
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EvergentServices {

    companion object {
        val instance = EvergentServices()
    }


    fun searchAccountv2(context: Context,searchAccountCallBack: EvergentSearchAccountCallBack) {

        var searchAccountJson = JsonObject()
        var json = JsonObject()
        json.addProperty("channelPartnerID", "ASTRO")
        json.addProperty("apiUser", "astrosottapiuser")
        json.addProperty("apiPassword", "Gfty5$" + "dfr&")
        json.addProperty("userName", "agarwalshalabh@gmail.com")

        searchAccountJson.add("SearchAccountV2RequestMessage", json)
        val apiInterface = EvergentNetworkClass().client?.create(EvergentApiInterface::class.java)
        val call = apiInterface?.searchAccountV2(searchAccountJson)
        call?.enqueue(object : Callback<SearchAccountv2Response?> {
            override fun onFailure(call: Call<SearchAccountv2Response?>, t: Throwable) {
                searchAccountCallBack.onFailure("Something Went Wrong")

            }

            override fun onResponse(call: Call<SearchAccountv2Response?>, response: Response<SearchAccountv2Response?>) {
                if (response.body() != null && response.body()?.searchAccountV2ResponseMessage != null && response.body()?.searchAccountV2ResponseMessage?.responseCode != null) {

                    if (response.body()?.searchAccountV2ResponseMessage?.responseCode.equals("1", true)) {
                        searchAccountCallBack.onSuccess(response.body()!!);
                    } else {
                        if (response.body()?.searchAccountV2ResponseMessage?.failureMessage != null) {
                            searchAccountCallBack.onFailure(EvergentErrorHandling().getErrorMessage(response.body()?.searchAccountV2ResponseMessage?.failureMessage,context))
                        } else {
                            searchAccountCallBack.onFailure("Something Went Wrong")
                        }
                    }

                }
            }
        }

        )


    }


}