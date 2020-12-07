package com.enveu.BaseClient

class BaseConfiguration {
    lateinit var clients: BaseClient
    companion object {
        val instance = BaseConfiguration()
    }

    fun clientSetup(client: BaseClient, type: BaseGateway,baseURL : String, apiKEY : String, deviceTYPE : String, platform: String) {
//        clients=client.init(type,baseURL,apiKEY,deviceTYPE,platform)
    }

    fun clientSetup(client: BaseClient) {
        clients=client
    }
}