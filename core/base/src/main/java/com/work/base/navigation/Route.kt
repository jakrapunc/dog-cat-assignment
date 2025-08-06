package com.work.base.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object SplashScreen : Route

    @Serializable
    data object HomeScreen : Route

    @Serializable
    data object ProfileScreen : Route

    @Serializable
    data object CatScreen : Route

    @Serializable
    data object DogScreen : Route
}