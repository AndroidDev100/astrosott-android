package com.enveu.BaseClient

class BaseConfiguration {
    var clients: BaseClient? = null

    companion object {
        val instance = BaseConfiguration()
    }

    fun clientSetup(client: BaseClient, type: BaseGateway, baseURL: String, apiKEY: String, deviceTYPE: String, platform: String) {
//        clients=client.init(type,baseURL,apiKEY,deviceTYPE,platform)
    }

    fun clientSetup(client: BaseClient) {
        clients = client
    }
}