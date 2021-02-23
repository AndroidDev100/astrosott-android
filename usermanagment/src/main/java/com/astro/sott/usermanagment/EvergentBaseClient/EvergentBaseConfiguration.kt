package com.astro.sott.usermanagment.EvergentBaseClient

class EvergentBaseConfiguration {
    lateinit var clients: EvergentBaseClient

    companion object {
        val instance = EvergentBaseConfiguration()
    }

    fun clientSetup(client: EvergentBaseClient) {
        clients = client
    }
}