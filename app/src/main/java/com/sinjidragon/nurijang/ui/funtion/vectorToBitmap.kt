package com.sinjidragon.nurijang.ui.funtion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Color as AndroidColor

fun vectorToBitmap(
    context: Context,
    vectorResId: Int,
    width: Int,
    height: Int,
    text: String? = null,
    isText: Boolean = true
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, width, height)
    val paint = Paint().apply {
        color = AndroidColor.parseColor("#11235A")
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 17f, context.resources.displayMetrics
        )
        typeface = Typeface.create("pretendard", Typeface.NORMAL)
        textAlign = Paint.Align.CENTER
    }
    val textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent
    val textWidth = if (text != null) paint.measureText(text) else 0f
    val totalWidth = maxOf(width, textWidth.toInt() + 20)
    val totalHeight = if (text != null && isText) {
        height + textHeight.toInt() + 20
    } else {
        height
    }
    val bm = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bm)
    val iconXPos = (totalWidth - width) / 2
    val iconYPos = (totalHeight - height - textHeight.toInt()) / 2
    drawable.setBounds(iconXPos, iconYPos, iconXPos + width, iconYPos + height)
    drawable.draw(canvas)
    if (text != null && isText) {
        val xPos = (canvas.width / 2).toFloat()
        val yPos = iconYPos + height + textHeight / 2
        canvas.drawText(text, xPos, yPos, paint)
    }
    return BitmapDescriptorFactory.fromBitmap(bm)
}






