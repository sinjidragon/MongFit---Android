package com.sinjidragon.nurijang.remote.service

import com.sinjidragon.nurijang.remote.data.Facility
import com.sinjidragon.nurijang.remote.data.GetDetailRequest
import com.sinjidragon.nurijang.remote.data.GetFacilitiesRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface FacilityService {
    @POST("facilities")
    suspend fun getFacilities(
        @Body request: GetFacilitiesRequest
    ): List<Facility>
    @POST("facility-detail")
    suspend fun getFacility(
        @Body request: GetDetailRequest
    ): Facility
}