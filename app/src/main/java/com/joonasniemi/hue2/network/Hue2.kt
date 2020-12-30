package com.joonasniemi.hue2.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val UPNP = "https://discovery.meethue.com/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory
    .create(moshi)).baseUrl(UPNP).build()

interface Hue2Service {
    @GET(".")
    suspend fun getBridges(): Set<HueServer>

    @POST("http://{ip}/api")
    suspend fun registerApp(@Path("ip") ip: String, @Body device: RegisterBody): Response<*>
}

object Hue2 {
    val retrofitService : Hue2Service by lazy {
        retrofit.create(Hue2Service::class.java)
    }
}

data class HueServer(
    val id: String,
    @Json(name = "internalipaddress") val ip: String
)

data class RegisterBody(val devicetype: String)