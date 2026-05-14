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
import kotlinx.coroutines.delay

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
            Class(id = "CL01", name = "Lớp 23SE1", subject = "Lập trình Android", room = "Phòng A.102", studentCount = 42, schedule = "Thứ 2 (7:30 - 11:30)"),
            Class(id = "CL02", name = "Lớp 22NS1", subject = "Cấu trúc dữ liệu", room = "Phòng B.305", studentCount = 35, schedule = "Thứ 4 (13:30 - 16:30)"),
            Class(id = "CL03", name = "Lớp 21DM1", subject = "Cơ sở dữ liệu", room = "Phòng C.201", studentCount = 40, schedule = "Thứ 6 (8:00 - 10:00)")
        )
        notifications = listOf(
            Notification(id = "1", title = "Họp chi bộ", content = "Nội dung chuẩn bị cho kỳ thi quốc gia.", type = "event"),
            Notification(id = "2", title = "Cập nhật điểm", content = "Vui lòng hoàn thành nhập điểm lớp 23SE1.", type = "exam"),
            Notification(id = "3", title = "Nghỉ lễ", content = "Thông báo nghỉ lễ Giỗ tổ Hùng Vương.", type = "info")
        )
        teacherData = Teacher(
            id = teacherId,
            name = teacherName,
            position = "Giảng viên khoa CNTT",
            schoolName = "Trường Đại học CNTT và TT Việt - Hàn",
            email = "tram.tnb@vku.udn.vn",
            qualifications = listOf("Tiến sĩ Khoa học máy tính", "Thạc sĩ Công nghệ phần mềm"),
            achievements = listOf("Giảng viên tiêu biểu năm 2023", "Bằng khen của Giám đốc Đại học Đà Nẵng")
        )
        isLoading = false
    }

    LaunchedEffect(selectedClassForGrades) {
        if (selectedClassForGrades != null) {
            isGradesLoading = true
            // Giả lập trễ mạng một chút cho chuyên nghiệp
            kotlinx.coroutines.delay(500) 
            
            when(selectedClassForGrades!!.id) {
                "CL01" -> { // 23SE1
                    studentsInSelectedClass = listOf(
                        Student(id = "SV101", name = "Lê Hoàng Long"),
                        Student(id = "SV102", name = "Nguyễn Minh Anh"),
                        Student(id = "SV103", name = "Trần Bảo Ngọc"),
                        Student(id = "SV104", name = "Phạm Gia Huy"),
                        Student(id = "SV105", name = "Vũ Hải Đăng")
                    )
                    gradesInSelectedClass = listOf(
                        Grade(studentId = "SV101", score15min = 10.0, score45min = 10.0, scoreMidterm = 9.5, scoreFinal = 9.7, average = 9.8),
                        Grade(studentId = "SV102", score15min = 9.0, score45min = 9.5, scoreMidterm = 9.0, scoreFinal = 9.3, average = 9.2),
                        Grade(studentId = "SV103", score15min = 8.0, score45min = 8.5, scoreMidterm = 8.5, scoreFinal = 9.0, average = 8.5),
                        Grade(studentId = "SV104", score15min = 7.0, score45min = 7.5, scoreMidterm = 8.0, scoreFinal = 8.7, average = 7.8),
                        Grade(studentId = "SV105", score15min = 10.0, score45min = 9.0, scoreMidterm = 9.5, scoreFinal = 9.5, average = 9.5)
                    )
                }
                "CL02" -> { // 22NS1
                    studentsInSelectedClass = listOf(
                        Student(id = "SV201", name = "Hoàng Thị Thùy"),
                        Student(id = "SV202", name = "Đặng Văn Lâm"),
                        Student(id = "SV203", name = "Bùi Tiến Dũng"),
                        Student(id = "SV204", name = "Nguyễn Quang Hải")
                    )
                    gradesInSelectedClass = listOf(
                        Grade(studentId = "SV201", score15min = 7.0, score45min = 7.5, scoreMidterm = 8.0, scoreFinal = 7.5, average = 7.5),
                        Grade(studentId = "SV202", score15min = 6.0, score45min = 7.0, scoreMidterm = 7.5, scoreFinal = 6.7, average = 6.8),
                        Grade(studentId = "SV203", score15min = 4.0, score45min = 4.0, scoreMidterm = 4.5, scoreFinal = 4.3, average = 4.2),
                        Grade(studentId = "SV204", score15min = 8.0, score45min = 8.0, scoreMidterm = 8.0, scoreFinal = 8.0, average = 8.0)
                    )
                }
                "CL03" -> { // 21DM1
                    studentsInSelectedClass = listOf(
                        Student(id = "SV301", name = "Phan Mạnh Quỳnh"),
                        Student(id = "SV302", name = "Sơn Tùng MTP"),
                        Student(id = "SV303", name = "Hồ Ngọc Hà")
                    )
                    gradesInSelectedClass = listOf(
                        Grade(studentId = "SV301", score15min = 5.0, score45min = 5.5, scoreMidterm = 6.0, scoreFinal = 5.5, average = 5.5),
                        Grade(studentId = "SV302", score15min = 10.0, score45min = 10.0, scoreMidterm = 10.0, scoreFinal = 10.0, average = 10.0),
                        Grade(studentId = "SV303", score15min = 8.5, score45min = 9.0, scoreMidterm = 8.5, scoreFinal = 9.2, average = 8.8)
                    )
                }
            }
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
                            2 -> GradesTab(
                                classes = classes,
                                selected = selectedClassForGrades,
                                onSelect = { selectedClassForGrades = it },
                                students = studentsInSelectedClass,
                                grades = gradesInSelectedClass,
                                loading = isGradesLoading,
                                onUpdateGrade = { updatedGrade ->
                                    // Cập nhật local state ngay lập tức
                                    gradesInSelectedClass = gradesInSelectedClass.map {
                                        if (it.studentId == updatedGrade.studentId) updatedGrade else it
                                    }
                                    Log.d("TEACHER_DASH", "✅ Đã cập nhật điểm cho student: ${updatedGrade.studentId}")
                                }
                            )
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
    var selectedClassForDetail by remember { mutableStateOf<Class?>(null) }
    var showDetailDialog by remember { mutableStateOf(false) }

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

        items(classes.take(2)) { cls ->
            PremiumClassItem(cls) {
                selectedClassForDetail = cls
                showDetailDialog = true
            }
        }
    }

    if (showDetailDialog && selectedClassForDetail != null) {
        ClassDetailDialog(
            classItem = selectedClassForDetail!!,
            onDismiss = { showDetailDialog = false }
        )
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
fun PremiumClassItem(cls: Class, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
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
fun GradesTab(
    classes: List<Class>,
    selected: Class?,
    onSelect: (Class) -> Unit,
    students: List<Student>,
    grades: List<Grade>,
    loading: Boolean,
    onUpdateGrade: (Grade) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editingGrade by remember { mutableStateOf<Grade?>(null) }
    var editingStudentName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Quản lý Bảng điểm", fontSize = 24.sp, fontWeight = FontWeight.Black)
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
            // Header bảng điểm
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Học sinh", modifier = Modifier.weight(1.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SurfaceDark.copy(alpha = 0.5f))
                Text("CC", modifier = Modifier.weight(0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SurfaceDark.copy(alpha = 0.5f))
                Text("BT", modifier = Modifier.weight(0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SurfaceDark.copy(alpha = 0.5f))
                Text("GK", modifier = Modifier.weight(0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SurfaceDark.copy(alpha = 0.5f))
                Text("CK", modifier = Modifier.weight(0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SurfaceDark.copy(alpha = 0.5f))
                Text("TB", modifier = Modifier.weight(0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(students) { student ->
                    val score = grades.find { it.studentId == student.id } ?: Grade(studentId = student.id)
                    GradeItem(student.name, score) {
                        editingGrade = score
                        editingStudentName = student.name
                        showEditDialog = true
                    }
                }
            }
        }
    }

    if (showEditDialog && editingGrade != null) {
        EditGradeDialog(
            studentName = editingStudentName,
            grade = editingGrade!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedGrade ->
                onUpdateGrade(updatedGrade)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun GradeItem(name: String, grade: Grade, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(grade.studentId, fontSize = 10.sp, color = SurfaceDark.copy(alpha = 0.5f))
            }
            Text(grade.score15min.toString(), modifier = Modifier.weight(0.5f), fontSize = 13.sp)
            Text(grade.score45min.toString(), modifier = Modifier.weight(0.5f), fontSize = 13.sp)
            Text(grade.scoreMidterm.toString(), modifier = Modifier.weight(0.5f), fontSize = 13.sp)
            Text(grade.scoreFinal.toString(), modifier = Modifier.weight(0.5f), fontSize = 13.sp)
            
            val avg = grade.average
            val scoreColor = when {
                avg >= 8.0 -> AccentTeal
                avg >= 5.0 -> PremiumGold
                else -> Color.Red
            }
            Text(avg.toString(), modifier = Modifier.weight(0.6f), fontSize = 15.sp, fontWeight = FontWeight.Black, color = scoreColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGradeDialog(studentName: String, grade: Grade, onDismiss: () -> Unit, onSave: (Grade) -> Unit) {
    var cc by remember { mutableStateOf(grade.score15min.toString()) }
    var bt by remember { mutableStateOf(grade.score45min.toString()) }
    var gk by remember { mutableStateOf(grade.scoreMidterm.toString()) }
    var ck by remember { mutableStateOf(grade.scoreFinal.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cập nhật điểm: $studentName", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GradeInputField("Chuyên cần (10%)", cc) { cc = it }
                GradeInputField("Bài tập (20%)", bt) { bt = it }
                GradeInputField("Giữa kỳ (30%)", gk) { gk = it }
                GradeInputField("Cuối kỳ (40%)", ck) { ck = it }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sCC = cc.toDoubleOrNull() ?: 0.0
                    val sBT = bt.toDoubleOrNull() ?: 0.0
                    val sGK = gk.toDoubleOrNull() ?: 0.0
                    val sCK = ck.toDoubleOrNull() ?: 0.0
                    val avg = (sCC + sBT + sGK + sCK) / 4.0
                    val formattedAvg = (avg * 10).toInt() / 10.0 // Làm tròn 1 chữ số
                    
                    onSave(grade.copy(
                        score15min = sCC,
                        score45min = sBT,
                        scoreMidterm = sGK,
                        scoreFinal = sCK,
                        average = formattedAvg
                    ))
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) { Text("Lưu thay đổi", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy", color = SurfaceDark.copy(alpha = 0.5f)) }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun GradeInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SurfaceDark.copy(alpha = 0.6f))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = PrimaryBlue,
                unfocusedIndicatorColor = SurfaceDark.copy(alpha = 0.1f)
            )
        )
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
fun ClassDetailDialog(classItem: Class, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(classItem.name, fontSize = 22.sp, fontWeight = FontWeight.Black, color = PrimaryBlue)
                Text(classItem.subject, fontSize = 14.sp, color = SecondaryBlue, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = PrimaryBlue.copy(alpha = 0.1f))
                
                DetailInfoItem("📍 Phòng học", classItem.room)
                DetailInfoItem("⏰ Thời gian", classItem.schedule)
                DetailInfoItem("👥 Sĩ số", "${classItem.studentCount} sinh viên")
                DetailInfoItem("📅 Học kỳ", "Học kỳ 1 - 2024")
                DetailInfoItem("🎓 Tín chỉ", "${if(classItem.credits > 0) classItem.credits else 3} tín chỉ")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = AccentTeal.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, null, tint = AccentTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Lớp học đang diễn ra bình thường", fontSize = 12.sp, color = AccentTeal, fontWeight = FontWeight.Medium)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đóng", color = Color.White)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
fun DetailInfoItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = SurfaceDark.copy(alpha = 0.6f))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SurfaceDark)
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
