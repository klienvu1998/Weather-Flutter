package com.example.weatherforecast.repository

import com.example.weatherforecast.data.remote.location.GeoCodingApi
import com.example.weatherforecast.data.remote.location.Location
import com.example.weatherforecast.data.remote.location.LocationItem
import com.example.weatherforecast.data.remote.weather.Weather
import com.example.weatherforecast.data.remote.weather.WeatherApi
import com.example.weatherforecast.utils.Constants
import com.example.weatherforecast.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val geoCodingApi: GeoCodingApi
) {

    private var location: LocationItem? = null

    suspend fun getWeatherInfo(): Resource<Weather> {
        val response = try {
            api.getWeather("${location?.lat}", "${location?.lon}", Constants.API_KEY)
        } catch (e: Exception) {
            return Resource.Error("Unknown Error occurred")
        }
        return Resource.Success(response)
    }

    suspend fun getLocation(city: String): Resource<Location> {
        val response = try {
            geoCodingApi.getLocation(city, Constants.API_KEY)
        } catch (e: Exception) {
            return Resource.Error("Unknown Error occurred")
        }
        return Resource.Success(response)
    }

    fun setLocation(location: LocationItem) {
        this.location = location
    }

    fun getLocation(): LocationItem? {
        return location
    }
}