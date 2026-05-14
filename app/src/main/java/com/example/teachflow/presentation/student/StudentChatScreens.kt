package com.example.teachflow.presentation.student

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.presentation.theme.*

// --- MOCK DATA GIẢNG VIÊN ---
data class TeacherChatPreview(val id: String, val name: String, val role: String, val lastMessage: String, val time: String, val unreadCount: Int, val avatarColor: Color)
data class StudentMessage(val id: String, val text: String, val isFromMe: Boolean, val time: String)

val mockTeacherChats = listOf(
    TeacherChatPreview("T01", "Thầy Nguyễn Văn A", "Giảng viên - Firebase", "Nhớ review lại database nhé em.", "10:30", 1, Color(0xFF1A73E8)),
    TeacherChatPreview("T02", "Cô Nguyễn Thị B", "Cố vấn - Scrum Master", "Sprint này team làm tốt lắm!", "Hôm qua", 0, Color(0xFF34A853)),
    TeacherChatPreview("T03", "Thầy Trần Văn C", "Giảng viên - Quản lý lớp", "Đã cập nhật danh sách lớp trên hệ thống.", "Thứ 3", 0, Color(0xFFEA4335)),
    TeacherChatPreview("T04", "Thầy Trần Anh D", "Giảng viên - Testing", "Pass hết test case chưa em?", "Thứ 2", 0, Color(0xFFFBBC05))
)

val mockStudentMessages = listOf(
    StudentMessage("1", "Dạ em chào thầy ạ, thầy cho em hỏi về cấu trúc Firebase mình đang dùng với ạ.", true, "10:15"),
    StudentMessage("2", "Chào em, cấu trúc hiện tại đang tối ưu rồi, nhưng em nhớ check lại rule bảo mật nhé.", false, "10:20"),
    StudentMessage("3", "Dạ vâng, chiều nay em sẽ cập nhật lại phần Security Rules luôn ạ.", true, "10:25"),
    StudentMessage("4", "Nhớ review lại database nhé em.", false, "10:30")
)

// --- MÀN HÌNH 1: DANH SÁCH TIN NHẮN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentChatListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trò chuyện với Giáo viên", fontWeight = FontWeight.Bold, color = TeachFlowText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TeachFlowText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(BgLightGray).padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockTeacherChats) { chat ->
                TeacherChatListItem(chat) {
                    navController.navigate("student_chat_detail/${chat.id}/${chat.name}")
                }
            }
        }
    }
}

@Composable
fun TeacherChatListItem(chat: TeacherChatPreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(52.dp), shape = CircleShape, color = chat.avatarColor.copy(alpha = 0.15f)) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🧑‍🏫", fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(chat.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TeachFlowText)
                    Text(chat.time, fontSize = 12.sp, color = TeachFlowTextSecondary)
                }
                Text(chat.role, fontSize = 11.sp, color = chat.avatarColor, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = chat.lastMessage,
                        fontSize = 14.sp,
                        color = if (chat.unreadCount > 0) TeachFlowText else TeachFlowTextSecondary,
                        fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (chat.unreadCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(modifier = Modifier.size(20.dp), shape = CircleShape, color = TeachFlowError) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(chat.unreadCount.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- MÀN HÌNH 2: CHI TIẾT CUỘC TRÒ CHUYỆN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentChatDetailScreen(navController: NavController, teacherName: String) {
    var textState by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(*mockStudentMessages.toTypedArray()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(teacherName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TeachFlowText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TeachFlowText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Nhắn tin cho giảng viên...", fontSize = 14.sp) },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BgLightGray,
                        unfocusedContainerColor = BgLightGray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (textState.isNotBlank()) {
                            messages.add(StudentMessage(id = System.currentTimeMillis().toString(), text = textState, isFromMe = true, time = "Vừa xong"))
                            textState = ""
                        }
                    },
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(TeachFlowBlue)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Gửi", tint = Color.White, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(BgLightGray).padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                StudentMessageBubble(msg)
            }
        }
    }
}

@Composable
fun StudentMessageBubble(message: StudentMessage) {
    val alignment = if (message.isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (message.isFromMe) TeachFlowBlue else Color.White
    val textColor = if (message.isFromMe) Color.White else TeachFlowText
    val shape = if (message.isFromMe) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Column(horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start) {
            Surface(shape = shape, color = bgColor, shadowElevation = 1.dp, modifier = Modifier.widthIn(max = 280.dp)) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = textColor,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(message.time, fontSize = 10.sp, color = TeachFlowTextSecondary.copy(alpha = 0.6f))
        }
    }
}