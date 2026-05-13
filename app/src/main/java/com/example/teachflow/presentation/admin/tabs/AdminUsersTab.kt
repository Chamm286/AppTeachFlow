package com.example.teachflow.presentation.admin.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.teachflow.data.model.User
import com.example.teachflow.presentation.admin.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersTab(padding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    
    fun loadUsers() {
        scope.launch {
            isLoading = true
            try {
                users = RepoHolder.repo.getAllUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
    
    LaunchedEffect(Unit) { loadUsers() }
    
    val filteredUsers = if (searchQuery.isEmpty()) users else users.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.email.contains(searchQuery, ignoreCase = true) ||
        it.role.contains(searchQuery, ignoreCase = true)
    }
    
    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("👥 Quản lý người dùng", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Tổng số: ${users.size} người dùng", fontSize = 13.sp, color = TextGray)
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
            placeholder = { Text("Tìm kiếm theo tên, email, vai trò...") },
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
                items(filteredUsers) { user ->
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
                                color = when (user.role) {
                                    "admin" -> ErrorRed.copy(alpha = 0.1f)
                                    "teacher" -> SuccessGreen.copy(alpha = 0.1f)
                                    else -> NavyPrimary.copy(alpha = 0.1f)
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(when (user.role) {
                                        "admin" -> "👨‍💼"
                                        "teacher" -> "👩‍🏫"
                                        else -> "👨‍🎓"
                                    }, fontSize = 24.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(user.name, fontWeight = FontWeight.SemiBold, color = TextDark)
                                Text(user.email, fontSize = 12.sp, color = TextGray)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = when (user.role) {
                                            "admin" -> ErrorRed.copy(alpha = 0.1f)
                                            "teacher" -> SuccessGreen.copy(alpha = 0.1f)
                                            else -> NavyPrimary.copy(alpha = 0.1f)
                                        }
                                    ) {
                                        Text(
                                            when (user.role) {
                                                "admin" -> "Quản trị viên"
                                                "teacher" -> "Giáo viên"
                                                else -> "Học sinh"
                                            },
                                            fontSize = 10.sp,
                                            color = when (user.role) {
                                                "admin" -> ErrorRed
                                                "teacher" -> SuccessGreen
                                                else -> NavyPrimary
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (user.isActive) "Hoạt động" else "Khóa", fontSize = 10.sp, color = if (user.isActive) SuccessGreen else ErrorRed)
                                }
                            }
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


