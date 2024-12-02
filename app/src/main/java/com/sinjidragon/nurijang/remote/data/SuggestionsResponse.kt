package com.sinjidragon.nurijang.remote.data

data class SuggestionsResponse (
    val mainItems: List<String>,
    val facilities: List<FacilityLite>
)
data class FacilityLite(
    val id : Int,
    val distance: Double,
    val fcltyNm: String
)