package com.sinjidragon.nurijang.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.remote.api.suggestions
import com.sinjidragon.nurijang.remote.data.FacilityLite
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.gray
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import kotlinx.coroutines.launch

@Composable
fun SearchView(navController: NavController,mainViewModel: MainViewModel){
    var searchText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val cameraPosition = mainViewModel.cameraPosition.value ?: LatLng(37.532600,127.024612)
    var eventList by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var facilityList by remember {
        mutableStateOf<List<FacilityLite>>(emptyList())
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
                        coroutineScope.launch {
                            val response = suggestions(
                                cameraPosition.longitude,
                                cameraPosition.latitude,
                                text = searchText
                            )
                            if (response != null){
                                eventList = response.mainItems
                                facilityList = response.facilities
                            }
                        }
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
        Column (
            modifier = Modifier
                .offset(y = 70.dp)
        ){
            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ){
                items(eventList) { item ->
                    EventItem(
                        text = item
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ){
                items(facilityList) { item ->
                    FacilityItem(
                        text = item.fcltyNm
                    )
                }
            }
        }
    }
}
@Composable
fun EventItem(
    text: String = "태권도",
    onClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .height(43.dp)
            .clickable { onClick() }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(31.dp, 31.dp)
                    .background(Color(0xFFE8EAED))
            ){
                Image(
                    modifier = Modifier
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.envent_icon),
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .offset(x = 14.dp),
                text = text,
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                color = gray2
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 48.dp)
                .align(Alignment.BottomEnd)
                .height(1.dp)
                .fillMaxWidth()
                .background(gray)
        )
    }
}
@Composable
fun FacilityItem(
    text: String = "구지 체육관",
    onClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .height(43.dp)
            .clickable { onClick() }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(31.dp, 31.dp)
                    .background(Color(0xFFE8EAED))
            ){
                Image(
                    modifier = Modifier
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.facilty_icon),
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .offset(x = 14.dp),
                text = text,
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                color = gray2
            )
        }
        Box(
            modifier = Modifier
                .padding(start = 48.dp)
                .align(Alignment.BottomEnd)
                .height(1.dp)
                .fillMaxWidth()
                .background(gray)
        )
    }
}


