package com.sinjidragon.nurijang.remote.data

data class Facility(
    val id: Int,
    val distance: Double,
    val fcltyNm: String,
    val fcltyAddr: String,
    val fcltyDetailAddr: String,
    val rprsntvTelNo: String,
    val mainItemNm: String,
    val fcltyCrdntLo: Double,
    val fcltyCrdntLa: Double
)