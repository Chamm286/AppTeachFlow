package com.example.teachflow.presentation.student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.presentation.theme.*

// Thêm các hằng số màu bổ trợ cho UI học sinh
val BgLightGray = Color(0xFFF8FAFC)
val CardWhite = Color(0xFFFFFFFF)

@Composable
fun StudentDashboardScreen(
    navController: NavController,
    studentId: String,
    studentName: String
) {
    Box(modifier = Modifier.fillMaxSize().background(BgLightGray)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Header Section với Gradient nhô lên
            item {
                StudentHeader(studentName)
            }

            // 2. Stats Section - Overlapping with Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-30).dp), // Tạo hiệu ứng nhô lên
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StudentStatCard(
                        modifier = Modifier.weight(1f),
                        value = "8.5",
                        label = "GPA",
                        icon = "📈",
                        color = TeachFlowSuccess
                    )
                    StudentStatCard(
                        modifier = Modifier.weight(1f),
                        value = "12",
                        label = "Môn học",
                        icon = "📚",
                        color = TeachFlowBlue
                    )
                    StudentStatCard(
                        modifier = Modifier.weight(1f),
                        value = "05",
                        label = "Huy hiệu",
                        icon = "🏆",
                        color = TeachFlowWarning
                    )
                }
            }

            // 3. Recent Grades Section
            item {
                SectionHeader("📊 Điểm số gần đây", onSeeAllClick = {})
            }

            items(listOf(
                GradeData("Toán học", 8.5, "15/05", Color(0xFFE0F2FE), Color(0xFF0284C7)),
                GradeData("Ngữ văn", 7.8, "14/05", Color(0xFFFEF2F2), Color(0xFFDC2626)),
                GradeData("Vật lý", 9.0, "12/05", Color(0xFFF0FDF4), Color(0xFF16A34A)),
                GradeData("Hóa học", 8.2, "10/05", Color(0xFFFFF7ED), Color(0xFFEA580C))
            )) { data ->
                GradeCardEnhanced(data)
            }

            // 4. Tasks Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("📋 Nhiệm vụ sắp tới", onSeeAllClick = {})
            }

            items(listOf(
                TaskData("Bài tập Toán - Chương 1", "Hôm nay, 23:59", "BÀI TẬP", Color(0xFFEEF2FF), Color(0xFF4F46E5)),
                TaskData("Soạn văn - Bài thơ mới", "Ngày mai", "TỰ HỌC", Color(0xFFF5F3FF), Color(0xFF7C3AED))
            )) { task ->
                TaskCardEnhanced(task)
            }
        }
    }
}

@Composable
fun StudentHeader(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TeachFlowSuccess, TeachFlowSuccess.copy(alpha = 0.8f))
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    "Chào mừng trở lại,",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = CircleShape
                ) {
                    Text(
                        "Lớp 12A1 • Học sinh xuất sắc",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
            
            // Avatar & Notification
            Box(contentAlignment = Alignment.TopEnd) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("👨‍🎓", fontSize = 32.sp)
                    }
                }
                Surface(
                    modifier = Modifier.size(14.dp),
                    shape = CircleShape,
                    color = TeachFlowError,
                    border = BorderStroke(2.dp, Color.White)
                ) {}
            }
        }
    }
}

@Composable
fun StudentStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TeachFlowText
            )
            Text(
                label,
                fontSize = 12.sp,
                color = TeachFlowTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TeachFlowText)
        Text(
            "Xem tất cả",
            fontSize = 13.sp,
            color = TeachFlowBlue,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

@Composable
fun GradeCardEnhanced(data: GradeData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = data.bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.MenuBook,
                        null,
                        tint = data.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(data.subject, fontWeight = FontWeight.Bold, color = TeachFlowText, fontSize = 15.sp)
                Text(data.date, fontSize = 12.sp, color = TeachFlowTextSecondary)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    String.format("%.1f", data.score),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = data.accentColor
                )
                Text("Điểm số", fontSize = 10.sp, color = TeachFlowTextSecondary)
            }
        }
    }
}

@Composable
fun TaskCardEnhanced(task: TaskData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = task.bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Assignment, null, tint = task.accentColor, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(task.title, fontWeight = FontWeight.Bold, color = TeachFlowText, fontSize = 14.sp)
                }
                Text(task.deadline, fontSize = 12.sp, color = TeachFlowTextSecondary)
            }
            
            Surface(
                color = task.bgColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    task.tag,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = task.accentColor
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

data class GradeData(val subject: String, val score: Double, val date: String, val bgColor: Color, val accentColor: Color)
data class TaskData(val title: String, val deadline: String, val tag: String, val bgColor: Color, val accentColor: Color)
