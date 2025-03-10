package com.example.adamssproject_geminichatbot

// Import necessary Android and Jetpack Compose libraries
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

// ChatViewModel is responsible for managing the chat state and interactions with the generative AI model.
class ChatViewModel : ViewModel() {

    // Lazily initialize a mutable state list to hold chat messages.
    // This list is observed by Compose to update the UI when messages change.
    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    // Initialize the generative AI model with a specific model name and API key.
    // SECURITY: Hardcoding API keys in source code is insecure. In production, use secure storage methods (e.g., BuildConfig or local.properties).
    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", // The model name for the generative AI service.
        apiKey = "AIzaSyDOeFgkiRKQ00xPTOS_EOSsvpA9obzWZRc" // ⚠️ WARNING: Exposed API key. Replace with secure handling.
    )

    // The sendMessage function sends a user's question to the generative model and processes the response.
    // The @RequiresApi annotation ensures that the function is only called on devices that meet the minimum API level.
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun sendMessage(question: String) {
        // Launch a coroutine tied to the ViewModel's scope for asynchronous work.
        viewModelScope.launch {
            try {
                // Start a chat session with the generative model by sending the history of chat messages.
                // Each message in the history is converted into the required content format (with role and text).
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                // Add the user's question to the chat history.
                messageList.add(MessageModel(question, "user"))
                // Add a temporary "Typing..." indicator to show that a response is being generated.
                messageList.add(MessageModel("Typing....", "model"))

                // Send the user's question to the model and await its response.
                val response = chat.sendMessage(question)
                // Remove the temporary typing indicator.
                messageList.removeLast()
                // Add the model's response to the chat history.
                messageList.add(MessageModel(response.text.toString(), "model"))
            } catch (e: Exception) {
                // In case of an error, remove the temporary message (if still present) and display an error message.
                messageList.removeLast()
                messageList.add(MessageModel("Error : " + e.message.toString(), "model"))
                // Log the exception to help with debugging.
                Log.e("ChatViewModel", "Error sending message", e)
            }
        }
    }
}
