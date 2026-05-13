package com.example.teachflow.presentation.admin.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Class
import com.example.teachflow.presentation.admin.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminClassesTab(padding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var classes by remember { mutableStateOf<List<Class>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    
    fun loadClasses() {
        scope.launch {
            isLoading = true
            try {
                classes = RepoHolder.repo.getAllClasses()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
    
    LaunchedEffect(Unit) { loadClasses() }
    
    val filteredClasses = if (searchQuery.isEmpty()) classes else classes.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.teacherName.contains(searchQuery, ignoreCase = true)
    }
    
    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("📚 Quản lý lớp học", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Tổng số: ${classes.size} lớp học", fontSize = 13.sp, color = TextGray)
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Thêm", color = Color.White)
            }
        }
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Tìm kiếm theo tên lớp, giáo viên...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NavyPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredClasses) { classItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = NavyPrimary.copy(alpha = 0.1f),
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("📖", fontSize = 24.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(classItem.name, fontWeight = FontWeight.SemiBold, color = TextDark)
                                        Text("Môn: ${classItem.subject}", fontSize = 12.sp, color = TextGray)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (classItem.status == "active") SuccessGreen.copy(alpha = 0.1f) else ErrorRed.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        if (classItem.status == "active") "Đang hoạt động" else "Tạm dừng",
                                        fontSize = 10.sp,
                                        color = if (classItem.status == "active") SuccessGreen else ErrorRed,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("👩‍🏫 ${classItem.teacherName}", fontSize = 12.sp, color = NavyPrimary)
                                Text("👥 ${classItem.studentCount} học sinh", fontSize = 12.sp, color = TextGray)
                                Text("🏠 ${classItem.room}", fontSize = 12.sp, color = TextGray)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = { }) {
                                    Icon(Icons.Outlined.Edit, null, tint = NavyPrimary)
                                }
                                IconButton(onClick = { }) {
                                    Icon(Icons.Outlined.Delete, null, tint = ErrorRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


