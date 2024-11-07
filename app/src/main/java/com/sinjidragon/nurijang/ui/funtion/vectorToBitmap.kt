package com.sinjidragon.nurijang.ui.funtion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat


fun vectorToBitmap(context: Context, vectorDrawableId: Int, isShadow: Boolean = false): Bitmap {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableId) as VectorDrawable
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    if (isShadow) {
        val paint = Paint().apply {
            color = Color.BLACK
            setShadowLayer(20f, 0f, 0f, Color.argb(64, 0, 0, 0)) // 블러 반경 8, 검정색, 투명도 25% (ARGB 64)
        }
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
    vectorDrawable.draw(canvas)
    return bitmap
}
