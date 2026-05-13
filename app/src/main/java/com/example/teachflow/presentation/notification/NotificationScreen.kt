package com.example.teachflow.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    userId: String,
    userRole: String
) {
    var notifications by remember { 
        mutableStateOf(
            listOf(
                NotificationItem(
                    id = "1",
                    title = "Chào mừng bạn!",
                    content = "Chào mừng bạn đến với TeachFlow",
                    time = "2 phút trước",
                    isRead = false,
                    icon = "🎉"
                ),
                NotificationItem(
                    id = "2",
                    title = "Cập nhật hệ thống",
                    content = "Hệ thống đã được cập nhật phiên bản mới",
                    time = "1 giờ trước",
                    isRead = false,
                    icon = "🔄"
                ),
                NotificationItem(
                    id = "3",
                    title = "Nhắc nhở",
                    content = "Bạn có 3 bài tập chưa hoàn thành",
                    time = "2 giờ trước",
                    isRead = true,
                    icon = "📝"
                )
            )
        ) 
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        notifications = notifications.map { it.copy(isRead = true) }
                    }) {
                        Icon(Icons.Filled.DoneAll, contentDescription = "Đánh dấu đã đọc")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F4C81),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F7FA)),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(
                    notification = notification,
                    onRead = {
                        notifications = notifications.map { 
                            if (it.id == notification.id) it.copy(isRead = true) else it 
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                if (!notification.isRead) onRead()
            }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color(0xFF0F4C81).copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = notification.icon,
                fontSize = 28.sp
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A2E),
                    fontSize = 16.sp
                )
                Text(
                    text = notification.content,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2
                )
                Text(
                    text = notification.time,
                    fontSize = 10.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFEA4335), RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

data class NotificationItem(
    val id: String,
    val icon: String,
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean
)
