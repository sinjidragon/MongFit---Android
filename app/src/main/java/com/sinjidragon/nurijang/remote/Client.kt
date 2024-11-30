package com.sinjidragon.nurijang.remote

import com.sinjidragon.nurijang.BuildConfig
import com.sinjidragon.nurijang.remote.service.ChatBotService
import com.sinjidragon.nurijang.remote.service.FacilityService
import com.sinjidragon.nurijang.remote.service.SearchService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object Client {
    private var retrofit: Retrofit? = null

    private fun getClient(): Retrofit {
        if (retrofit == null) {
            val url = BuildConfig.BASE_URL


            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
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
    val chatBotService: ChatBotService by lazy {
        getClient().create(ChatBotService::class.java)
    }
}