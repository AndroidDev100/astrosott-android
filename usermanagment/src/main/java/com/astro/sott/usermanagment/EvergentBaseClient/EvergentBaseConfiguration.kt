package com.astro.sott.usermanagment.EvergentBaseClient

class EvergentBaseConfiguration {
    var clients: EvergentBaseClient? = null

    companion object {
        val instance = EvergentBaseConfiguration()
    }

    fun clientSetup(client: EvergentBaseClient) {
        clients = client
    }
}