package com.example.teachflow.presentation.teacher

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teachflow.presentation.teacher.viewmodel.TeacherViewModel

@Composable
fun TeacherHomeScreen(
    navController: NavController,
    userId: String
) {
    val viewModel: TeacherViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(userId) {
        viewModel.loadData(userId)
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6366F1))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("✨ Chào mừng!", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                        Text(state.teacherName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("👨‍🏫", fontSize = 40.sp)
                }
            }
        }
        
        // Thống kê
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TeacherStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "📚",
                    value = state.classCount.toString(),
                    label = "Lớp học",
                    color = Color(0xFF6366F1)
                )
                TeacherStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "👥",
                    value = state.studentCount.toString(),
                    label = "Học sinh",
                    color = Color(0xFF10B981)
                )
                TeacherStatCard(
                    modifier = Modifier.weight(1f),
                    icon = "📊",
                    value = String.format("%.1f", state.avgScore),
                    label = "Điểm TB",
                    color = Color(0xFFF59E0B)
                )
            }
        }
        
        // Lớp học
        item {
            Text("📚 Lớp học của tôi", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        }
        
        if (state.classes.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text("Chưa có lớp học nào", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            }
        } else {
            items(state.classes) { classItem ->
                ClassCardTeacher(
                    name = classItem.name,
                    subject = classItem.subject,
                    studentCount = classItem.studentCount,
                    onClick = { }
                )
            }
        }
        
        // Tác vụ nhanh
        item {
            Text("⚡ Tác vụ nhanh", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionTeacher(
                    modifier = Modifier.weight(1f),
                    icon = "📝",
                    title = "Nhập điểm",
                    color = Color(0xFF6366F1),
                    onClick = { }
                )
                QuickActionTeacher(
                    modifier = Modifier.weight(1f),
                    icon = "📖",
                    title = "Điểm danh",
                    color = Color(0xFF10B981),
                    onClick = { }
                )
                QuickActionTeacher(
                    modifier = Modifier.weight(1f),
                    icon = "📊",
                    title = "Thống kê",
                    color = Color(0xFFF59E0B),
                    onClick = { }
                )
                QuickActionTeacher(
                    modifier = Modifier.weight(1f),
                    icon = "🔔",
                    title = "Thông báo",
                    color = Color(0xFFEF4444),
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun TeacherStatCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 28.sp)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ClassCardTeacher(name: String, subject: String, studentCount: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF6366F1).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) { Text("📖", fontSize = 24.sp) }
            }
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("$subject • $studentCount học sinh", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun QuickActionTeacher(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 24.sp)
            Text(title, fontSize = 11.sp, color = color)
        }
    }
}
