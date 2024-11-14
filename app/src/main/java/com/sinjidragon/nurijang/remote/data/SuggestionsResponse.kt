package com.sinjidragon.nurijang.remote.data

import androidx.compose.foundation.pager.PagerSnapDistance

data class SuggestionsResponse (
    val mainItems: List<String>,
    val facilities: List<facility>
)
data class facility(
    val id : Int,
    val distance: Double,
    val fcltyNm: String
)