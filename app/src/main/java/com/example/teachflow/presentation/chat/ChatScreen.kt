package com.example.teachflow.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatUser(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatar: String = "",
    val role: String = "",
    val isOnline: Boolean = false
)

data class Conversation(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val lastMessageSenderId: String = "",
    val unreadCount: Map<String, Int> = emptyMap()
)

data class ChatMessageModel(
    val id: String = "",
    val conversationId: String = "",
    val text: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false,
    val type: String = "text" // text, image, file
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    userId: String,
    userRole: String
) {
    val scope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    
    var showConversations by remember { mutableStateOf(true) }
    var currentConversation by remember { mutableStateOf<Conversation?>(null) }
    var currentChatUser by remember { mutableStateOf<ChatUser?>(null) }
    var messageText by remember { mutableStateOf("") }
    
    // State cho danh sách conversations
    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }
    var conversationsLoading by remember { mutableStateOf(true) }
    
    // State cho messages
    var messages by remember { mutableStateOf<List<ChatMessageModel>>(emptyList()) }
    var messagesLoading by remember { mutableStateOf(false) }
    
    // Listener registration để cleanup
    var conversationListener by remember { mutableStateOf<ListenerRegistration?>(null) }
    var messagesListener by remember { mutableStateOf<ListenerRegistration?>(null) }
    
    // Load conversations từ Firebase
    LaunchedEffect(userId) {
        conversationsLoading = true
        try {
            // Lấy các conversation mà user tham gia
            db.collection("conversations")
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTime", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("CHAT", "Lỗi load conversations: ${error.message}")
                        conversationsLoading = false
                        return@addSnapshotListener
                    }
                    
                    val convList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Conversation::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    
                    conversations = convList
                    conversationsLoading = false
                }
        } catch (e: Exception) {
            Log.e("CHAT", "Lỗi: ${e.message}")
            conversationsLoading = false
        }
    }
    
    // Load messages khi chọn conversation
    LaunchedEffect(currentConversation) {
        messagesListener?.remove()
        
        if (currentConversation != null) {
            messagesLoading = true
            
            // Lấy thông tin người chat
            val otherUserId = currentConversation!!.participants.first { it != userId }
            try {
                val userDoc = db.collection("users").document(otherUserId).get().await()
                currentChatUser = userDoc.toObject(ChatUser::class.java)?.copy(id = otherUserId)
            } catch (e: Exception) {
                Log.e("CHAT", "Lỗi load user: ${e.message}")
            }
            
            // Lắng nghe messages realtime
            messagesListener = db.collection("messages")
                .document(currentConversation!!.id)
                .collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("CHAT", "Lỗi load messages: ${error.message}")
                        messagesLoading = false
                        return@addSnapshotListener
                    }
                    
                    val msgList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(ChatMessageModel::class.java)?.copy(id = doc.id)
                    } ?: emptyList()
                    
                    messages = msgList
                    messagesLoading = false
                    
                    // Đánh dấu đã đọc
                    markMessagesAsRead(db, currentConversation!!.id, userId)
                }
        }
    }
    
    // Cleanup listeners
    DisposableEffect(Unit) {
        onDispose {
            conversationListener?.remove()
            messagesListener?.remove()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (showConversations) {
                        Text("Tin nhắn", fontWeight = FontWeight.Bold)
                    } else {
                        Column {
                            Text(currentChatUser?.name ?: "Chat", fontWeight = FontWeight.Bold)
                            if (currentChatUser?.isOnline == true) {
                                Text("Đang hoạt động", fontSize = 12.sp, color = Color.Green)
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!showConversations) {
                            showConversations = true
                            currentConversation = null
                            currentChatUser = null
                        } else {
                            navController.navigateUp()
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F4C81),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    if (showConversations) {
                        IconButton(onClick = { /* TODO: Tìm kiếm */ }) {
                            Icon(Icons.Filled.Search, contentDescription = "Tìm kiếm", tint = Color.White)
                        }
                        IconButton(onClick = { /* TODO: Menu */ }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Tùy chọn", tint = Color.White)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!showConversations) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Nhập tin nhắn...") },
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0F4C81),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        FloatingActionButton(
                            onClick = {
                                if (messageText.isNotBlank() && currentConversation != null) {
                                    sendMessage(
                                        db = db,
                                        conversationId = currentConversation!!.id,
                                        text = messageText,
                                        senderId = userId,
                                        senderName = userRole,
                                        receiverId = currentConversation!!.participants.first { it != userId }
                                    )
                                    messageText = ""
                                }
                            },
                            containerColor = Color(0xFF0F4C81),
                            shape = CircleShape,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "Gửi", tint = Color.White)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showConversations) {
                // Danh sách conversations
                if (conversationsLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF0F4C81))
                    }
                } else if (conversations.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Chưa có tin nhắn nào", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(conversations) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                currentUserId = userId,
                                onClick = {
                                    currentConversation = conversation
                                    showConversations = false
                                }
                            )
                        }
                    }
                }
            } else {
                // Khung chat
                if (messagesLoading && messages.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF0F4C81))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F7FA)),
                        contentPadding = PaddingValues(16.dp),
                        reverseLayout = true
                    ) {
                        items(messages.reversed()) { message ->
                            ChatBubbleItem(
                                message = message,
                                isSentByUser = message.senderId == userId
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationItem(
    conversation: Conversation,
    currentUserId: String,
    onClick: () -> Unit
) {
    val otherUserId = conversation.participants.first { it != currentUserId }
    var otherUser by remember { mutableStateOf<ChatUser?>(null) }
    val db = FirebaseFirestore.getInstance()
    
    LaunchedEffect(otherUserId) {
        try {
            val doc = db.collection("users").document(otherUserId).get().await()
            otherUser = doc.toObject(ChatUser::class.java)?.copy(id = otherUserId)
        } catch (e: Exception) {
            Log.e("CHAT", "Lỗi: ${e.message}")
        }
    }
    
    val unread = conversation.unreadCount[currentUserId] ?: 0
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (unread > 0) Color(0xFF0F4C81).copy(alpha = 0.05f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color(0xFF0F4C81)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = otherUser?.avatar ?: "👤",
                        fontSize = 24.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = otherUser?.name ?: "Đang tải...",
                        fontWeight = if (unread > 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(conversation.lastMessageTime),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = conversation.lastMessage,
                        fontSize = 13.sp,
                        color = if (unread > 0) Color.Black else Color.Gray,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    if (unread > 0) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEA4335),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (unread > 99) "99+" else unread.toString(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessageModel, isSentByUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isSentByUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isSentByUser) 16.dp else 4.dp,
                bottomEnd = if (isSentByUser) 4.dp else 16.dp
            ),
            color = if (isSentByUser) Color(0xFF0F4C81) else Color.White,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isSentByUser) {
                    Text(
                        text = message.senderName,
                        fontSize = 11.sp,
                        color = Color(0xFF0F4C81),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = message.text,
                    color = if (isSentByUser) Color.White else Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp),
                    color = if (isSentByUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

// Hàm gửi message lên Firebase
fun sendMessage(
    db: FirebaseFirestore,
    conversationId: String,
    text: String,
    senderId: String,
    senderName: String,
    receiverId: String
) {
    val message = ChatMessageModel(
        id = UUID.randomUUID().toString(),
        conversationId = conversationId,
        text = text,
        senderId = senderId,
        senderName = senderName,
        receiverId = receiverId,
        timestamp = System.currentTimeMillis(),
        isRead = false,
        type = "text"
    )
    
    // Thêm message vào subcollection
    db.collection("messages")
        .document(conversationId)
        .collection("chats")
        .document(message.id)
        .set(message)
    
    // Cập nhật lastMessage trong conversation
    db.collection("conversations")
        .document(conversationId)
        .update(
            mapOf(
                "lastMessage" to text,
                "lastMessageTime" to System.currentTimeMillis(),
                "lastMessageSenderId" to senderId,
                "unreadCount.$receiverId" to com.google.firebase.firestore.FieldValue.increment(1)
            )
        )
}

// Hàm đánh dấu đã đọc
fun markMessagesAsRead(db: FirebaseFirestore, conversationId: String, userId: String) {
    db.collection("conversations")
        .document(conversationId)
        .update("unreadCount.$userId", 0)
    
    // Có thể update isRead cho từng message nếu cần
}
