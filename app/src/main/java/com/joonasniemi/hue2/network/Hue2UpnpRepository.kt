package com.joonasniemi.hue2.network

class Hue2UpnpRepository {
    suspend fun getBridges(): Set<HueServer> {
        return Hue2Upnp.retrofitService.getBridges()
    }
}