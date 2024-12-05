package com.sinjidragon.nurijang.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.sinjidragon.nurijang.ui.theme.dropShadow
import com.sinjidragon.nurijang.ui.theme.pretendard
import com.sinjidragon.nurijang.ui.theme.subColor

@Composable
fun BaseAlert(
    onDismissRequest: () -> Unit = {},
    onButtonClick: () -> Unit = {},
    titleText: String = "경고",
    contentText: String = "인터넷에 연결되어 있지 않습니다.",
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    )
    {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
        Box(
            modifier = Modifier
                .size(260.dp,160.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .dropShadow()
        ){
            Text(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .align(Alignment.TopCenter),
                text = titleText,
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .align(Alignment.TopCenter),
                text = contentText,
                fontFamily = pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .size(200.dp,35.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(subColor)
                    .clickable { onButtonClick() },
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "확인",
                    fontSize = 14.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}


@Preview
@Composable
fun ALPRV(){
    BaseAlert()
}