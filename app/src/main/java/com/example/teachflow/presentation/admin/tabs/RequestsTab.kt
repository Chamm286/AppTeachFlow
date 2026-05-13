package com.example.teachflow.presentation.admin.tabs

import androidx.navigation.NavController

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.presentation.admin.components.*

data class RequestData(val name: String, val type: String, val date: String, val status: String)

@Composable
fun RequestsTab(padding: PaddingValues, navController: NavController) {
    var requests by remember { mutableStateOf(listOf(
        RequestData("Nguyễn Văn An", "Đăng ký giáo viên", "10 phút trước", "pending"),
        RequestData("Trần Thị Bích", "Đăng ký học sinh", "30 phút trước", "pending"),
        RequestData("Lê Thị Mai", "Tạo lớp học", "1 giờ trước", "pending")
    )) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("⏳ Duyệt yêu cầu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Tổng số: ${requests.size} yêu cầu chờ duyệt", fontSize = 13.sp, color = TextGray)
        }
        items(requests) { request ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = WarningOrange.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("📝", fontSize = 24.sp) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(request.name, fontWeight = FontWeight.SemiBold, color = TextDark)
                        Text(request.type, fontSize = 12.sp, color = TextGray)
                        Text(request.date, fontSize = 10.sp, color = TextLight)
                    }
                    Row {
                        Button(
                            onClick = { requests = requests.filter { it.name != request.name } },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Duyệt", fontSize = 12.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { requests = requests.filter { it.name != request.name } },
                            colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Từ chối", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


