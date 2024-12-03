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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.ui.component.FacilityDetail
import com.sinjidragon.nurijang.ui.nav.NavGroup
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.gray
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import kotlinx.coroutines.launch

@Composable
fun SearchView(navController: NavController,viewModel: MainViewModel){
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    viewModel.setCameraPosition(37.532600,127.024612)
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
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
                        .align(Alignment.CenterVertically)
                        .focusRequester(focusRequester),
                    value = uiState.searchText,
                    onValueChange =
                    { newValue ->
                        viewModel.setSearchText(newValue)
                        viewModel.suggestions(
                            uiState.cameraPosition.longitude,
                            uiState.cameraPosition.latitude,
                            newValue
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions {
                        println("clicked")
                        viewModel.setBaseSearchText(uiState.searchText)
                        viewModel.baseSearch(
                            uiState.cameraPosition.longitude,
                            uiState.cameraPosition.latitude,
                            uiState.searchText
                        )
                        viewModel.setIsBaseSearch(true)
                    },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (uiState.searchText.isEmpty()) {
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
                        navController.navigate(NavGroup.CHAT_BOT)
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
                    painter = painterResource(id = R.drawable.bot_icon),
                    contentDescription = ""
                )
            }
        }
        if (uiState.isBaseSearch){
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 80.dp)) {
                item {
                    Text(
                        "\"${uiState.baseSearchText}\" 검색결과",
                        modifier = Modifier.padding(start = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(uiState.facilityList){ item->
                    FacilityDetail(
                        modifier = Modifier,
                        facilityName = item.fcltyNm,
                        eventName = item.mainItemNm,
                        distance = item.distance,
                        tellNumber = item.rprsntvTelNo,
                        facilityAddress = item.fcltyAddr,
                        facilityDetailAddress = item.fcltyDetailAddr,
                        onClick = {
                            viewModel.getFacility(
                                item.id,
                                uiState.cameraPosition.longitude,
                                uiState.cameraPosition.latitude,
                            )
                            navController.popBackStack()
                        },
                        isButton = true
                    )
                }

            }
        }
        else {
            Column(
                modifier = Modifier
                    .offset(y = 70.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(uiState.eventList) { item ->
                        EventItem(
                            text = item,
                            onClick = {
                                viewModel.eventSearch(
                                    uiState.cameraPosition.longitude,
                                    uiState.cameraPosition.latitude,
                                    item
                                )
                                navController.popBackStack()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(uiState.searchFacilityList) { item ->
                        FacilityItem(
                            text = item.fcltyNm,
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.getFacility(
                                        item.id,
                                        uiState.cameraPosition.longitude,
                                        uiState.cameraPosition.latitude,
                                    )
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
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
            .padding(start = 20.dp)
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
            .padding(start = 20.dp)
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


