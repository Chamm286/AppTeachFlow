// presentation/teacher/TeacherDashboardScreen.kt
package com.example.teachflow.presentation.teacher

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Class
import kotlinx.coroutines.launch

// Màu sắc
val NavyBlue = Color(0xFF0F4C81)
val NavyDark = Color(0xFF0A3A62)
val SuccessGreen = Color(0xFF34A853)
val WarningOrange = Color(0xFFFBBC05)
val ErrorRed = Color(0xFFEA4335)
val BgGray = Color(0xFFF5F7FA)
val CardWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1A1A2E)
val TextGray = Color(0xFF6B7280)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    navController: NavController,
    teacherId: String,
    teacherName: String
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedTab by remember { mutableStateOf(0) }

    // State cho dữ liệu
    var classes by remember { mutableStateOf<List<Class>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true)}

    // Load dữ liệu
    LaunchedEffect(teacherId) {
        isLoading = true
        try {
            Log.d("TEACHER_DASH", "📚 Đang tải lớp học cho giáo viên: $teacherId")
            val classList = RepoHolder.repo.getClassesByTeacher(teacherId)
            classes = classList
            Log.d("TEACHER_DASH", "✅ Đã tải ${classes.size} lớp học")
        } catch (e: Exception) {
            Log.e("TEACHER_DASH", "❌ Lỗi: ${e.message}")
        }
        isLoading = false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp).shadow(8.dp),
                drawerContainerColor = CardWhite
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(
                                brush = Brush.verticalGradient(colors = listOf(NavyBlue, NavyDark))
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .shadow(8.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👩‍🏫", fontSize = 36.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(teacherName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(teacherId, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Giáo viên", fontSize = 12.sp, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                            }
                        }
                    }

                    // Menu items
                    Column(modifier = Modifier.padding(16.dp)) {
                        DrawerMenuItem(Icons.Outlined.Dashboard, "Trang chủ", true) { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Outlined.School, "Lớp học") { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Outlined.Grade, "Bảng điểm") { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Outlined.Person, "Hồ sơ") { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Outlined.Chat, "Tin nhắn") { scope.launch { drawerState.close() } }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        DrawerMenuItem(Icons.Outlined.Logout, "Đăng xuất", isDestructive = true) {
                            scope.launch { drawerState.close() }
                            navController.navigate("login") {
                                popUpTo("teacher_dashboard") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.background(BgGray),
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = NavyBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) { Text("📚", fontSize = 22.sp) }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("TeachFlow", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyBlue)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = NavyBlue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = NavyBlue)
                        }
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Surface(
                                shape = CircleShape,
                                color = NavyBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(teacherName.take(1).uppercase(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavyBlue)
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                    containerColor = CardWhite
                ) {
                    val items = listOf("Trang chủ", "Lớp học", "Bảng điểm", "Cá nhân")
                    items.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    when (index) {
                                        0 -> if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home
                                        1 -> if (selectedTab == 1) Icons.Filled.School else Icons.Outlined.School
                                        2 -> if (selectedTab == 2) Icons.Filled.Grade else Icons.Outlined.Grade
                                        else -> if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person
                                    },
                                    contentDescription = title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = { Text(title, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyBlue,
                                selectedTextColor = NavyBlue,
                                unselectedIconColor = TextGray,
                                unselectedTextColor = TextGray
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (selectedTab) {
                0 -> HomeTabContent(padding, teacherName, classes, isLoading)
                1 -> ClassesTabContent(padding, classes, isLoading)
                2 -> GradesTabContent(padding)
                3 -> ProfileTabContent(padding, teacherName, teacherId)
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isActive: Boolean = false,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isActive) NavyBlue.copy(alpha = 0.1f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isActive) NavyBlue.copy(alpha = 0.1f) else Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = if (isDestructive) ErrorRed else if (isActive) NavyBlue else TextGray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 14.sp, fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal, color = if (isDestructive) ErrorRed else if (isActive) NavyBlue else TextDark, modifier = Modifier.weight(1f))
        if (isActive) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(NavyBlue))
        }
    }
}

@Composable
fun HomeTabContent(padding: PaddingValues, teacherName: String, classes: List<Class>, isLoading: Boolean) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NavyBlue)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyBlue)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Chào mừng,", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(teacherName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Chúc một ngày tốt lành!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                        }
                        Text("👩‍🏫", fontSize = 48.sp)
                    }
                }
            }

            item {
                Text("📚 Lớp học của bạn", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            }

            if (classes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📭", fontSize = 48.sp)
                            Text("Chưa có lớp học nào", fontSize = 14.sp, color = TextGray)
                        }
                    }
                }
            } else {
                items(classes.take(3)) { classItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = NavyBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) { Text("📖", fontSize = 24.sp) }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(classItem.name, fontWeight = FontWeight.SemiBold, color = TextDark)
                                Text("👥 ${classItem.studentCount} học sinh", fontSize = 12.sp, color = TextGray)
                                Text("🏠 ${classItem.room ?: "Chưa có phòng"}", fontSize = 11.sp, color = NavyBlue)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassesTabContent(padding: PaddingValues, classes: List<Class>, isLoading: Boolean) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NavyBlue)
        }
    } else if (classes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📚", fontSize = 64.sp)
                Text("Chưa có lớp học nào", fontSize = 16.sp, color = TextGray)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(classes) { classItem ->
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
                            shape = RoundedCornerShape(12.dp),
                            color = NavyBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) { Text("📖", fontSize = 28.sp) }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(classItem.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                            Text("👥 ${classItem.studentCount} học sinh", fontSize = 12.sp, color = TextGray)
                            Text("🏠 ${classItem.room ?: "Chưa có"} - 👩‍🏫 ${classItem.teacherName}", fontSize = 11.sp, color = NavyBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradesTabContent(padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📊", fontSize = 64.sp)
            Text("Bảng điểm", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Đang phát triển...", fontSize = 14.sp, color = TextGray)
        }
    }
}

@Composable
fun ProfileTabContent(padding: PaddingValues, teacherName: String, teacherId: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = CircleShape,
                color = NavyBlue.copy(alpha = 0.1f),
                modifier = Modifier.size(100.dp)
            ) {
                Box(contentAlignment = Alignment.Center) { Text("👩‍🏫", fontSize = 56.sp) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(teacherName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Mã GV: $teacherId", fontSize = 14.sp, color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Chức vụ: Giáo viên", fontSize = 14.sp, color = TextGray)
        }
    }
}
