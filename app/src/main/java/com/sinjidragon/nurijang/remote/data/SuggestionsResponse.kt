package com.sinjidragon.nurijang.remote.data

import androidx.compose.foundation.pager.PagerSnapDistance

data class SuggestionsResponse (
    val mainItems: List<String>,
    val facilities: List<FacilityLite>
)
data class FacilityLite(
    val id : Int,
    val distance: Double,
    val fcltyNm: String
)