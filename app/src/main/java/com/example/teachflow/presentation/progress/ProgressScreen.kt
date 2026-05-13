package com.example.teachflow.presentation.progress

import androidx.compose.foundation.background
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
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    userId: String
) {
    val courses = listOf(
        Pair("Toán cao cấp", 75),
        Pair("Ngữ văn", 60),
        Pair("Vật lý", 90),
        Pair("Tiếng Anh", 45)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tiến trình học tập", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A73E8)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header stats
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A73E8)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatProgressItem("📚", "85%", "Hoàn thành", Color.White)
                        StatProgressItem("⭐", "12", "Huy hiệu", Color(0xFFFBBC05))
                        StatProgressItem("🔥", "7", "Ngày streak", Color(0xFFEA4335))
                    }
                }
            }
            
            // Course progress
            item {
                Text("Khóa học đang học", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            items(courses) { (course, progress) ->
                CourseProgressCard(course, progress)
            }
            
            // Badges earned
            item {
                Text("Huy hiệu đã đạt", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BadgeCard(
                        modifier = Modifier.weight(1f),
                        icon = "🎯",
                        title = "Chăm chỉ",
                        description = "Hoàn thành 10 bài tập"
                    )
                    BadgeCard(
                        modifier = Modifier.weight(1f),
                        icon = "🏆",
                        title = "Xuất sắc",
                        description = "Điểm TB > 9.0"
                    )
                    BadgeCard(
                        modifier = Modifier.weight(1f),
                        icon = "⚡",
                        title = "Tốc độ",
                        description = "Làm bài nhanh"
                    )
                }
            }
            
            // Weekly activity chart
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Hoạt động tuần này", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        WeeklyActivityChart()
                    }
                }
            }
        }
    }
}

@Composable
fun StatProgressItem(icon: String, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 28.sp)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 11.sp, color = color.copy(alpha = 0.8f))
    }
}

@Composable
fun CourseProgressCard(course: String, progress: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(course, fontWeight = FontWeight.Medium)
                Text("$progress%", color = Color(0xFF1A73E8), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF1A73E8),
                trackColor = Color(0xFFE8EAED)
            )
        }
    }
}

@Composable
fun BadgeCard(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    description: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(description, fontSize = 10.sp, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun WeeklyActivityChart() {
    val days = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    val activities = listOf(4, 5, 3, 6, 4, 2, 1)
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.indices.forEach { index ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(32.dp, (activities[index] * 4).dp)
                        .background(
                            Color(0xFF1A73E8),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(days[index], fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}
