package com.sinjidragon.nurijang.ui.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import android.graphics.Color as AndroidColor

fun placeVectorToBitmap(
    context: Context,
    vectorResId: Int,
    widthDp: Float,
    heightDp: Float,
    text: String? = null
): BitmapDescriptor? {
    val widthPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, widthDp, context.resources.displayMetrics
    ).toInt()
    val heightPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, heightDp, context.resources.displayMetrics
    ).toInt()

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, widthPx, heightPx)

    val paint = Paint().apply {
        color = AndroidColor.parseColor("#11235A")
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 14f, context.resources.displayMetrics
        )
        typeface = Typeface.create("pretendard", Typeface.NORMAL)
        textAlign = Paint.Align.CENTER
    }

    val textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent
    val textWidth = if (text != null) paint.measureText(text) else 0f

    val totalWidthPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, maxOf(widthDp, (textWidth / context.resources.displayMetrics.density) + 20), context.resources.displayMetrics
    ).toInt()
    val totalHeightPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, if (text != null) heightDp + (textHeight / context.resources.displayMetrics.density) + 20 else heightDp, context.resources.displayMetrics
    ).toInt()

    val bm = Bitmap.createBitmap(totalWidthPx, totalHeightPx, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bm)

    val iconXPos = (totalWidthPx - widthPx) / 2
    val iconYPos = (totalHeightPx - heightPx - textHeight.toInt()) / 2
    drawable.setBounds(iconXPos, iconYPos, iconXPos + widthPx, iconYPos + heightPx)
    drawable.draw(canvas)

    if (text != null) {
        val xPos = (canvas.width / 2).toFloat()
        val yPos = iconYPos + heightPx + textHeight / 2 + 20
        canvas.drawText(text, xPos, yPos, paint)
    }

    return BitmapDescriptorFactory.fromBitmap(bm)
}



@Composable
fun PlaceMaker(
    context: Context,
    position: LatLng,
    text: String,
    onClick: () -> Unit = {},
    @DrawableRes iconResourceId: Int){
    val icon = placeVectorToBitmap(
        context, iconResourceId,22f,30f,text
    )
    Marker(
        state = MarkerState(position = position),
        icon = icon,
        onClick = {
            onClick() // 클릭 이벤트 호출
            true // 이벤트 소비
        }
    )
}
