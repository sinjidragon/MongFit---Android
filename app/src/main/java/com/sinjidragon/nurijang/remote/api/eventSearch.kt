package com.sinjidragon.nurijang.remote.api

import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getFacility(lo: Double, la: Double, text: String): List<Facility>? {
    return withContext(Dispatchers.IO) {
        try {
            val service = Client.searchService
            val request = SearchRequest(lo,la,text)
            val response = service.eventSearch(request)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}