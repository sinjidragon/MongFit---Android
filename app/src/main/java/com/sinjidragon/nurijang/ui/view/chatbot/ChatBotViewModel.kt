package com.sinjidragon.nurijang.ui.view.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.data.GetFacilitiesRequest
import com.sinjidragon.nurijang.ui.view.MainSideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class ChatBotState(
    val chats : List<String> = emptyList(),
    val message : String = "",
    val threadId : String = ""
)

sealed interface ChatBotSideEffect {
    data object Failed : ChatBotSideEffect
}
class ChatBotViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ChatBotState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ChatBotSideEffect>()
    val uiEffect: SharedFlow<ChatBotSideEffect> = _uiEffect.asSharedFlow()

    fun updateChats(chats: List<String>) {
        _uiState.update { it.copy(chats = chats) }
    }

    fun updateMessage(message: String) {
        _uiState.update { it.copy(message = message) }
    }
    fun updateThreadId(threadId: String) {
        _uiState.update { it.copy(threadId = threadId) }
    }
    fun startChatBot() {
        viewModelScope.launch {
            try {
                val facilityService = Client.chatBotService
                val response = facilityService.startChat()
                updateThreadId(response.id)
                println(uiState.value.threadId)
            } catch (e: HttpException) {
                _uiEffect.emit(ChatBotSideEffect.Failed)
            }
        }
    }
}