package com.sinjidragon.nurijang.remote.service

import com.sinjidragon.nurijang.remote.data.SearchRequest
import com.sinjidragon.nurijang.remote.data.SuggestionsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchService {
    @POST("suggestions")
    suspend fun suggestions(
        @Body request: SearchRequest
    ): SuggestionsResponse
}