package com.sinjidragon.nurijang.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)
            val statusCode = response.code

            when (statusCode) {
                400 -> {
                    Log.e("API 오류", "잘못된 요청입니다.")
                }
                401, 402 -> {
                    //{todo 여따가 오류낫을때 뭐 할지 넣으면 됨}
                }
                403 -> {
                    Log.e("인터셉터", "권한이 없습니다.")
                }
            }
            return response

        } catch (e: NoConnectivityException) {
            throw e
        } catch (e: Exception) {
            throw NetworkException(e.message ?: "네트워크 오류가 발생했습니다.")
        }
    }
}