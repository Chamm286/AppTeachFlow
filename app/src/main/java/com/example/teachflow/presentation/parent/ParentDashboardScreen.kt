package com.example.teachflow.presentation.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Student
import com.example.teachflow.data.model.Grade
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    navController: NavController,
    parentId: String,
    parentName: String,
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var children by remember { mutableStateOf<List<Student>>(emptyList()) }
    var selectedChild by remember { mutableStateOf<Student?>(null) }
    var recentGrades by remember { mutableStateOf<List<Grade>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load danh sách con từ Firebase
    LaunchedEffect(parentId) {
        isLoading = true
        errorMessage = null
        try {
            children = RepoHolder.repo.getChildrenByParent(parentId)
            if (children.isNotEmpty()) {
                selectedChild = children.first()
                recentGrades = RepoHolder.repo.getRecentGradesByStudent(selectedChild?.id ?: "")
            }
        } catch (e: Exception) {
            errorMessage = e.message ?: "Có lỗi xảy ra"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier.background(Color(0xFFF5F7FA)),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("👨‍👩‍👧", fontSize = 22.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("TeachFlow", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Phụ huynh", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F4C81)
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Badge(containerColor = Color(0xFFEA4335)) { Text("3", fontSize = 10.sp) }
                        Icon(Icons.Outlined.Notifications, null, tint = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { navController.navigate("profile") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(parentName.take(1).uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F4C81))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF0F4C81))
                    }
                }
                errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("⚠️", fontSize = 48.sp)
                        Text(errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
                        Button(onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                try {
                                    children = RepoHolder.repo.getChildrenByParent(parentId)
                                    if (children.isNotEmpty()) {
                                        selectedChild = children.first()
                                        recentGrades = RepoHolder.repo.getRecentGradesByStudent(selectedChild?.id ?: "")
                                    }
                                } catch (e: Exception) {
                                    errorMessage = e.message ?: "Có lỗi xảy ra"
                                }
                                isLoading = false
                            }
                        }) {
                            Text("Thử lại")
                        }
                    }
                }
                children.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("👨‍👩‍👧", fontSize = 64.sp)
                        Text("Chưa có học sinh nào", fontSize = 16.sp, color = Color.Gray)
                        Text("Vui lòng liên hệ nhà trường để thêm thông tin", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Welcome Banner
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F4C81))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Chào mừng,", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                                        Text(parentName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Text("Bạn đang theo dõi ${children.size} học sinh", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                                    }
                                    Surface(
                                        shape = CircleShape,
                                        color = Color.White.copy(alpha = 0.2f),
                                        modifier = Modifier.size(56.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("👨‍👩‍👧", fontSize = 32.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Chọn con (nếu nhiều hơn 1)
                        if (children.size > 1) {
                            item {
                                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                                    Text("Chọn học sinh", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        children.forEach { child ->
                                            FilterChip(
                                                selected = selectedChild?.id == child.id,
                                                onClick = {
                                                    selectedChild = child
                                                    scope.launch {
                                                        recentGrades = RepoHolder.repo.getRecentGradesByStudent(child.id)
                                                    }
                                                },
                                                label = { Text(child.name, fontSize = 13.sp) },
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = Color(0xFF0F4C81).copy(alpha = 0.1f),
                                                    selectedLabelColor = Color(0xFF0F4C81)
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Thông tin học sinh được chọn - SỬA LỖI SMART CAST
                        val currentChild = selectedChild
                        if (currentChild != null) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF0F4C81).copy(alpha = 0.1f),
                                            modifier = Modifier.size(56.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text("👨‍🎓", fontSize = 28.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(currentChild.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                            Text("Lớp: ${currentChild.className}", fontSize = 13.sp, color = Color(0xFF64748B))
                                            Text("Mã HS: ${currentChild.studentCode}", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                            Row(
                                                modifier = Modifier.padding(top = 8.dp),
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                InfoChip("📚 ĐTB: ${currentChild.averageScore}", Color(0xFF0F4C81))
                                                InfoChip("🏆 Xếp loại: ${currentChild.ranking}", Color(0xFF34A853))
                                                InfoChip("✅ Hạnh kiểm: ${currentChild.conduct}", Color(0xFFFBBC05))
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                SectionHeader("📊 Điểm số gần đây", "Xem tất cả") {
                                    navController.navigate("grades/${currentChild.id}")
                                }
                            }

                            if (recentGrades.isEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Text("Chưa có điểm số", color = Color.Gray, modifier = Modifier.padding(16.dp))
                                    }
                                }
                            } else {
                                item {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(recentGrades) { grade ->
                                            GradeCard(grade = grade)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(text, fontSize = 11.sp, color = color, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

@Composable
fun SectionHeader(title: String, actionText: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
        TextButton(onClick = onAction) {
            Text(actionText, fontSize = 12.sp, color = Color(0xFF0F4C81))
        }
    }
}

@Composable
fun GradeCard(grade: Grade) {
    Card(
        modifier = Modifier.width(120.dp).height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(grade.subjectName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
            Text(String.format("%.1f", grade.average), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F4C81))
            Text("Trung bình", fontSize = 10.sp, color = Color(0xFF64748B))
        }
    }
}
