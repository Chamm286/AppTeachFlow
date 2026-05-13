// presentation/teacher/TeacherDashboardScreen.kt
package com.example.teachflow.presentation.teacher

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.data.model.Class
import com.example.teachflow.data.model.Notification
import com.example.teachflow.data.model.Student
import com.example.teachflow.data.model.Grade
import com.example.teachflow.data.model.Teacher
import kotlinx.coroutines.launch

// --- Premium Color Palette ---
val PrimaryBlue = Color(0xFF1E3A8A)
val SecondaryBlue = Color(0xFF3B82F6)
val AccentTeal = Color(0xFF10B981)
val SoftIndigo = Color(0xFF6366F1)
val PremiumGold = Color(0xFFF59E0B)
val GlassWhite = Color(0xBBFFFFFF)
val BgGradientStart = Color(0xFFF8FAFC)
val BgGradientEnd = Color(0xFFE2E8F0)
val SurfaceDark = Color(0xFF1E293B)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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

    // Load dữ liệu mẫu
    LaunchedEffect(Unit) {
        isLoading = true
        classes = listOf(
            Class(id = "CL01", name = "Lớp 12A1", subject = "Toán học nâng cao", room = "Phòng A.102", studentCount = 42, schedule = "Thứ 2 (7:30 - 11:30)"),
            Class(id = "CL02", name = "Lớp 11B4", subject = "Đại số & Giải tích", room = "Phòng B.305", studentCount = 35, schedule = "Thứ 4 (13:30 - 16:30)"),
            Class(id = "CL03", name = "Lớp 10C2", subject = "Hình học không gian", room = "Phòng C.201", studentCount = 40, schedule = "Thứ 6 (8:00 - 10:00)")
        )
        notifications = listOf(
            Notification(id = "1", title = "Họp chi bộ", content = "Nội dung chuẩn bị cho kỳ thi quốc gia.", type = "event"),
            Notification(id = "2", title = "Cập nhật điểm", content = "Vui lòng hoàn thành nhập điểm lớp 12A1.", type = "exam"),
            Notification(id = "3", title = "Nghỉ lễ", content = "Thông báo nghỉ lễ Giỗ tổ Hùng Vương.", type = "info")
        )
        teacherData = Teacher(
            id = teacherId,
            name = teacherName,
            position = "Trưởng bộ môn Toán",
            schoolName = "THPT Chuyên Lê Hồng Phong",
            email = "tram.tnb@school.edu.vn",
            qualifications = listOf("Thạc sĩ Sư phạm Toán - ĐH Khoa học Tự nhiên", "Chứng chỉ TESOL quốc tế"),
            achievements = listOf("Chiến sĩ thi đua cấp Thành phố 2023", "Bằng khen của Bộ Giáo dục")
        )
        isLoading = false
    }

    LaunchedEffect(selectedClassForGrades) {
        if (selectedClassForGrades != null) {
            isGradesLoading = true
            studentsInSelectedClass = listOf(
                Student(id = "SV01", name = "Lê Hoàng Long"),
                Student(id = "SV02", name = "Nguyễn Minh Anh"),
                Student(id = "SV03", name = "Trần Bảo Ngọc"),
                Student(id = "SV04", name = "Phạm Gia Huy")
            )
            gradesInSelectedClass = listOf(
                Grade(studentId = "SV01", average = 9.2),
                Grade(studentId = "SV02", average = 8.5),
                Grade(studentId = "SV03", average = 5.4),
                Grade(studentId = "SV04", average = 7.8)
            )
            isGradesLoading = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            PremiumDrawerContent(teacherName, teacherId) { 
                scope.launch { drawerState.close() } 
                if(it == "logout") {
                    navController.navigate("login") { popUpTo("teacher_dashboard") { inclusive = true } }
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                PremiumBottomBar(selectedTab) { selectedTab = it }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(listOf(BgGradientStart, BgGradientEnd))
            )) {
                AnimatedBackgroundElements()

                Column(modifier = Modifier.padding(padding)) {
                    PremiumTopAppBar(teacherName) { scope.launch { drawerState.open() } }
                    
                    AnimatedContent(
                        targetState = selectedTab,
                        label = "TabTransition",
                        transitionSpec = {
                            fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                        }
                    ) { tab ->
                        when (tab) {
                            0 -> HomeTab(teacherName, classes, notifications)
                            1 -> ClassesTab(classes)
                            2 -> GradesTab(classes, selectedClassForGrades, { selectedClassForGrades = it }, studentsInSelectedClass, gradesInSelectedClass, isGradesLoading)
                            3 -> ProfileTab(teacherData, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedBackgroundElements() {
    Canvas(modifier = Modifier.fillMaxSize().blur(80.dp)) {
        drawCircle(
            brush = Brush.radialGradient(listOf(SecondaryBlue.copy(alpha = 0.15f), Color.Transparent)),
            radius = 600f,
            center = Offset(size.width * 0.1f, size.height * 0.2f)
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(SoftIndigo.copy(alpha = 0.1f), Color.Transparent)),
            radius = 800f,
            center = Offset(size.width * 0.9f, size.height * 0.8f)
        )
    }
}

@Composable
fun PremiumTopAppBar(name: String, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.5f))
        ) {
            Icon(Icons.Default.Menu, contentDescription = null, tint = PrimaryBlue)
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("TeachFlow", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue.copy(alpha = 0.6f))
            Text("Teacher Hub", fontSize = 18.sp, fontWeight = FontWeight.Black, color = PrimaryBlue)
        }

        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(
                brush = Brush.linearGradient(listOf(SecondaryBlue, PrimaryBlue))
            ).padding(2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                Text(name.take(1), fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }
        }
    }
}

@Composable
fun HomeTab(name: String, classes: List<Class>, notifications: List<Notification>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column {
                Text("Chào buổi sáng,", fontSize = 16.sp, color = SurfaceDark.copy(alpha = 0.6f))
                Text(name, fontSize = 28.sp, fontWeight = FontWeight.Black, color = SurfaceDark)
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PremiumStatCard("Lớp dạy", classes.size.toString(), Icons.Default.List, SecondaryBlue, Modifier.weight(1f))
                PremiumStatCard("Học sinh", classes.sumOf { it.studentCount }.toString(), Icons.Default.Person, AccentTeal, Modifier.weight(1f))
            }
        }

        item {
            SectionHeader("Thông báo mới", "Xem tất cả")
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(notifications) { PremiumNotificationCard(it) }
            }
        }

        item {
            SectionHeader("Lịch dạy gần đây", "Lịch biểu")
        }

        items(classes.take(2)) { PremiumClassItem(it) }
    }
}

@Composable
fun PremiumStatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.shadow(20.dp, RoundedCornerShape(24.dp), ambientColor = color.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = SurfaceDark)
            Text(label, fontSize = 12.sp, color = SurfaceDark.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun PremiumNotificationCard(noti: Notification) {
    Card(
        modifier = Modifier.width(260.dp).height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(
                brush = Brush.radialGradient(listOf(PremiumGold.copy(alpha = 0.2f), Color.Transparent))
            ), contentAlignment = Alignment.Center) {
                Text(if(noti.type == "exam") "📝" else "🔔", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(noti.title, fontWeight = FontWeight.Bold, maxLines = 1, color = SurfaceDark)
                Text(noti.content, fontSize = 12.sp, color = SurfaceDark.copy(alpha = 0.6f), maxLines = 2)
            }
        }
    }
}

@Composable
fun PremiumClassItem(cls: Class) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(PrimaryBlue.copy(alpha = 0.05f)), contentAlignment = Alignment.Center) {
                Text(cls.name.take(1), fontWeight = FontWeight.Black, color = PrimaryBlue, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(cls.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SurfaceDark)
                Text(cls.subject, fontSize = 13.sp, color = PrimaryBlue)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(cls.room, fontWeight = FontWeight.Bold, color = SurfaceDark)
                Text(cls.schedule.split(" (").first(), fontSize = 11.sp, color = SurfaceDark.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun ClassesTab(classes: List<Class>) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("Lớp học phụ trách", fontSize = 24.sp, fontWeight = FontWeight.Black, color = SurfaceDark) }
        items(classes) { cls ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(16.dp)).background(SecondaryBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                            Text("🏫", fontSize = 30.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(cls.name, fontWeight = FontWeight.Black, fontSize = 22.sp)
                            Text(cls.subject, color = SecondaryBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        ClassDetailInfo("Sĩ số", cls.studentCount.toString(), Icons.Default.Person)
                        ClassDetailInfo("Phòng học", cls.room, Icons.Default.Place)
                        ClassDetailInfo("Thông tin", "Tiết 1-4", Icons.Default.Info)
                    }
                }
            }
        }
    }
}

@Composable
fun ClassDetailInfo(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue.copy(alpha = 0.4f), modifier = Modifier.size(20.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, fontSize = 10.sp, color = SurfaceDark.copy(alpha = 0.5f))
    }
}

@Composable
fun GradesTab(classes: List<Class>, selected: Class?, onSelect: (Class) -> Unit, students: List<Student>, grades: List<Grade>, loading: Boolean) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Bảng điểm & Đánh giá", fontSize = 24.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(classes) { cls ->
                val isSelected = selected?.id == cls.id
                Surface(
                    modifier = Modifier.clickable { onSelect(cls) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) PrimaryBlue else Color.White,
                    shadowElevation = if(isSelected) 8.dp else 0.dp
                ) {
                    Text(cls.name, modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), color = if (isSelected) Color.White else PrimaryBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (selected == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chọn một lớp để xem bảng điểm", color = SurfaceDark.copy(alpha = 0.4f))
            }
        } else if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryBlue) }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(students) { student ->
                    val score = grades.find { it.studentId == student.id }?.average ?: 0.0
                    GradeItem(student.name, student.id, score)
                }
            }
        }
    }
}

@Composable
fun GradeItem(name: String, id: String, score: Double) {
    val scoreColor = when {
        score >= 8.0 -> AccentTeal
        score >= 5.0 -> PremiumGold
        else -> Color.Red
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(scoreColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Text(name.take(1), fontWeight = FontWeight.Bold, color = scoreColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(id, fontSize = 11.sp, color = SurfaceDark.copy(alpha = 0.5f))
            }
            Text(score.toString(), fontSize = 20.sp, fontWeight = FontWeight.Black, color = scoreColor)
        }
    }
}

@Composable
fun ProfileTab(teacher: Teacher?, navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, contentPadding = PaddingValues(20.dp)) {
        item {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(modifier = Modifier.size(120.dp).shadow(20.dp, CircleShape), shape = CircleShape, color = Color.White) {
                    Box(contentAlignment = Alignment.Center) { Text("👩‍🏫", fontSize = 60.sp) }
                }
                Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = AccentTeal) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.padding(6.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(teacher?.name ?: "", fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(teacher?.position ?: "", color = PrimaryBlue, fontWeight = FontWeight.Bold)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            PremiumInfoSection("Thông tin cơ bản") {
                PremiumInfoRow(Icons.Default.AccountBox, "Mã số", teacher?.id ?: "")
                PremiumInfoRow(Icons.Default.Email, "Email", teacher?.email ?: "")
                PremiumInfoRow(Icons.Default.LocationOn, "Đơn vị", teacher?.schoolName ?: "")
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(20.dp))
            PremiumInfoSection("Học vấn & Thành tích") {
                teacher?.qualifications?.forEach { PremiumDetailRow("🎓", it) }
                teacher?.achievements?.forEach { PremiumDetailRow("🏆", it) }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate("login") { popUpTo("teacher_dashboard") { inclusive = true } }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng xuất", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PremiumInfoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue.copy(alpha = 0.5f), modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp), content = content)
        }
    }
}

@Composable
fun PremiumInfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 11.sp, color = SurfaceDark.copy(alpha = 0.5f))
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PremiumDetailRow(emoji: String, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SectionHeader(title: String, action: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Black, color = SurfaceDark)
        Text(action, fontSize = 13.sp, color = SecondaryBlue, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PremiumDrawerContent(name: String, id: String, onAction: (String) -> Unit) {
    ModalDrawerSheet(drawerContainerColor = Color.White) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(
            brush = Brush.verticalGradient(listOf(PrimaryBlue, SecondaryBlue))
        ), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.2f)) {
                    Box(contentAlignment = Alignment.Center) { Text("👩‍🏫", fontSize = 40.sp) }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(id, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        DrawerItem(Icons.Default.List, "Bảng điều khiển", true) { onAction("close") }
        DrawerItem(Icons.Default.Email, "Tin nhắn", false) { onAction("close") }
        DrawerItem(Icons.Default.Settings, "Cài đặt", false) { onAction("close") }
        HorizontalDivider(modifier = Modifier.padding(20.dp))
        DrawerItem(Icons.Default.ExitToApp, "Đăng xuất", false) { onAction("logout") }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if(selected) SecondaryBlue.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = if(selected) SecondaryBlue else SurfaceDark)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontWeight = if(selected) FontWeight.Bold else FontWeight.Medium, color = if(selected) SecondaryBlue else SurfaceDark)
    }
}

@Composable
fun PremiumBottomBar(selected: Int, onSelect: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        val items = listOf("Home", "Classes", "Grades", "Profile")
        val icons = listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.Star, Icons.Default.Person)
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selected == index,
                onClick = { onSelect(index) },
                icon = { Icon(icons[index], contentDescription = label) },
                label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = SurfaceDark.copy(alpha = 0.3f),
                    unselectedTextColor = SurfaceDark.copy(alpha = 0.3f),
                    indicatorColor = SecondaryBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}
