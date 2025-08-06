package com.work.home.model

import com.work.base.navigation.Route

enum class MenuItem(
    val route: Route,
    val label: String,
    val icon: Int,
) {
    DOG(Route.DogScreen, "Dogs", com.work.design.R.drawable.dog),
    CAT(Route.CatScreen, "Cats", com.work.design.R.drawable.cat),
    ME(Route.ProfileScreen, "Me", com.work.design.R.drawable.profile)
}