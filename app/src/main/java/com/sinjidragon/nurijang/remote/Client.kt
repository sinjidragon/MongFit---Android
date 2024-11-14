package com.sinjidragon.nurijang.remote

import com.sinjidragon.nurijang.BuildConfig
import com.sinjidragon.nurijang.remote.service.FacilityService
import com.sinjidragon.nurijang.remote.service.SearchService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit {
        if (retrofit == null) {
            val url = BuildConfig.BASE_URL
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    val facilityService: FacilityService by lazy {
        getClient().create(FacilityService::class.java)
    }
    val searchService: SearchService by lazy {
        getClient().create(SearchService::class.java)
    }
}