package com.example.weatherforecast.data.remote.location


import com.google.gson.annotations.SerializedName

data class LocalNames(
    @SerializedName("ascii")
    val ascii: String,
    @SerializedName("ca")
    val ca: String,
    @SerializedName("en")
    val en: String,
    @SerializedName("feature_name")
    val featureName: String
)