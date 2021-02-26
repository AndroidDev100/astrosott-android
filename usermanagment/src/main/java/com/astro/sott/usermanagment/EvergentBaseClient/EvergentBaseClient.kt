package com.astro.sott.usermanagment.EvergentBaseClient

class EvergentBaseClient {

    private var BASE_URL: String? = ""
    private var UDID: String? = ""

    constructor(baseUrl:String){
        BASE_URL=baseUrl;
    }

    fun getBaseUrl(): String? {
        return BASE_URL
    }
}