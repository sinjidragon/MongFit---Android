package com.sinjidragon.nurijang.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.ui.theme.pretendard
import com.sinjidragon.semtong.nav.NavGroup
import kotlinx.coroutines.delay

@Composable
fun SplashView(navController: NavController){
    LaunchedEffect(Unit)  {
        delay(1500L)
        navController.navigate(NavGroup.MAP)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF171717))
            .systemBarsPadding()
    ){
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 41.dp, y = 120.dp)
        ) {
            Text(
                text = "모든 장애인을 위한",
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "누리장",
                    fontSize = 34.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Image(
                    modifier = Modifier
                        .size(43.dp),
                    painter = painterResource(id = R.drawable.splash_small_image),
                    contentDescription = ""
                )
            }
        }
        Image(
            modifier = Modifier.fillMaxHeight(0.92f),
            painter = painterResource(id = R.drawable.splash_big_image),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}
@Preview
@Composable
fun SplashViewPreview(){
    SplashView(navController = NavController(context = LocalContext.current))
}