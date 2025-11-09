package com.kaaneneskpc.f1setupinstructor.feature.chatbot

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import com.kaaneneskpc.f1setupinstructor.domain.model.ChatMessage
import com.kaaneneskpc.f1setupinstructor.domain.model.Role

@Composable
fun ChatRoute(
    onBack: () -> Unit = {},
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                ChatEvent.OnBack -> onBack()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@Composable
fun ChatScreen(
    uiState: ChatUiState,
    onEvent: (ChatEvent) -> Unit
) {
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size)
        }
    }

    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            ChatTopBar(
                title = uiState.title,
                onBackClick = { onEvent(ChatEvent.OnBack) }
            )

            // Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Group messages for avatar display
                val groupedMessages = groupMessages(uiState.messages)

                groupedMessages.forEachIndexed { groupIndex, group ->
                    items(group.messages.size) { index ->
                        val message = group.messages[index]
                        val isFirst = index == 0
                        val isLast = index == group.messages.size - 1

                        when (message.role) {
                            Role.AI -> AiBubble(
                                message = message,
                                showAvatar = isFirst,
                                showLabel = groupIndex == 0 && isFirst,
                                onLongPress = { onEvent(ChatEvent.OnMessageLongPress(message.id)) }
                            )

                            Role.USER -> UserBubble(
                                message = message,
                                showAvatar = isLast,
                                onLongPress = { onEvent(ChatEvent.OnMessageLongPress(message.id)) }
                            )
                        }
                    }
                }

                // Typing indicator
                if (uiState.isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Bottom composer
            ChatComposer(
                input = uiState.input,
                suggestions = uiState.suggestions,
                canSend = uiState.canSend,
                onInputChange = { onEvent(ChatEvent.OnInputChange(it)) },
                onSuggestionClick = { onEvent(ChatEvent.OnSuggestionClick(it)) },
                onAttachClick = { onEvent(ChatEvent.OnAttachClick) },
                onSendClick = { onEvent(ChatEvent.OnSendClick) }
            )
        }
    }
}

@Composable
fun ChatTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.semantics {
                    contentDescription = "Navigate back"
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Placeholder for symmetry
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AiBubble(
    message: ChatMessage,
    showAvatar: Boolean,
    showLabel: Boolean,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Label (only for first AI message)
        if (showLabel) {
            Text(
                text = "AI Danışman",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = if (showAvatar) 52.dp else 12.dp, bottom = 4.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.Start
        ) {
            // Avatar
            if (showAvatar) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://api.dicebear.com/7.x/avataaars/png?seed=ai")
                        .crossfade(true)
                        .build(),
                    contentDescription = "AI avatar",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Message bubble
            Surface(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = onLongPress
                    ),
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
                color = Color.DarkGray.copy(alpha = 0.3f),
                tonalElevation = 2.dp
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserBubble(
    message: ChatMessage,
    showAvatar: Boolean,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.End
        ) {
            // Message bubble
            Surface(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {},
                        onLongClick = onLongPress
                    ),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 4.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
                color = Color.Red,
                tonalElevation = 2.dp
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Avatar
            if (showAvatar) {
                Spacer(modifier = Modifier.width(12.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://api.dicebear.com/7.x/avataaars/png?seed=user")
                        .crossfade(true)
                        .build(),
                    contentDescription = "User avatar",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3))
                )
            }
        }
    }
}

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(0.85f),
        horizontalArrangement = Arrangement.Start
    ) {
        // AI Avatar
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://api.dicebear.com/7.x/avataaars/png?seed=ai")
                .crossfade(true)
                .build(),
            contentDescription = "AI avatar",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Typing dots
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.DarkGray.copy(alpha = 0.3f),
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    TypingDot(delayMillis = index * 150)
                }
            }
        }
    }
}

@Composable
fun TypingDot(delayMillis: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = delayMillis),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color.Gray.copy(alpha = alpha))
    )
}

@Composable
fun ChatComposer(
    input: String,
    suggestions: List<String>,
    canSend: Boolean,
    onInputChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onAttachClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Suggestion chips
            if (suggestions.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestions) { suggestion ->
                        SuggestionChip(
                            text = suggestion,
                            onClick = { onSuggestionClick(suggestion) }
                        )
                    }
                }
            }

            // Input bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attach button
                IconButton(
                    onClick = onAttachClick,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Attach file" }
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.DarkGray.copy(alpha = 0.5f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Input field
                OutlinedTextField(
                    value = input,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Setup'ınla ilgili bir soru sor",
                            color = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Red,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedContainerColor = Color.DarkGray.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.DarkGray.copy(alpha = 0.3f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { if (canSend) onSendClick() }
                    ),
                    singleLine = false,
                    maxLines = 3
                )

                // Send button
                FloatingActionButton(
                    onClick = onSendClick,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Send message" },
                    containerColor = if (canSend) Color.Red else Color.Gray,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.DarkGray.copy(alpha = 0.5f),
        tonalElevation = 0.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// Helper function to group consecutive messages by role
data class MessageGroup(val role: Role, val messages: List<ChatMessage>)

fun groupMessages(messages: List<ChatMessage>): List<MessageGroup> {
    if (messages.isEmpty()) return emptyList()

    val groups = mutableListOf<MessageGroup>()
    var currentGroup = mutableListOf<ChatMessage>()
    var currentRole: Role? = null

    messages.forEach { message ->
        if (currentRole == null || currentRole == message.role) {
            currentGroup.add(message)
            currentRole = message.role
        } else {
            groups.add(MessageGroup(currentRole!!, currentGroup.toList()))
            currentGroup = mutableListOf(message)
            currentRole = message.role
        }
    }

    if (currentGroup.isNotEmpty() && currentRole != null) {
        groups.add(MessageGroup(currentRole!!, currentGroup.toList()))
    }

    return groups
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ChatScreenPreview() {
    val sampleMessages = listOf(
        ChatMessage.ai("Merhaba! F1 setup'ınla ilgili nasıl yardımcı olabilirim? Pist, hava durumu ve yaşadığın sorun hakkında bilgi verebilir misin?"),
        ChatMessage.user("Monza'da son virajda arkadan kayma yaşıyorum."),
        ChatMessage.ai("Anladım. Bu sorunu çözmek için arka kanadı 1-2 puan artırmayı ve diferansiyel ayarını düşürmeyi deneyebilirsin.")
    )

    GradientBackground {
        ChatScreen(
            uiState = ChatUiState(
                messages = sampleMessages,
                isTyping = true,
                input = "",
                canSend = false
            ),
            onEvent = {}
        )
    }
}
