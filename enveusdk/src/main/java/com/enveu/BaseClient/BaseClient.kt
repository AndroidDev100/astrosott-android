package com.enveu.BaseClient

import com.enveu.Constants.Constants
import com.enveu.Logging.EnveuLogs

class BaseClient {
    private var currentPlatform: BaseGateway? = null
    private var BASE_URL: String? = ""
    private var USER_MNGMT_BASE_URL: String? = ""
    private var API_KEY: String? = ""
    private var OVP_API_KEY: String? = ""
    private var DEVICE_TYPE: String? = ""
    private var PLATFORM: String? = ""
    private var SDK_VERSION: String? = ""
    private var API_VERSION: String? = ""
    private var GATEWAY_TYPE: String? = ""
    private var IS_TAB: Boolean? = false
    private var UDID: String? = ""

    constructor(platform: BaseGateway, isTablet: Boolean) {
        currentPlatform = platform
        IS_TAB = isTablet
        BASE_URL = Constants.BASE_URL
        USER_MNGMT_BASE_URL = Constants.USER_MNGMT_BASE_URL
        API_KEY = if (IS_TAB!!) Constants.API_KEY_TAB else Constants.API_KEY_MOB
        DEVICE_TYPE = if (IS_TAB!!) BaseDeviceType.tablet.name else BaseDeviceType.mobile.name
        OVP_API_KEY = Constants.OVP_API_KEY
        PLATFORM = BasePlatform.android.name
        SDK_VERSION = ""
        API_VERSION = ""
        GATEWAY_TYPE = platform.name
        UDID = ""
    }

    constructor(currentPlatform: BaseGateway, baseURL: String, ovpBaseURL: String,userMngmtApiKey: String,apiKEY: String, deviceTYPE: String, platform: String, isTablet: Boolean, udid: String) {
        when (currentPlatform) {
            BaseGateway.ENVEU -> {
                EnveuLogs.printWarning(currentPlatform.name+" "+baseURL+" "+apiKEY+" "+deviceTYPE+" "+platform+" "+isTablet+" "+udid)
                BASE_URL = baseURL
                API_KEY = apiKEY
                DEVICE_TYPE = deviceTYPE
                PLATFORM = platform
                SDK_VERSION = ""
                API_VERSION = ""
                OVP_API_KEY=userMngmtApiKey
                USER_MNGMT_BASE_URL=ovpBaseURL
                GATEWAY_TYPE = currentPlatform.name
                IS_TAB = isTablet
                UDID = udid
            }
            BaseGateway.KALTURA -> {
                EnveuLogs.printWarning(currentPlatform.name);
            }
            BaseGateway.OVP -> {
                EnveuLogs.printWarning(currentPlatform.name);

            }
            BaseGateway.OTHER -> {
                EnveuLogs.printWarning(currentPlatform.name);
            }
        }
    }

    fun getUdid(): String? {
        return UDID
    }

    fun getBaseUrl(): String? {
        return BASE_URL
    }

    fun getUserMngmntBaseUrl(): String? {
        return USER_MNGMT_BASE_URL
    }

    fun getDeviceType():String?{
        return DEVICE_TYPE
    }
    fun getPlatform():String?{
        return PLATFORM
    }
    fun getApiKey():String?{
        return API_KEY
    }

    fun getOVPApiKey():String?{
        return OVP_API_KEY
    }

    fun getGateway(): String? {
        return GATEWAY_TYPE
    }

    fun getIsTablet(): Boolean? {
       return IS_TAB;
    }

}