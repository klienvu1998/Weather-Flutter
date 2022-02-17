package com.example.weatherforecast

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.data.remote.location.LocationItem
import com.example.weatherforecast.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    fun getLocation(): LocationItem? {
        return repository.getLocation()
    }

}