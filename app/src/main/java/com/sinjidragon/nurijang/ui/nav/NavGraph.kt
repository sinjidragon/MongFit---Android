package com.sinjidragon.semtong.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sinjidragon.nurijang.ui.view.MapView
import com.sinjidragon.nurijang.ui.view.SearchView
import com.sinjidragon.nurijang.ui.view.SplashView

@Composable
fun NavGraph(navController: NavHostController) {
    val startDestination = NavGroup.SPLASH

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = NavGroup.SPLASH) {
            SplashView(navController = navController)
        }
        composable(route = NavGroup.MAP) {
            MapView(navController = navController)
        }
        composable(route = NavGroup.SEARCH) {
            SearchView(navController = navController)
        }
    }
}