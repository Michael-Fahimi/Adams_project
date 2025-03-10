package com.example.adamssproject_geminichatbot

// Import necessary Android, Jetpack Compose, and Material components.
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adamssproject_geminichatbot.ui.theme.Purple80

// The ChatPage composable acts as the container for the whole chat screen.
// It arranges the header, message list, and message input in a vertical column.
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier
    ) {
        // App header displaying the title.
        AppHeader()
        // MessageList displays the chat history and expands to take available space.
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = viewModel.messageList
        )
        // MessageInput allows the user to type and send messages.
        MessageInput(
            onMessageSend = {
                viewModel.sendMessage(it)
            }
        )
    }
}

// MessageList renders the list of chat messages.
// If no messages exist, it shows a prompt with an icon.
@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    if(messageList.isEmpty()){
        // When there are no messages, center a prompt in the available space.
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display an icon from drawable resources.
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.google_gemini_logo_svg),
                contentDescription = "Icon",
                tint = Purple80,
            )
            // Display a friendly prompt text.
            Text(text = "Ask me anything", fontSize = 12.sp)
        }
    } else {
        // When messages are available, display them in a scrollable list.
        LazyColumn(
            modifier = modifier,
            reverseLayout = true // Latest messages appear at the bottom.
        ) {
            // Reverse the message list to maintain the latest message at the bottom.
            items(messageList.reversed()){
                MessageRow(messageModel = it)
            }
        }
    }
}

// MessageRow renders an individual chat message with a "bubble" style.
@Composable
fun MessageRow(messageModel: MessageModel) {
    // Determine if the message comes from the model (AI) or the user.
    val isModel = messageModel.role == "model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Use a full-width container for proper alignment.
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Message bubble with customized padding, alignment, and background.
            Box(
                modifier = Modifier
                    // Align left for model messages, right for user messages.
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    // Apply padding to create spacing between messages.
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    // Clip the message bubble to have rounded corners.
                    .clip(RoundedCornerShape(48f))
                    // Set different background colors for model and user messages.
                    .background(color = if (isModel) Purple80 else Color.Blue)
                    .padding(16.dp) // Internal padding for the text.
            ) {
                // Enable text selection for the message content.
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// MessageInput composable provides a text field for the user to type messages
// and an icon button to send the message.
@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    // Local state to store the current text input.
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // OutlinedTextField where the user enters the message.
        OutlinedTextField(
            modifier = Modifier.weight(1f), // Takes available horizontal space.
            value = message,
            onValueChange = {
                message = it // Update the state with user input.
            }
        )
        // IconButton triggers sending of the message.
        IconButton(onClick = {
            if(message.isNotEmpty()){
                onMessageSend(message) // Invoke the callback with the current message.
                message = "" // Clear the input field after sending.
            }
        }) {
            // Display a send icon.
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send" // Accessibility description.
            )
        }
    }
}

// AppHeader renders the top header of the chat screen.
@Composable
fun AppHeader() {
    // Box container with a background color from the theme.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Header text with padding.
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Adam's  Bot ",
            color = Color.White,
            fontSize = 22.sp
        )
    }
}
