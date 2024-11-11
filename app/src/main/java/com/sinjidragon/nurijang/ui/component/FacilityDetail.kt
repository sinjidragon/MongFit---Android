package com.sinjidragon.nurijang.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.ui.theme.gray2
import com.sinjidragon.nurijang.ui.theme.mainColor
import com.sinjidragon.nurijang.ui.theme.pretendard
import com.sinjidragon.nurijang.ui.theme.subColor

@Composable
fun FacilityDetail(
    modifier: Modifier,
    facilityName: String,
    eventName: String,
    tellNumber: String? = null,
    facilityAddress: String,
    facilityDetailAddress: String?,
    onClick: () -> Unit = {},
){
    val address = if (facilityDetailAddress == null){
        facilityAddress
    } else {
        "$facilityAddress $facilityDetailAddress"
    }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            if (facilityName.length <= 20) {
                Row {
                    Text(
                        text = facilityName,
                        fontFamily = pretendard,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = eventName,
                        fontFamily = pretendard,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = gray2
                    )
                }
            }
            else {
                Column {
                    Text(
                        text = facilityName,
                        fontFamily = pretendard,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = eventName,
                        fontFamily = pretendard,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = gray2
                    )
                }
            }
            if (tellNumber != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Image(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.tell_icon),
                        contentDescription = "tell icon"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = tellNumber,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = mainColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = address,
                fontWeight = FontWeight.Light,
                fontFamily = pretendard,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .size(115.dp, 30.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(subColor)
                    .align(Alignment.End)
                    .clickable {onClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "위치로 이동",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.go_icon),
                    contentDescription = ""
                )
            }
        }
    }
@Composable
@Preview(
    showBackground = true
)
fun FacilityDetailPreview(
){
    FacilityDetail(
        modifier = Modifier,
        facilityName = "구지 시민 체육관",
        eventName = "기타 종목",
        tellNumber = "010_23-1230",
        facilityAddress = "대구광역시",
        facilityDetailAddress = "구지면 창리로"
    )
}
