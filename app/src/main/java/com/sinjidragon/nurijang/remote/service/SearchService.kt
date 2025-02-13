package com.sinjidragon.nurijang.remote.service

import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.SearchRequest
import com.sinjidragon.nurijang.remote.data.SuggestionsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchService {
    @POST("suggestions")
    suspend fun suggestions(
        @Body request: SearchRequest
    ): SuggestionsResponse
    @POST("search-item")
    suspend fun eventSearch(
        @Body request: SearchRequest
    ): List<Facility>
    @POST("search")
    suspend fun baseSearch(
        @Body request: SearchRequest
    ): List<Facility>
}