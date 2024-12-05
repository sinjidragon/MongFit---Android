package com.sinjidragon.nurijang.remote

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sinjidragon.nurijang.BuildConfig.BASE_URL
import com.sinjidragon.nurijang.NurijangApplication
import com.sinjidragon.nurijang.remote.service.ChatBotService
import com.sinjidragon.nurijang.remote.service.FacilityService
import com.sinjidragon.nurijang.remote.service.SearchService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object Client {
    private var gson: Gson = GsonBuilder().setLenient().create()

    private val interceptorClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(
            RequestInterceptor(
                NetworkUtil(NurijangApplication.getContext())
            )
        )
        .addInterceptor(ResponseInterceptor())
        .build()

    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(interceptorClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }


    val facilityService: FacilityService by lazy { instance.create(FacilityService::class.java) }
    val searchService: SearchService by lazy { instance.create(SearchService::class.java) }
    val chatBotService: ChatBotService by lazy { instance.create(ChatBotService::class.java) }
}

