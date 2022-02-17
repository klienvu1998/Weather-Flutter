package com.example.weatherforecast.inject

import com.example.weatherforecast.data.remote.location.GeoCodingApi
import com.example.weatherforecast.data.remote.weather.WeatherApi
import com.example.weatherforecast.repository.WeatherRepository
import com.example.weatherforecast.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideWeatherRepository(api: WeatherApi, geoCodingApi: GeoCodingApi) = WeatherRepository(api = api, geoCodingApi = geoCodingApi)

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLocationApi(): GeoCodingApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(GeoCodingApi::class.java)
    }
}