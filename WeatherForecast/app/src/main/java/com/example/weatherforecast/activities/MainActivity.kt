package com.example.weatherforecast.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecast.MainActivityViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.ui.navdrawer.Drawer
import com.example.weatherforecast.ui.navdrawer.NavDrawerItem
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import com.example.weatherforecast.ui.weather.WeatherScreen
import com.example.weatherforecast.ui.weather.WeatherViewModel
import com.example.weatherforecast.ui.weatherdetail.WeatherDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastTheme {
                MainScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope, scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            Drawer(scope, scaffoldState, navController)
        }
    ) {
        Navigation(navController = navController)
    }
}

@Composable
fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    TopAppBar(
        title = {
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
               ProvideTextStyle(value = MaterialTheme.typography.h6) {
                   Text(
                       text = viewModel.getLocation()?.name ?: "",
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(0.dp, 0.dp, 64.dp, 0.dp),
                       maxLines = 1,
                       textAlign = TextAlign.Center,
                       color = MaterialTheme.colors.primary
                   )
               }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Image(painter = painterResource(id = R.drawable.ic_menu), "Menu" )
            }
        },
        backgroundColor = Color(0, 0, 0, 0),
        contentColor = Color.Black,
        elevation = 1.dp
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavDrawerItem.Home.route) {
        composable(NavDrawerItem.Home.route) {
            WeatherScreen()
        }
        composable(NavDrawerItem.NextSevenDays.route) {
            WeatherDetailScreen()
        }
    }
}