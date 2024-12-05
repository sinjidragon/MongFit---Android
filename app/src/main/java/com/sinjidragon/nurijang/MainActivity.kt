package com.sinjidragon.nurijang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.sinjidragon.nurijang.ui.nav.NavGraph
import com.sinjidragon.nurijang.ui.theme.NurijangTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NurijangTheme {
                val navHostController = rememberNavController()
                NavGraph(navController = navHostController)
            }
        }
    }
}
