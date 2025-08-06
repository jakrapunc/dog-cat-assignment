package com.work.cnd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.work.base.navigation.Route
import com.work.cat.screen.CatScreen
import com.work.cnd.ui.theme.CatAndDogTheme
import com.work.dog.screen.DogScreen
import com.work.home.ui.HomeScreen
import com.work.profile.screen.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatAndDogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Route.SplashScreen
                    ) {
                        composable<Route.SplashScreen> {
                            SplashScreen(
                                onNavigate = {
                                    navController.navigate(Route.HomeScreen)
                                }
                            )
                        }
                        composable<Route.HomeScreen> {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatAndDogTheme {
        Greeting("Android")
    }
}