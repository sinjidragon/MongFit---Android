package com.sinjidragon.nurijang.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor(
    private val networkUtil: NetworkUtil,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            if (!networkUtil.isNetworkConnected()) {
                throw NoConnectivityException()
            }

            val request = chain.request()
//            val shouldSkipHeader = request.url.encodedPath.contains("/auth") && !request.url.encodedPath.contains("/auth/me")

//            val newRequest = if (shouldSkipHeader) {
//                request.newBuilder().build()
//            } else {
//                request.newBuilder()
//                    .build()
//            }
            //헤더 뺴야할거 잇으면 주소 넣고 주석 푸셈 글고 이 아랫놈 주석처리 하고
            val newRequest = request.newBuilder()
                .build()

            return chain.proceed(newRequest)
        } catch (e: NoConnectivityException) {
            throw e
        } catch (e: Exception) {
            throw NetworkException(e.message ?: "네트워크 오류가 발생했습니다.")
        }
    }
}

class NetworkUtil(private val context: Context) {
    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val isActiveNetwork =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            isActiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            isActiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}

class NoConnectivityException : IOException("인터넷 연결이 없습니다. 네트워크 상태를 확인해주세요.")
class NetworkException(message: String) : IOException(message)

