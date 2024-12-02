package com.sinjidragon.nurijang.remote.data

import com.google.android.gms.common.internal.Objects
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

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
) : ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(fcltyCrdntLa,fcltyCrdntLo)
    }

    override fun getTitle(): String {
        return fcltyNm
    }

    override fun getSnippet(): String {
        return "hello"
    }

    override fun getZIndex(): Float {
        return 0f
    }
    override fun hashCode(): Int {
        return Objects.hashCode(1)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Facility

        if (id != other.id) return false
        if (distance != other.distance) return false
        if (fcltyNm != other.fcltyNm) return false
        if (fcltyAddr != other.fcltyAddr) return false
        if (fcltyDetailAddr != other.fcltyDetailAddr) return false
        if (rprsntvTelNo != other.rprsntvTelNo) return false
        if (mainItemNm != other.mainItemNm) return false
        if (fcltyCrdntLo != other.fcltyCrdntLo) return false
        if (fcltyCrdntLa != other.fcltyCrdntLa) return false

        return true
    }
}