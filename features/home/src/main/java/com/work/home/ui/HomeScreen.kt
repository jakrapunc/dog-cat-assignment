package com.work.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.work.base.navigation.Route
import com.work.cat.screen.CatScreen
import com.work.dog.screen.DogScreen
import com.work.home.model.MenuItem
import com.work.profile.screen.ProfileScreen

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = com.work.design.R.drawable.dog_n_cat),
                    contentDescription = ""
                )

                Text(
                    text = "Dog + Cat & I",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = com.work.design.R.drawable.account),
                    contentDescription = ""
                )
            }
        },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets,
                containerColor = Color.White
            ) {
                MenuItem.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.Companion.size(36.dp),
                                painter = painterResource(id = item.icon),
                                contentDescription = ""
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (selectedDestination == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color.Companion.Blue,
                            indicatorColor = Color.Companion.Transparent
                        )
                    )
                }
            }
        }
    ) {
        HomeNavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Route.DogScreen
        )
    }
}

@Composable
fun HomeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Route.DogScreen> {
            DogScreen()
        }
        composable<Route.CatScreen> {
            CatScreen()
        }
        composable<Route.ProfileScreen> {
            ProfileScreen()
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}