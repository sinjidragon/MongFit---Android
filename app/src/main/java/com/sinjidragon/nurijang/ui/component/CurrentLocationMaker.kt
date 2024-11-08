package com.sinjidragon.nurijang.ui.component

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

fun currentVectorToBitmap(context: Context, vectorResId: Int, width: Int, height: Int): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, width, height) // 벡터 이미지의 크기를 적절히 설정
    val bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
@Composable
fun CurrentLocationMarker(
    context: Context,
    position: LatLng,
    title: String,
    @DrawableRes iconResourceId: Int
) {
    val icon = currentVectorToBitmap(
        context, iconResourceId,160,160
    )
    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon
    )
}