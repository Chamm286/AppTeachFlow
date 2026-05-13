package com.example.teachflow.presentation.admin.tabs

import androidx.navigation.NavController

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.SwapHoriz
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

@Composable
fun ConnectionsTab(padding: PaddingValues, navController: NavController) {
    val connections = listOf(
        Triple("Nguyễn Minh Quân", "Trần Nguyễn Bảo Trâm", "Lập trình Android"),
        Triple("Trần Phương Thảo", "Lê Minh Tuấn", "Cấu trúc dữ liệu"),
        Triple("Lê Hoàng Nam", "Phạm Thị Hồng", "Web Development")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("🔗 Kết nối Học sinh - Giáo viên", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Quản lý kết nối giữa học sinh và giáo viên", fontSize = 13.sp, color = TextGray)
        }
        items(connections) { (student, teacher, subject) ->
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
                        color = NavyPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("👨‍🎓", fontSize = 20.sp) }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Outlined.SwapHoriz, null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        shape = CircleShape,
                        color = SuccessGreen.copy(alpha = 0.1f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("👩‍🏫", fontSize = 20.sp) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("$student → $teacher", fontWeight = FontWeight.SemiBold, color = TextDark)
                        Text("Môn: $subject", fontSize = 12.sp, color = NavyPrimary)
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Xem chi tiết", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}


