package com.sinjidragon.nurijang.ui.funtion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


fun vectorToBitmap(context: Context, vectorDrawableId: Int): Bitmap {
    // 벡터 드로어블 로드
    val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableId) as VectorDrawable

    // 비트맵 생성
    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // 벡터 이미지를 캔버스에 그리기
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)

    return bitmap
}