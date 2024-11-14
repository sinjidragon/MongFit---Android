package com.sinjidragon.nurijang.remote.api

import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.SearchRequest
import com.sinjidragon.nurijang.remote.data.SuggestionsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun suggestions(lo: Double, la: Double, text: String): SuggestionsResponse? {
    return withContext(Dispatchers.IO) {
        try {
            val service = Client.searchService
            val request = SearchRequest(lo,la,text)
            val response = service.suggestions(request)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}