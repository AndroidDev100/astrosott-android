package com.astro.sott.usermanagment.EvergentBaseClient

class EvergentBaseClient {

    private var UDID: String? = ""

    constructor(baseUrl: String, appVersion: String) {
        BASE_URL = baseUrl
        APP_VERSION = appVersion
    }

    fun getBaseUrl(): String? {
        return BASE_URL
    }
    companion object {
        private var APP_VERSION: String? = null
        private var BASE_URL: String? = ""

        fun getAppVersion(): String? {
            return APP_VERSION
        }
    }

}