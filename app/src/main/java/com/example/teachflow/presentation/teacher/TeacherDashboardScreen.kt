// presentation/teacher/TeacherDashboardScreen.kt
package com.example.teachflow.presentation.teacher

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.teachflow.data.model.Notification
import com.example.teachflow.data.model.Student
import com.example.teachflow.data.model.Grade
import com.example.teachflow.data.model.Teacher
import kotlinx.coroutines.launch

// Màu sắc
val NavyBlue = Color(0xFF0F4C81)
val NavyLight = Color(0xFF2369A3)
val NavyDark = Color(0xFF06335B)
val GlassWhite = Color(0xCCFFFFFF)
val SuccessGreen = Color(0xFF2D9D78)
val WarningOrange = Color(0xFFE6A23C)
val ErrorRed = Color(0xFFD64545)
val BgGray = Color(0xFFF0F2F5)
val CardWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1E293B)
val TextGray = Color(0xFF64748B)
val AccentGold = Color(0xFFFFD700)

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
    var notifications by remember { mutableStateOf<List<Notification>>(emptyList()) }
    var teacherData by remember { mutableStateOf<Teacher?>(null) }
    var selectedClassForGrades by remember { mutableStateOf<Class?>(null) }
    var studentsInSelectedClass by remember { mutableStateOf<List<Student>>(emptyList()) }
    var gradesInSelectedClass by remember { mutableStateOf<List<Grade>>(emptyList()) }
    
    var isLoading by remember { mutableStateOf(true)}
    var isGradesLoading by remember { mutableStateOf(false) }

    // Dữ liệu mẫu tạm thời
    LaunchedEffect(Unit) {
        isLoading = true
        // 1. Tạo danh sách lớp học mẫu
        classes = listOf(
            Class(id = "CL01", name = "Lớp 12A1", subject = "Toán học nâng cao", room = "P.302", studentCount = 45, schedule = "Thứ 2, 4 (Tiết 1-3)"),
            Class(id = "CL02", name = "Lớp 11B2", subject = "Toán cơ bản", room = "P.105", studentCount = 38, schedule = "Thứ 3, 5 (Tiết 4-6)"),
            Class(id = "CL03", name = "Lớp 10C3", subject = "Đại số 10", room = "P.404", studentCount = 42, schedule = "Thứ 6 (Tiết 7-9)")
        )

        // 2. Tạo thông báo mẫu
        notifications = listOf(
            Notification(id = "N1", title = "Họp hội đồng sư phạm", content = "Nội dung: Phổ biến kế hoạch thi cuối học kỳ 2 tại hội trường A.", type = "event", createdAt = System.currentTimeMillis()),
            Notification(id = "N2", title = "Cập nhật điểm thi lớp 12A1", content = "Nhắc nhở: Hạn cuối nhập điểm là ngày 20/05.", type = "exam", createdAt = System.currentTimeMillis() - 86400000),
            Notification(id = "N3", title = "Thông báo nghỉ lễ 2/9", content = "Toàn trường nghỉ lễ từ ngày 01/09 đến hết 03/09.", type = "info", createdAt = System.currentTimeMillis() - 172800000)
        )

        // 3. Tạo profile giáo viên mẫu
        teacherData = Teacher(
            id = teacherId,
            name = "Trần Nguyễn Bảo Trâm",
            email = "tram.tnb@school.edu.vn",
            position = "Trưởng bộ môn Toán",
            schoolName = "Trường THPT Chuyên Lê Hồng Phong",
            gender = "Nữ",
            qualifications = listOf("Thạc sĩ Toán học - ĐH Sư Phạm", "Chứng chỉ sư phạm quốc tế"),
            achievements = listOf("Giáo viên dạy giỏi cấp Thành phố 2023", "Chiến sĩ thi đua cơ sở")
        )
        
        isLoading = false
    }

    // Load học sinh và điểm mẫu khi chọn lớp
    LaunchedEffect(selectedClassForGrades) {
        if (selectedClassForGrades != null) {
            isGradesLoading = true
            // Tạo danh sách học sinh và điểm mẫu
            studentsInSelectedClass = listOf(
                Student(id = "HS001", name = "Nguyễn Văn An"),
                Student(id = "HS002", name = "Trần Thị Bình"),
                Student(id = "HS003", name = "Lê Hoàng Cường"),
                Student(id = "HS004", name = "Phạm Minh Đức")
            )
            
            gradesInSelectedClass = listOf(
                Grade(id = "G1", studentId = "HS001", classId = selectedClassForGrades!!.id, average = 8.5),
                Grade(id = "G2", studentId = "HS002", classId = selectedClassForGrades!!.id, average = 9.2),
                Grade(id = "G3", studentId = "HS003", classId = selectedClassForGrades!!.id, average = 6.8),
                Grade(id = "G4", studentId = "HS004", classId = selectedClassForGrades!!.id, average = 4.5)
            )
            isGradesLoading = false
        }
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
                        DrawerMenuItem(Icons.Filled.Home, "Trang chủ", true) { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Filled.School, "Lớp học") { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Filled.Star, "Bảng điểm") { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Filled.Person, "Hồ sơ") { scope.launch { drawerState.close() } }
                        DrawerMenuItem(Icons.Filled.Info, "Tin nhắn") { scope.launch { drawerState.close() } }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        DrawerMenuItem(Icons.Filled.ExitToApp, "Đăng xuất", isDestructive = true) {
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
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = NavyBlue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = NavyBlue)
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
                    val bottomNavItems = listOf("Trang chủ", "Lớp học", "Bảng điểm", "Cá nhân")
                    bottomNavItems.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                Icon(
                                    imageVector = when (index) {
                                        0 -> Icons.Filled.Home
                                        1 -> Icons.Filled.School
                                        2 -> Icons.Filled.Star
                                        else -> Icons.Filled.Person
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
            Box(modifier = Modifier.fillMaxSize().background(BgGray)) {
                // Background Decorative Elements
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = NavyBlue.copy(alpha = 0.05f),
                        radius = 400f,
                        center = Offset(this.size.width * 0.9f, this.size.height * 0.1f)
                    )
                }

                when (selectedTab) {
                    0 -> HomeTabContent(padding, teacherName, classes, notifications, isLoading)
                    1 -> ClassesTabContent(padding, classes, isLoading, navController)
                    2 -> GradesTabContent(
                        padding = padding,
                        classes = classes,
                        selectedClass = selectedClassForGrades,
                        onClassSelect = { selectedClassForGrades = it },
                        students = studentsInSelectedClass,
                        grades = gradesInSelectedClass,
                        isLoading = isGradesLoading
                    )
                    3 -> ProfileTabContent(padding, teacherName, teacherId, teacherData, navController)
                }
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
fun HomeTabContent(
    padding: PaddingValues, 
    teacherName: String, 
    classes: List<Class>, 
    notifications: List<Notification>,
    isLoading: Boolean
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NavyBlue)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).background(BgGray),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(NavyBlue, NavyLight),
                                start = Offset(0f, 0f),
                                end = Offset(1000f, 1000f)
                            )
                        )
                ) {
                    // Decorative patterns
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(Color.White.copy(alpha = 0.1f), radius = 200f, center = Offset(this.size.width * 0.8f, 0f))
                    }

                    Row(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.padding(bottom = 12.dp)
                            ) {
                                Text(
                                    "✨ Chúc bạn một ngày làm việc tốt lành",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                            Text("Chào bạn,", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                            Text(teacherName, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🧑‍🏫", fontSize = 40.sp)
                            }
                        }
                    }
                }
            }

            // Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCardPremium("Lớp học", classes.size.toString(), Icons.Default.School, NavyBlue, Modifier.weight(1f))
                    StatCardPremium("Học sinh", classes.sumOf { it.studentCount }.toString(), Icons.Default.Person, SuccessGreen, Modifier.weight(1f))
                }
            }

            // Notifications Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔔 Thông báo mới", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("Xem tất cả", fontSize = 12.sp, color = NavyBlue, modifier = Modifier.clickable { })
                }
            }

            if (notifications.isEmpty()) {
                item {
                    EmptyStateCard("Không có thông báo nào mới")
                }
            } else {
                items(items = notifications.take(3)) { notification: Notification ->
                    NotificationItem(notification)
                }
            }

            // Recent Classes
            item {
                Text("📅 Lịch dạy gần đây", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }

            if (classes.isEmpty()) {
                item { EmptyStateCard("Bạn chưa được phân công lớp nào") }
            } else {
                items(items = classes.take(2)) { classItem: Class ->
                    ClassCompactItem(classItem)
                }
            }
        }
    }
}

@Composable
fun StatCardPremium(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.shadow(12.dp, RoundedCornerShape(24.dp), ambientColor = color.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextGray)
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(
                    brush = Brush.radialGradient(
                        colors = when(notification.type) {
                            "exam" -> listOf(ErrorRed.copy(alpha = 0.2f), ErrorRed.copy(alpha = 0.05f))
                            "event" -> listOf(WarningOrange.copy(alpha = 0.2f), WarningOrange.copy(alpha = 0.05f))
                            else -> listOf(NavyBlue.copy(alpha = 0.2f), NavyBlue.copy(alpha = 0.05f))
                        }
                    )
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(when(notification.type) {
                    "exam" -> "📝"
                    "event" -> "📅"
                    else -> "📢"
                }, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(notification.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text(notification.content, fontSize = 13.sp, color = TextGray, maxLines = 1)
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun ClassCompactItem(classItem: Class) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(NavyBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(classItem.name.take(1), fontWeight = FontWeight.Bold, color = NavyBlue)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(classItem.name, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(classItem.schedule ?: "Chưa có lịch", fontSize = 12.sp, color = TextGray)
            }
            Text(classItem.room ?: "", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyBlue)
        }
    }
}

@Composable
fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite.copy(alpha = 0.5f))
    ) {
        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
            Text(message, fontSize = 14.sp, color = TextGray)
        }
    }
}

@Composable
fun ClassesTabContent(padding: PaddingValues, classes: List<Class>, isLoading: Boolean, navController: NavController) {
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
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Lớp học phụ trách", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                    Text("Quản lý danh sách và tình hình học tập", fontSize = 14.sp, color = TextGray)
                }
            }

            items(items = classes) { classItem: Class ->
                Card(
                    modifier = Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(NavyBlue.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📚", fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(classItem.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                Text(classItem.subject ?: "Môn học chuyên ngành", fontSize = 13.sp, color = NavyBlue, fontWeight = FontWeight.Medium)
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SuccessGreen.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    "Đang dạy",
                                    color = SuccessGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 16.dp), color = BgGray)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ClassInfoItem(Icons.Default.Person, "${classItem.studentCount} HS", "Học sinh")
                            ClassInfoItem(Icons.Default.Place, classItem.room ?: "P.Học", "Phòng học")
                            ClassInfoItem(Icons.Default.Info, "Tiết 1-3", "Thời gian")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { /* Detail */ },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                        ) {
                            Text("Xem chi tiết lớp học", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = NavyBlue, modifier = Modifier.size(18.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text(label, fontSize = 10.sp, color = TextGray)
    }
}

@Composable
fun GradesTabContent(
    padding: PaddingValues,
    classes: List<Class>,
    selectedClass: Class?,
    onClassSelect: (Class) -> Unit,
    students: List<Student>,
    grades: List<Grade>,
    isLoading: Boolean
) {
    Column(modifier = Modifier.fillMaxSize().padding(padding)) {
        // Class Selector
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Quản lý bảng điểm", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(items = classes) { classItem: Class ->
                    val isSelected = selectedClass?.id == classItem.id
                    Surface(
                        modifier = Modifier.clickable { onClassSelect(classItem) }.shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) NavyBlue else CardWhite,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, NavyBlue.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = classItem.name,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            color = if (isSelected) Color.White else NavyBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (selectedClass == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📊", fontSize = 80.sp)
                    Text("Vui lòng chọn một lớp học", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("Để bắt đầu quản lý điểm số", fontSize = 13.sp, color = TextGray)
                }
            }
        } else if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NavyBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyBlue.copy(alpha = 0.05f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Học sinh trong lớp", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = NavyBlue)
                            Text("Điểm trung bình", fontWeight = FontWeight.Bold, color = NavyBlue)
                        }
                    }
                }
                
                items(items = students) { student: Student ->
                    val studentGrade = grades.find { it.studentId == student.id }
                    val avg = studentGrade?.average ?: 0.0
                    val scoreColor = when {
                        avg >= 8.0 -> SuccessGreen
                        avg >= 5.0 -> WarningOrange
                        else -> ErrorRed
                    }
                    
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(scoreColor.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(student.name.take(1), fontWeight = FontWeight.Bold, color = scoreColor)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(student.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                Text("ID: ${student.id}", fontSize = 12.sp, color = TextGray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (avg > 0) String.format("%.1f", avg) else "--",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = scoreColor
                                )
                                Text(
                                    text = when {
                                        avg >= 8.0 -> "Giỏi"
                                        avg >= 6.5 -> "Khá"
                                        avg >= 5.0 -> "Trung bình"
                                        avg > 0 -> "Yếu"
                                        else -> "Chưa nhập"
                                    },
                                    fontSize = 10.sp,
                                    color = scoreColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTabContent(padding: PaddingValues, teacherName: String, teacherId: String, teacher: Teacher?, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).background(BgGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth().height(160.dp).background(NavyBlue),
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier.offset(y = 40.dp).size(100.dp).shadow(8.dp, CircleShape),
                    shape = CircleShape,
                    color = CardWhite
                ) {
                    Box(contentAlignment = Alignment.Center) { 
                        Text(if (teacher?.gender == "Nữ") "👩‍🏫" else "👨‍🏫", fontSize = 50.sp) 
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text(teacher?.name ?: teacherName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text(teacher?.position ?: "Giảng viên", fontSize = 14.sp, color = TextGray)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                ProfileInfoCard("Mã nhân viên", teacherId, Icons.Default.Info)
                ProfileInfoCard("Email", teacher?.email ?: "Đang tải...", Icons.Default.Email)
                ProfileInfoCard("Đơn vị", teacher?.schoolName ?: "Trường ĐH Công nghệ Thông tin", Icons.Default.Work)
                
                if (teacher?.qualifications?.isNotEmpty() == true) {
                    Text("🎓 Bằng cấp", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp), fontWeight = FontWeight.Bold, color = NavyBlue)
                    teacher.qualifications.forEach { q ->
                        ProfileDetailItem(q)
                    }
                }

                if (teacher?.achievements?.isNotEmpty() == true) {
                    Text("🏆 Thành tích", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp), fontWeight = FontWeight.Bold, color = NavyBlue)
                    teacher.achievements.forEach { a ->
                        ProfileDetailItem(a)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { 
                        navController.navigate("login") {
                            popUpTo("teacher_dashboard") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đăng xuất", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileDetailItem(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBlue.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 14.sp, color = TextDark)
        }
    }
}

@Composable
fun ProfileInfoCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = NavyBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, fontSize = 12.sp, color = TextGray)
                Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextDark)
            }
        }
    }
}
