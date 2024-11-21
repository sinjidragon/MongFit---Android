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
}