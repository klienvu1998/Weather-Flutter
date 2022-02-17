package com.example.weatherforecast

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.remote.location.LocationItem
import com.example.weatherforecast.repository.WeatherRepository
import com.example.weatherforecast.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _locationList = MutableLiveData(listOf<LocationItem>())
    val locationList: LiveData<List<LocationItem>> = _locationList

    var isLoading = mutableStateOf(false)

    private var getListLocationJob: Job? = null

    fun getListLocation(location: String) {
        getListLocationJob?.cancel()
        isLoading.value = true
        getListLocationJob = viewModelScope.launch {
            when (val result = repository.getLocation(location)) {
                is Resource.Success -> {
                    if (result.data != null && result.data.size > 0) {
                        _locationList.postValue(listOf())
                        _locationList.postValue(result.data)
                    }
                    isLoading.value = false
                }

                is Resource.Error -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun setLocation(location: LocationItem) {
        repository.setLocation(location)
    }

}