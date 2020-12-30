package com.joonasniemi.hue2.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val UPNP = "https://discovery.meethue.com/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(UPNP).build()

interface Hue2UpnpService {
    @GET(".")
    suspend fun getBridges(): Set<HueServer>
}

object Hue2Upnp {
    val retrofitService : Hue2UpnpService by lazy {
        retrofit.create(Hue2UpnpService::class.java)
    }
}

data class HueServer(
    val id: String,
    @Json(name = "internalipaddress") val ip: String
)