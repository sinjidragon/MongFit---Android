package com.sinjidragon.nurijang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sinjidragon.nurijang.ui.theme.NurijangTheme
import com.sinjidragon.nurijang.ui.view.MapView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NurijangTheme {
                MapView()
            }
        }
    }
}
