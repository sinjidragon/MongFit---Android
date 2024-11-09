package com.sinjidragon.nurijang.remote.service

import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.GetFacilitiesRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface FacilityService {
    @POST("facilities")
    suspend fun getFacilities(
        @Body request: GetFacilitiesRequest
    ): List<Facility>
}