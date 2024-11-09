package com.sinjidragon.nurijang.remote.api

import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.GetFacilitiesRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getFacilities(fcltyCrdntLo: Double, fcltyCrdntLa: Double): List<Facility>? {
    return withContext(Dispatchers.IO) {
        try {
            val service = Client.facilityService
            val request = GetFacilitiesRequest(fcltyCrdntLo,fcltyCrdntLa)
            val response = service.getFacilities(request)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}