package com.example.weatherforecast.data.remote.location

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {
    @GET("/geo/1.0/direct?")
    suspend fun getLocation(
        @Query("q") cityName: String,
        @Query("appid") appId: String
    ): Location
}