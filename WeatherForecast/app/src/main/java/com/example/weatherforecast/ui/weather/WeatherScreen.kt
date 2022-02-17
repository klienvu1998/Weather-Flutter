package com.example.weatherforecast.ui.weather

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.ui.theme.Pink
import com.example.weatherforecast.ui.theme.Purple
import com.example.weatherforecast.utils.getImageFromUrl
import com.example.weatherforecast.utils.getUVIndexColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val scrollStateScreen = rememberScrollState()
    val isLoading = remember { viewModel.isLoading }
    val loadError = remember { viewModel.loadError }
    val configuration = LocalConfiguration.current
    if (!isLoading.value && loadError.value.isEmpty()) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .verticalScroll(scrollStateScreen)
                ) {
                    TodaysDateBox()
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CurrentWeatherBox()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        ) {
                            Text(
                                text = "Later today",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Start
                            )
                            HourlyWeatherList()
                        }
                    }
                }
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .verticalScroll(scrollStateScreen)
                ) {
                    TodaysDateBox()
                    CurrentWeatherBox()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Later today",
                            fontSize = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                    HourlyWeatherList()
                }
            }
        }
    } else if (loadError.value.isNotEmpty()) {
        RetrySection(error = loadError.value) {
            viewModel.loadCurrentWeatherData()
            viewModel.loadHourlyWeatherData()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Color.Cyan)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyWeatherList(viewModel: WeatherViewModel = hiltViewModel()) {
    val hourlyWeatherList = remember { viewModel.hourlyWeatherList }
    val isLoading = remember { viewModel.isLoading }

    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(hourlyWeatherList.value) {
            if (!isLoading.value) {
                HourlyWeatherBox(
                    time = it.time,
                    imgUrl = it.imgUrl,
                    temp = it.temp,
                    tempHigh = it.highTemp,
                    tempLow = it.lowTemp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodaysDateBox(viewModel: WeatherViewModel = hiltViewModel()) {
    val currentDate = remember { viewModel.currentDate }
    val isLoading = remember { viewModel.isLoading }
    val loadingError = remember { viewModel.loadError }
    if (!isLoading.value) {
        Box(
            modifier = Modifier.fillMaxWidth(0.5f),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material.Surface(
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentDate.value,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 19.sp
                    )
                }
            }
        }
    } else {

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentWeatherBox(viewModel: WeatherViewModel = hiltViewModel()) {
    val currentTemp = remember { viewModel.currentTemp }
    val currentWeatherType = remember { viewModel.currentWeatherType }
    val currentHumidity = remember { viewModel.currentHumidity }
    val currentUV = remember { viewModel.currentUV }
    val currentImgUrl = remember { viewModel.currentImgUrl }
    val isLoading = remember { viewModel.isLoading }
    val uvIndexColor = getUVIndexColor(currentUV.value)

    if (!isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(16.dp, 4.dp, 16.dp, 16.dp)
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Purple)
                .padding(4.dp)) {
                Column {
                    Text(
                        text = currentTemp.value,
                        color = Color.White,
                        fontSize = 72.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                    Text(
                        text = currentWeatherType.value,
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingValues(0.dp, 0.dp, 24.dp, 8.dp))
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.weatherforecast.R.drawable.ic_humidity),
                                    contentDescription = "Description"
                                )
                                Text(
                                    text = "Humidity ${currentHumidity.value}",
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(6.dp, 12.dp),
                                    fontSize = 18.sp
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.weatherforecast.R.drawable.ic_wind),
                                    contentDescription = "Description"
                                )
                                Text(
                                    text = "UV ${currentUV.value}",
                                    color = uvIndexColor,
                                    modifier = Modifier
                                        .padding(6.dp, 12.dp),
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Image(
                            painter = painterResource(id = getImageFromUrl(currentImgUrl.value)),
                            contentDescription = "Description",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HourlyWeatherBox(
    time: String,
    imgUrl: String,
    temp: String,
    tempHigh: String,
    tempLow: String
) {
    Box(modifier = Modifier.padding(8.dp, 4.dp).fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Pink)
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = time,
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(0.dp, 8.dp, 0.dp, 8.dp))
                )

                Image(
                    painter = painterResource(id = getImageFromUrl(imgUrl)),
                    contentDescription = "Description",
                    modifier = Modifier.size(96.dp)
                )

                Text(
                    text = temp,
                    color = Color.White,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(0.dp, 0.dp, 0.dp, 8.dp))
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp, 0.dp)
                    ) {
                        Icon(
                            Icons.Outlined.ExpandMore,
                            contentDescription = "Description",
                            modifier = Modifier.size(40.dp),
                            Color.White
                        )
                        Text(
                            text = tempLow,
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(PaddingValues(0.dp, 0.dp, 0.dp, 8.dp))
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp, 0.dp)
                    ) {
                        Icon(
                            Icons.Filled.ExpandLess,
                            contentDescription = "Description",
                            modifier = Modifier.size(40.dp),
                            Color.White
                        )
                        Text(
                            text = tempHigh,
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(PaddingValues(0.dp, 0.dp, 0.dp, 8.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = { onRetry() }) {
            Text(text = "Retry", color = Color.White)
        }
    }
}