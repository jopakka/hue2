package com.joonasniemi.hue2.network

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Response

class Hue2Repository {
    suspend fun getBridges(): Set<HueServer> = Hue2.retrofitService.getBridges()
    suspend fun registerApp(ip: String, name: String): Response<*> =
            Hue2.retrofitService.registerApp(ip, RegisterBody(name))

}