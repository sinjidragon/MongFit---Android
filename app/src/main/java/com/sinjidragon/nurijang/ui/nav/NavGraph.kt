package com.sinjidragon.nurijang.ui.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sinjidragon.nurijang.ui.view.MainViewModel
import com.sinjidragon.nurijang.ui.view.MapView
import com.sinjidragon.nurijang.ui.view.SearchView
import com.sinjidragon.nurijang.ui.view.SplashView
import com.sinjidragon.nurijang.ui.view.chatbot.ChatBotView

@Composable
fun NavGraph(navController: NavHostController,mainViewModel: MainViewModel = hiltViewModel()) {
    val startDestination = NavGroup.SPLASH

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = NavGroup.SPLASH) {
            SplashView(navController = navController)
        }
        composable(route = NavGroup.MAP) {
            MapView(navController = navController,mainViewModel)
        }
        composable(route = NavGroup.SEARCH) {
            SearchView(navController = navController,mainViewModel)
        }
        composable(route = NavGroup.CHAT_BOT) {
            ChatBotView(navController = navController)
        }
    }
}