package com.sinjidragon.nurijang.ui.component

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.sinjidragon.nurijang.ui.funtion.vectorToBitmap

@Composable
fun MapMarker(
    context: Context,
    position: LatLng,
    title: String,
    @DrawableRes iconResourceId: Int
) {
    val icon = vectorToBitmap(
        context, iconResourceId,160,160, isText = false
    )
    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon
    )
}