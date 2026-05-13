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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.teachflow.presentation.student.viewmodel.StudentViewModel

@Composable
fun StudentHomeScreen(
    navController: NavController,
    userId: String
) {
    val viewModel: StudentViewModel = viewModel()
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
                        Text(state.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text("📚", fontSize = 40.sp)
                }
            }
        }
        
        // Thống kê nhanh
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCardStudent(
                    modifier = Modifier.weight(1f),
                    icon = "📖",
                    value = String.format("%.1f", state.averageScore),
                    label = "Điểm TB",
                    color = Color(0xFF10B981)
                )
                StatCardStudent(
                    modifier = Modifier.weight(1f),
                    icon = "📚",
                    value = state.classCount.toString(),
                    label = "Môn học",
                    color = Color(0xFF6366F1)
                )
                StatCardStudent(
                    modifier = Modifier.weight(1f),
                    icon = "✅",
                    value = state.taskCompleted.toString(),
                    label = "Hoàn thành",
                    color = Color(0xFFF59E0B)
                )
            }
        }
        
        // Nhiệm vụ hôm nay
        item {
            Text("📋 Nhiệm vụ hôm nay", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        }
        
        if (state.tasks.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Chưa có nhiệm vụ nào", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            }
        } else {
            items(state.tasks) { task ->
                TaskCardStudent(
                    title = task.title,
                    dueDate = task.dueDate,
                    isDone = task.isCompleted,
                    onClick = { viewModel.toggleTask(task.id, !task.isCompleted) }
                )
            }
        }
        
        // Lớp học
        item {
            Text("📚 Lớp học của bạn", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
        }
        
        if (state.classes.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Chưa có lớp học nào", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            }
        } else {
            items(state.classes) { classItem ->
                ClassCardStudent(
                    name = classItem.name,
                    subject = classItem.subject,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun StatCardStudent(
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
fun TaskCardStudent(title: String, dueDate: Long, isDone: Boolean, onClick: () -> Unit) {
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
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF6366F1).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) { Text("📝", fontSize = 20.sp) }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium)
                Text("Hạn: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(dueDate))}", fontSize = 11.sp, color = Color.Gray)
            }
            if (isDone) {
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF10B981))
            } else {
                Icon(Icons.Default.RadioButtonUnchecked, null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun ClassCardStudent(name: String, subject: String, onClick: () -> Unit) {
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
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF6366F1).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) { Text("📖", fontSize = 20.sp) }
            }
            Column {
                Text(name, fontWeight = FontWeight.Medium)
                Text(subject, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}
