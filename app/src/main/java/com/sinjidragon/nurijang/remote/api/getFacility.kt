package com.sinjidragon.nurijang.remote.api

import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.GetDetailRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getFacility(id: Int, lo: Double, la: Double): Facility? {
    return withContext(Dispatchers.IO) {
        try {
            val service = Client.facilityService
            val request = GetDetailRequest(id,lo,la)
            val response = service.getFacility(request)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}