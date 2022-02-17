package com.example.weatherforecast.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.LandingPageViewModel
import com.example.weatherforecast.data.remote.location.LocationItem
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingPageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastTheme {
                MainLandingScreen()
            }
        }
    }

}

@Composable
fun MainLandingScreen(viewModel: LandingPageViewModel = hiltViewModel()) {
    val location = rememberSaveable { mutableStateOf("") }
    Column {
        SearchLocationList(location, viewModel)
        LocationList(viewModel)
    }
}

@Composable
fun LocationList(viewModel: LandingPageViewModel) {
    val locationList: List<LocationItem> by viewModel.locationList.observeAsState(initial = listOf())
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(locationList) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(context, MainActivity::class.java)
                        viewModel.setLocation(it)
                        context.startActivity(intent)
                    }
            ) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (city, country) = createRefs()
                    Text(
                        text = it.name,
                        modifier = Modifier.constrainAs(city) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                        fontSize = 16.sp,
                    )
                    Text(
                        text = it.country,
                        modifier = Modifier.constrainAs(country) {
                            top.linkTo(city.bottom)
                            start.linkTo(city.start)
                        },
                        fontSize = 13.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun SearchLocationList(textVal: MutableState<String>, viewModel: LandingPageViewModel) {
    TextField(
        value = textVal.value,
        onValueChange = {
            viewModel.getListLocation(textVal.value)
            textVal.value = it
        },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            if (textVal.value != "") {
                IconButton(onClick = { textVal.value = "" }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape
    )
}
