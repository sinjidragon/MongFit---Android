package com.sinjidragon.nurijang.remote.service

import com.sinjidragon.nurijang.remote.data.SendMessageRequest
import com.sinjidragon.nurijang.remote.data.StartChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatBotService {
    @POST("chat/start")
    suspend fun startChat():StartChatResponse
    @POST("chat/send")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): String
}