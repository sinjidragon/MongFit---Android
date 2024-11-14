package com.sinjidragon.nurijang.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.remote.api.suggestions
import com.sinjidragon.nurijang.remote.data.facility
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import kotlinx.coroutines.launch

@Composable
fun SearchView(navController: NavController){
    var searchText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var eventList by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var facilityList by remember {
        mutableStateOf<List<facility>>(emptyList())
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ){
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(44.dp)
                .align(Alignment.TopCenter),
        ){
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .dropShadow()
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.Start,
            ) {
                Spacer(modifier = Modifier.width(11.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(22.dp, 22.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = ""
                )
            }
                Spacer(modifier = Modifier.width(6.dp))
                BasicTextField(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    value = searchText,
                    onValueChange = {
                        searchText = it
                                    },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchText.isEmpty()) {
                                Text(
                                    fontFamily = pretendard,
                                    text = "시설명 종목",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = gray2
                                )
                            }
                            innerTextField()
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clickable {
                        TODO("일단 보류기능")
                    }
                    .clip(RoundedCornerShape(8.dp))
                    .dropShadow()
                    .background(mainColor)
                    .width(40.dp)
                    .fillMaxHeight(),
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.mike_icon),
                    contentDescription = ""
                )
            }
        }
    }
}
@Preview
@Composable
fun SearchViewPreview(){
    SearchView(navController = NavController(LocalContext.current))
}
