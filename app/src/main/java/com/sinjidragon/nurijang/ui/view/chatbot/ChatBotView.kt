package com.sinjidragon.nurijang.ui.view.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.ui.theme.gray
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.pretendard

@Composable
fun ChatBotView(
    navController: NavController,
    viewModel: ChatBotViewModel = viewModel()
){
    val uiState by viewModel.uiState. collectAsState()


    LaunchedEffect(Unit) {
        viewModel.startChatBot()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
        ){
            Row (
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x =18.dp)
                    .clickable { navController.popBackStack() }
            ){
                Image(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.Black)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "나가기",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontFamily = pretendard,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
            Text(
                "AI 누리",
                modifier = Modifier.align(Alignment.Center),
                fontFamily = pretendard,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(gray))
        LazyColumn (
            modifier = Modifier.fillMaxWidth().weight(1f)
        ){

        }
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(gray))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(83.dp)
        ){
            Row(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                BasicTextField(
                    modifier = Modifier
                        .padding(start = 22.dp)
                        .height(40.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(gray),
                    value = uiState.message,
                    textStyle = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    onValueChange = viewModel::updateMessage,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            if (uiState.message.isEmpty()) {
                                Text(
                                    text = "AI 누리에게 질문하기",
                                    fontFamily = pretendard,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = gray2
                                )
                            }
                            innerTextField()
                        }
                    },
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 22.dp),
                    painter = painterResource(R.drawable.send_icon),
                    contentDescription = ""
                )
            }
        }
    }
}
@Preview
@Composable
fun CBPV(){
    ChatBotView(navController = NavController(LocalContext.current))
}



