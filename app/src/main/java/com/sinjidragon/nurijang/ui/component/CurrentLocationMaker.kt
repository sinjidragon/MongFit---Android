package com.sinjidragon.nurijang.ui.component

import android.content.Context
import android.graphics.Bitmap
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

fun currentVectorToBitmap(context: Context, vectorResId: Int, widthDp: Float, heightDp: Float): BitmapDescriptor? {
    val widthPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, widthDp, context.resources.displayMetrics
    ).toInt()
    val heightPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, heightDp, context.resources.displayMetrics
    ).toInt()

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, widthPx, heightPx)

    val bm = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bm)
}

@Composable
fun CurrentLocationMarker(
    context: Context,
    position: LatLng,
    @DrawableRes iconResourceId: Int
) {
    val icon = currentVectorToBitmap(
        context, iconResourceId,33f,33f
    )
    Marker(
        state = MarkerState(position = position),
        icon = icon
    )
}