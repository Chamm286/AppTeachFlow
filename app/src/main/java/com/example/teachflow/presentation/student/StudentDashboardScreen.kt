package com.example.teachflow.presentation.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.presentation.theme.TeachFlowBlue
import com.example.teachflow.presentation.theme.TeachFlowSuccess
import com.example.teachflow.presentation.theme.TeachFlowSurface
import com.example.teachflow.presentation.theme.TeachFlowText
import com.example.teachflow.presentation.theme.TeachFlowTextSecondary

val TeachFlowWarning = Color(0xFFFBBC05)
val TeachFlowError = Color(0xFFEA4335)

@Composable
fun StudentDashboardScreen(
    navController: NavController,
    studentId: String,
    studentName: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TeachFlowSurface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TeachFlowSuccess)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Chào mừng,", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                        Text(studentName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Học tập chăm chỉ nhé!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Text("👨‍🎓", fontSize = 48.sp)
                }
            }
        }
        
        // Stats Row - Sửa lỗi weight
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StudentStatCard(
                    modifier = Modifier.weight(1f),
                    value = "8.5",
                    label = "Điểm TB",
                    icon = Icons.Default.Grade,
                    color = TeachFlowSuccess
                )
                StudentStatCard(
                    modifier = Modifier.weight(1f),
                    value = "12",
                    label = "Môn học",
                    icon = Icons.Default.School,
                    color = TeachFlowBlue
                )
                StudentStatCard(
                    modifier = Modifier.weight(1f),
                    value = "5",
                    label = "Huy hiệu",
                    icon = Icons.Default.Star,
                    color = TeachFlowWarning
                )
            }
        }
        
        // Điểm gần đây
        item {
            Text("📝 Điểm gần đây", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TeachFlowText)
        }
        
        items(listOf(
            "Toán" to 8.5,
            "Văn" to 7.8,
            "Lý" to 9.0,
            "Hóa" to 8.2
        )) { (subject, score) ->
            GradeCard(subject, score)
        }
        
        // Nhiệm vụ hôm nay
        item {
            Text("📋 Nhiệm vụ hôm nay", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TeachFlowText, modifier = Modifier.padding(top = 8.dp))
        }
        
        items(listOf(
            "Bài tập Toán - Chương 1" to "20/05",
            "Soạn văn - Bài thơ" to "21/05"
        )) { (task, deadline) ->
            TaskCard(task, deadline)
        }
    }
}

@Composable
fun StudentStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TeachFlowText)
            Text(label, fontSize = 11.sp, color = TeachFlowTextSecondary)
        }
    }
}

@Composable
fun GradeCard(subject: String, score: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(subject, fontWeight = FontWeight.Medium, color = TeachFlowText)
                Text("Mới cập nhật", fontSize = 11.sp, color = TeachFlowTextSecondary)
            }
            Text(
                String.format("%.1f", score),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (score >= 8) TeachFlowSuccess else if (score >= 5) TeachFlowWarning else TeachFlowError
            )
        }
    }
}

@Composable
fun TaskCard(task: String, deadline: String) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = TeachFlowBlue.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) { Text("📝", fontSize = 20.sp) }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(task, fontWeight = FontWeight.Medium, color = TeachFlowText)
                Text("Hạn: $deadline", fontSize = 11.sp, color = TeachFlowTextSecondary)
            }
            Icon(Icons.Default.ChevronRight, null, tint = TeachFlowTextSecondary)
        }
    }
}
