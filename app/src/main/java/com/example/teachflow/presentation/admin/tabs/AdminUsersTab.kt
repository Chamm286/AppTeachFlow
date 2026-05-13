package com.example.teachflow.presentation.admin.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
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
import com.example.teachflow.data.model.User
import com.example.teachflow.presentation.admin.*
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersTab(padding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    
    // State cho Dialogs
    var showUserDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<User?>(null) }
    
    fun loadUsers() {
        scope.launch {
            isLoading = true
            try {
                users = RepoHolder.repo.getAllUsers().sortedByDescending { it.createdAt }
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
    
    Column(modifier = Modifier.fillMaxSize().padding(padding).background(BgGray)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("👥 Quản lý người dùng", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Hệ thống có ${users.size} tài khoản", fontSize = 13.sp, color = TextGray)
            }
            Button(
                onClick = { 
                    selectedUser = null
                    showUserDialog = true 
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Thêm mới", color = Color.White, fontSize = 14.sp)
            }
        }
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Tìm theo tên, email, vai trò...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextGray) },
            trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null) } },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = NavyPrimary
            ),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NavyPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp) // Tạo đường kẻ mờ giữa các item
            ) {
                items(filteredUsers) { user ->
                    UserItem(
                        user = user,
                        onEdit = { 
                            selectedUser = user
                            showUserDialog = true 
                        },
                        onDelete = {
                            userToDelete = user
                            showDeleteConfirm = true
                        },
                        onToggleActive = {
                            val updatedUser = user.copy(isActive = !user.isActive)
                            scope.launch {
                                RepoHolder.repo.updateUser(updatedUser)
                                loadUsers()
                            }
                        }
                    )
                }
            }
        }
    }
    
    // Dialog Thêm/Sửa người dùng
    if (showUserDialog) {
        UserDetailDialog(
            user = selectedUser,
            onDismiss = { showUserDialog = false },
            onConfirm = { newUser ->
                scope.launch {
                    if (selectedUser == null) {
                        RepoHolder.repo.createUser(newUser)
                    } else {
                        RepoHolder.repo.updateUser(newUser)
                    }
                    showUserDialog = false
                    loadUsers()
                }
            }
        )
    }
    
    // Dialog Xác nhận xóa
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn xóa người dùng '${userToDelete?.name}'? Hành động này không thể hoàn tác.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            userToDelete?.let { RepoHolder.repo.deleteUser(it.uid) }
                            showDeleteConfirm = false
                            loadUsers()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Xóa ngay", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Hủy") }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun UserItem(
    user: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar Placeholder
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(
                    when (user.role) {
                        "admin" -> ErrorRed.copy(alpha = 0.1f)
                        "teacher" -> SuccessGreen.copy(alpha = 0.1f)
                        else -> NavyPrimary.copy(alpha = 0.1f)
                    }
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when (user.role) {
                        "admin" -> "👨‍💼"
                        "teacher" -> "👩‍🏫"
                        else -> "👨‍🎓"
                    }, 
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(user.name, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 16.sp)
                Text(user.email, fontSize = 12.sp, color = TextGray)
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Role Badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (user.role) {
                            "admin" -> ErrorRed.copy(alpha = 0.1f)
                            "teacher" -> SuccessGreen.copy(alpha = 0.1f)
                            else -> NavyPrimary.copy(alpha = 0.1f)
                        }
                    ) {
                        Text(
                            when (user.role) {
                                "admin" -> "ADMIN"
                                "teacher" -> "GIÁO VIÊN"
                                else -> "HỌC SINH"
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (user.role) {
                                "admin" -> ErrorRed
                                "teacher" -> SuccessGreen
                                else -> NavyPrimary
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Status Badge
                    Text(
                        if (user.isActive) "● Đang hoạt động" else "○ Đã bị khóa",
                        fontSize = 11.sp,
                        color = if (user.isActive) SuccessGreen else ErrorRed
                    )
                }
            }
            
            // Actions
            Row {
                IconButton(onClick = onToggleActive) {
                    Icon(
                        if (user.isActive) Icons.Outlined.LockOpen else Icons.Outlined.Lock,
                        null, 
                        tint = if (user.isActive) SuccessGreen else ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Outlined.Edit, null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailDialog(
    user: User?,
    onDismiss: () -> Unit,
    onConfirm: (User) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var role by remember { mutableStateOf(user?.role ?: "student") }
    var isActive by remember { mutableStateOf(user?.isActive ?: true) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Thêm người dùng mới" else "Cập nhật thông tin", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Họ và tên") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = user == null // Không cho sửa email nếu là cập nhật
                )
                
                Text("Vai trò người dùng", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("admin", "teacher", "student").forEach { r ->
                        FilterChip(
                            selected = role == r,
                            onClick = { role = r },
                            label = { Text(r.replaceFirstChar { it.uppercase() }) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isActive, onCheckedChange = { isActive = it })
                    Text("Kích hoạt tài khoản", fontSize = 14.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalUser = user?.copy(
                        name = name,
                        role = role,
                        isActive = isActive,
                        updatedAt = System.currentTimeMillis()
                    ) ?: User(
                        uid = UUID.randomUUID().toString(),
                        name = name,
                        email = email,
                        role = role,
                        isActive = isActive,
                        createdAt = System.currentTimeMillis()
                    )
                    onConfirm(finalUser)
                },
                enabled = name.isNotBlank() && email.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) { Text("Lưu thay đổi", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}
