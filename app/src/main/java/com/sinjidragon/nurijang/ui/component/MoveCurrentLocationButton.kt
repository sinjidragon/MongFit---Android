package com.sinjidragon.nurijang.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sinjidragon.nurijang.R
import com.sinjidragon.nurijang.ui.theme.dropShadow

@Composable
fun MoveCurrentLocationButton(
    modifier: Modifier,
    onClick: () -> Unit = {},
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .clickable {onClick()}
            .size(64.dp)
        ,
    ){
        Image(
            modifier = Modifier
                .dropShadow(shape = RoundedCornerShape(100.dp), blur = 4.dp, offsetY = 2.dp),
            painter = painterResource(id = R.drawable.move_current_location_icon),
            contentDescription = ""
        )
    }
}