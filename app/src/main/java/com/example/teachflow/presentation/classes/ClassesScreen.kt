package com.example.teachflow.presentation.classes

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Class
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    navController: NavController,
    userRole: String,
    userId: String
) {
    var classes by remember { mutableStateOf<List<Class>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Load dữ liệu từ Firebase
    LaunchedEffect(userId, userRole) {
        isLoading = true
        try {
            if (userRole == "teacher") {
                // Sửa: getClassesByTeacher trả về List, không phải Flow
                val classList = RepoHolder.repo.getClassesByTeacher(userId)
                classes = classList
                isLoading = false
            } else {
                // Nếu có hàm getClassesByStudent, sửa tương tự
                // Hiện tại chưa có, tạm thời để empty
                classes = emptyList()
                isLoading = false
            }
        } catch (e: Exception) {
            error = e.message
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lớp học", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    if (userRole == "teacher") {
                        IconButton(onClick = { /* TODO: Thêm lớp */ }) {
                            Icon(Icons.Default.Add, null, tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A73E8)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA))
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF1A73E8))
                    }
                }
                error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("⚠️", fontSize = 48.sp)
                        Text(error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
                        Button(onClick = {
                            scope.launch {
                                isLoading = true
                                try {
                                    val classList = RepoHolder.repo.getClassesByTeacher(userId)
                                    classes = classList
                                    error = null
                                } catch (e: Exception) {
                                    error = e.message
                                }
                                isLoading = false
                            }
                        }) {
                            Text("Thử lại")
                        }
                    }
                }
                classes.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("📚", fontSize = 64.sp)
                        Text("Chưa có lớp học nào", fontSize = 16.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        if (userRole == "teacher") {
                            Button(
                                onClick = { /* TODO: Tạo lớp */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                            ) {
                                Text("Tạo lớp học mới")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(classes) { classItem ->
                            ClassCard(
                                classItem = classItem,
                                userRole = userRole,
                                onClick = {
                                    navController.navigate("class_detail/${classItem.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassCard(
    classItem: Class,
    userRole: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1A73E8).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📖", fontSize = 24.sp)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202124)
                )
                Text(
                    text = classItem.subject,
                    fontSize = 13.sp,
                    color = Color(0xFF5F6368)
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "👥 ${classItem.studentCount} học sinh",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "🏠 ${classItem.room}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                if (userRole == "student") {
                    Text(
                        text = "👨‍🏫 ${classItem.teacherName}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}