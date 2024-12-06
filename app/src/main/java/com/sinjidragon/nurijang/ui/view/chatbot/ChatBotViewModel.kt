package com.sinjidragon.nurijang.ui.view.chatbot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.sinjidragon.nurijang.remote.Client
import com.sinjidragon.nurijang.remote.NoConnectivityException
import com.sinjidragon.nurijang.remote.data.BotMessage
import com.sinjidragon.nurijang.remote.data.SendMessageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatBotState(
    val messages : List<MessageItem> = emptyList(),
    val message : String = "",
    val threadId : String = "",
    val isLaunch : Boolean = false,
    val isError: Boolean = false,
    val errorText: String = ""
)

sealed class MessageItem {
    data class MyMessageItem(val myMessage : String) : MessageItem()
    data class BotMessageItem(val botMessage : String) : MessageItem()
    data class RecommandMessageItem(val recommandMessage : List<String>) : MessageItem()
}

sealed interface ChatBotSideEffect {
    data object Failed : ChatBotSideEffect
}
class ChatBotViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ChatBotState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ChatBotSideEffect>()
    val uiEffect: SharedFlow<ChatBotSideEffect> = _uiEffect.asSharedFlow()

    fun addMessage(message: MessageItem) {
        _uiState.update { it.copy(messages = uiState.value.messages + message) }
    }

    fun updateMessage(message: String) {
        _uiState.update { it.copy(message = message) }
    }
    fun updateThreadId(threadId: String) {
        _uiState.update { it.copy(threadId = threadId) }
    }
    fun updateIsLaunch(isLaunch: Boolean) {
        _uiState.update { it.copy(isLaunch = isLaunch) }
    }
    fun changeLastMessage(item : MessageItem) {
        _uiState.update { currentState ->
            val updatedMessages = currentState.messages.toMutableList()
            if (updatedMessages.isNotEmpty()) {
                updatedMessages[updatedMessages.lastIndex] = item
            }
            currentState.copy(messages = updatedMessages)
        }
    }
    fun setIsError(isError: Boolean) {
        _uiState.update { it.copy(isError = isError) }
    }
    fun setErrorText(errorText: String) {
        _uiState.update { it.copy(errorText = errorText) }
    }
    fun startChatBot() {
        viewModelScope.launch {
            try {
                val chatBotService = Client.chatBotService
                val response = chatBotService.startChat()
                updateThreadId(response.id)
                println(uiState.value.threadId)
            }
            catch (e: NoConnectivityException){
                setErrorText("네트워크 연결을 확인해 주세요")
                Log.d("jalbwa","오류")
                _uiEffect.emit(ChatBotSideEffect.Failed)
            }catch (e: Exception) {
                setErrorText("서버와의 통신과정에서 오류가 발생하였습니다.")
                _uiEffect.emit(ChatBotSideEffect.Failed)
            }
        }
    }
    fun sendMessage(message: String) {
        if (uiState.value.messages.isNotEmpty() && uiState.value.messages.last() is MessageItem.RecommandMessageItem) {
            changeLastMessage(MessageItem.MyMessageItem(message))
        }
        else {
            addMessage(MessageItem.MyMessageItem(message))
        }
        updateIsLaunch(true)
        viewModelScope.launch {
            delay(500)
            addMessage(MessageItem.BotMessageItem("응답을 생성중입니다.  "))
            while (uiState.value.isLaunch){
                delay(1000)
                if (uiState.value.isLaunch) {
                    changeLastMessage(MessageItem.BotMessageItem("응답을 생성중입니다.. "))
                }
                delay(1000)
                if (uiState.value.isLaunch) {
                    changeLastMessage(MessageItem.BotMessageItem("응답을 생성중입니다..."))
                }
                delay(1000)
                if (uiState.value.isLaunch) {
                    changeLastMessage(MessageItem.BotMessageItem("응답을 생성중입니다.  "))
                }
            }
        }
        viewModelScope.launch {
            try{
                val chatBotService = Client.chatBotService
                val threadId = uiState.value.threadId
                val request = SendMessageRequest(threadId,message)
                val response = chatBotService.sendMessage(request)
                updateIsLaunch(false)
                delay(1000)
                println(response)
                try {
                    val madeMessage = response.replace("```json", "").replace("```", "").trimIndent()
                    println(madeMessage)
                    val gson = Gson()
                    val jsonMessage = gson.fromJson(madeMessage,BotMessage::class.java)
                    val botMessage = jsonMessage.answer.joinToString(separator = "\n")
                    changeLastMessage(MessageItem.BotMessageItem(botMessage))
                    addMessage(MessageItem.RecommandMessageItem(jsonMessage.recommand))
                }catch(e: JsonSyntaxException) {
                    changeLastMessage(MessageItem.BotMessageItem(response))
                }
            } catch (e:NoConnectivityException){
                setErrorText("네트워크 연결을 확인해 주세요")
                Log.d("jalbwa","오류")
                _uiEffect.emit(ChatBotSideEffect.Failed)
            } catch (e: Exception) {
                setErrorText("서버와의 통신과정에서 오류가 발생하였습니다.")
                _uiEffect.emit(ChatBotSideEffect.Failed)
            }
        }
    }
}