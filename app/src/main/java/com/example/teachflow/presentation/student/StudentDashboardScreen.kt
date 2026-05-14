package com.example.teachflow.presentation.student

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.presentation.student.viewmodel.StudentViewModel
// Nếu team có file theme riêng, giữ lại các import theme của anh ở đây
// import com.example.teachflow.presentation.theme.*

// --- HẰNG SỐ MÀU SẮC ---
val TeachFlowBlue = Color(0xFF1A73E8)
val TeachFlowSuccess = Color(0xFF34A853)
val TeachFlowWarning = Color(0xFFFBBC05)
val TeachFlowError = Color(0xFFEA4335)
val TeachFlowText = Color(0xFF202124)
val TeachFlowTextSecondary = Color(0xFF5F6368)
val BgLightGray = Color(0xFFF8FAFC)
val CardWhite = Color(0xFFFFFFFF)

@Composable
fun StudentDashboardScreen(
    navController: NavController,
    studentId: String,
    studentName: String,
    viewModel: StudentViewModel // Bắt buộc truyền ViewModel để lấy data Firebase
) {
    // 1. Lấy state từ ViewModel
    val mockCourses = listOf(
        EnrolledCourseData("CL01", "Lập trình Android nâng cao", "Thầy Nguyễn Văn A", 0.85f, "📱", Color(0xFFE0F2FE), Color(0xFF0284C7)),
        EnrolledCourseData("CL02", "Cơ sở dữ liệu NoSQL", "Cô Nguyễn Thị B", 0.45f, "💾", Color(0xFFF0FDF4), Color(0xFF16A34A)),
        EnrolledCourseData("CL03", "Thiết kế UI/UX với Figma", "Thầy Trần Văn C", 0.60f, "🎨", Color(0xFFFFF7ED), Color(0xFFEA580C)),
        EnrolledCourseData("CL04", "Kỹ năng mềm cho Dev", "Thầy Trần Anh D", 0.20f, "🤝", Color(0xFFFEF2F2), Color(0xFFDC2626))
    )
    var isExpanded by remember { mutableStateOf(false) }

    // DÙNG SCAFFOLD ĐỂ THÊM NÚT NHẮN TIN LƠ LỬNG
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("student_chat_list") },
                containerColor = TeachFlowBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Chat, contentDescription = "Nhắn tin cho giáo viên")
            }
        }
    ) { paddingValues ->
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(BgLightGray)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // --- PHẦN 1: HEADER & STATS ---
            item {
                StudentHeader(studentName)
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-30).dp), // Tạo hiệu ứng nhô lên
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StudentStatCard(Modifier.weight(1f), "8.5", "GPA", "📈", TeachFlowSuccess)
                    StudentStatCard(Modifier.weight(1f), mockCourses.size.toString(), "Môn học", "📚", TeachFlowBlue)
                    StudentStatCard(Modifier.weight(1f), "05", "Huy hiệu", "🏆", TeachFlowWarning)
                }
            }

            // --- PHẦN 2: LỊCH HỌC HÔM NAY & ĐIỂM DANH ---
            item {
                Spacer(modifier = Modifier.height(8.dp)) // Bù lại khoảng offset của Stats
                SectionHeader("📅 Lịch học hôm nay", onSeeAllClick = { })
            }

            val todaySchedule = listOf(
                ScheduleData("Toán Giải Tích", "Ca 1 (07:00 - 08:45)", "Phòng 301", "Thầy Nguyễn Văn A", AttendanceStatus.PRESENT),
                ScheduleData("Lập trình Android", "Ca 2 (09:00 - 10:45)", "Phòng 205", "Cô Trần Thị B", AttendanceStatus.LATE),
                ScheduleData("Tiếng Anh CN", "Ca 4 (13:00 - 14:45)", "Phòng 102", "Cô Lê C", AttendanceStatus.PENDING)
            )

            items(todaySchedule) { schedule ->
                ScheduleCardEnhanced(schedule)
            }

            // --- PHẦN 3: KHÓA HỌC CỦA TÔI (FIREBASE) ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "📚 Khóa học của tôi",
                    onSeeAllClick = { isExpanded = !isExpanded }
                )
            }

            // Logic hiển thị: Nếu chưa nhấn Xem tất cả thì chỉ hiện 2 khóa
            val displayCourses = if (isExpanded) mockCourses else mockCourses.take(2)

            items(displayCourses) { uiData ->
                EnrolledCourseCard(course = uiData, onClick = {
                    // navController.navigate("course_detail/${uiData.id}")
                })
            }

            if (mockCourses.size > 2) {
                item {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isExpanded) "Thu gọn bớt" else "Xem thêm ${mockCourses.size - 2} khóa học")
                    }
                }
            }

            // --- PHẦN 4: ĐIỂM SỐ GẦN ĐÂY ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("📊 Điểm số gần đây", onSeeAllClick = {})
            }

            items(listOf(
                GradeData("Toán học", 8.5, "15/05", Color(0xFFE0F2FE), Color(0xFF0284C7)),
                GradeData("Ngữ văn", 7.8, "14/05", Color(0xFFFEF2F2), Color(0xFFDC2626)),
                GradeData("Vật lý", 9.0, "12/05", Color(0xFFF0FDF4), Color(0xFF16A34A))
            )) { data ->
                GradeCardEnhanced(data)
            }

            // --- PHẦN 5: NHIỆM VỤ SẮP TỚI ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("📋 Nhiệm vụ sắp tới", onSeeAllClick = {})
            }

            items(listOf(
                TaskData("Bài tập Toán - Chương 1", "Hôm nay, 23:59", "BÀI TẬP", Color(0xFFEEF2FF), Color(0xFF4F46E5)),
                TaskData("Soạn văn - Bài thơ mới", "Ngày mai", "TỰ HỌC", Color(0xFFF5F3FF), Color(0xFF7C3AED))
            )) { task ->
                TaskCardEnhanced(task)}
            }
        }
    }
}

// ==========================================
// CÁC COMPOSABLE GIAO DIỆN HỖ TRỢ
// ==========================================

@Composable
fun StudentHeader(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(listOf(TeachFlowSuccess, TeachFlowSuccess.copy(alpha = 0.8f))),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text("Chào mừng trở lại,", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                Text(name, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(color = Color.White.copy(alpha = 0.2f), shape = CircleShape) {
                    Text("Lớp 12A1 • Học sinh xuất sắc", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 11.sp, color = Color.White)
                }
            }
            Box(contentAlignment = Alignment.TopEnd) {
                Surface(modifier = Modifier.size(56.dp), shape = CircleShape, color = Color.White, shadowElevation = 8.dp) {
                    Box(contentAlignment = Alignment.Center) { Text("👨‍🎓", fontSize = 32.sp) }
                }
                Surface(modifier = Modifier.size(14.dp), shape = CircleShape, color = TeachFlowError, border = BorderStroke(2.dp, TeachFlowSuccess)) {}
            }
        }
    }
}

@Composable
fun StudentStatCard(modifier: Modifier = Modifier, value: String, label: String, icon: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TeachFlowText)
            Text(label, fontSize = 12.sp, color = TeachFlowTextSecondary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TeachFlowText)
        Text("Xem tất cả", fontSize = 13.sp, color = TeachFlowBlue, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { onSeeAllClick() })
    }
}

// --- Lịch học & Điểm danh ---
@Composable
fun ScheduleCardEnhanced(data: ScheduleData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp), color = TeachFlowBlue.copy(alpha = 0.1f)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.MenuBook, contentDescription = null, tint = TeachFlowBlue, modifier = Modifier.size(24.dp)) }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(data.subject, fontWeight = FontWeight.Bold, color = TeachFlowText, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, tint = TeachFlowTextSecondary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(data.time, fontSize = 12.sp, color = TeachFlowTextSecondary)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Room, null, tint = TeachFlowTextSecondary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${data.room} • ${data.teacherName}", fontSize = 12.sp, color = TeachFlowTextSecondary)
                }
            }
            AttendanceStatusTag(data.status)
        }
    }
}

@Composable
fun AttendanceStatusTag(status: AttendanceStatus) {
    val (text, bgColor, textColor) = when (status) {
        AttendanceStatus.PRESENT -> Triple("CÓ MẶT", Color(0xFFDCFCE7), Color(0xFF16A34A))
        AttendanceStatus.LATE -> Triple("ĐI TRỄ", Color(0xFFFEF9C3), Color(0xFFCA8A04))
        AttendanceStatus.ABSENT -> Triple("VẮNG MẶT", Color(0xFFFEE2E2), Color(0xFFDC2626))
        AttendanceStatus.PENDING -> Triple("CHỜ ĐIỂM DANH", Color(0xFFF1F5F9), Color(0xFF64748B))
    }
    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
        Text(text = text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor)
    }
}

// --- Khóa học của tôi (Firebase) ---
@Composable
fun EnrolledCourseCard(course: EnrolledCourseData, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(14.dp), color = course.bgColor) {
                Box(contentAlignment = Alignment.Center) { Text(course.icon, fontSize = 24.sp) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = course.courseName, fontWeight = FontWeight.Bold, color = TeachFlowText, fontSize = 15.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Giảng viên: ${course.teacherName}", fontSize = 12.sp, color = TeachFlowTextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = course.progress,
                        modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = course.accentColor, trackColor = course.accentColor.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${(course.progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = course.accentColor)
                }
            }
        }
    }
}

// --- Điểm số gần đây ---
@Composable
fun GradeCardEnhanced(data: GradeData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp), color = data.bgColor) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.MenuBook, null, tint = data.accentColor, modifier = Modifier.size(24.dp)) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(data.subject, fontWeight = FontWeight.Bold, color = TeachFlowText, fontSize = 15.sp)
                Text(data.date, fontSize = 12.sp, color = TeachFlowTextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(String.format("%.1f", data.score), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = data.accentColor)
                Text("Điểm số", fontSize = 10.sp, color = TeachFlowTextSecondary)
            }
        }
    }
}

// --- Nhiệm vụ sắp tới ---
@Composable
fun TaskCardEnhanced(task: TaskData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = task.bgColor) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Assignment, null, tint = task.accentColor, modifier = Modifier.size(20.dp)) }
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(task.title, fontWeight = FontWeight.Bold, color = TeachFlowText, fontSize = 14.sp)
                }
                Text(task.deadline, fontSize = 12.sp, color = TeachFlowTextSecondary)
            }
            Surface(color = task.bgColor, shape = RoundedCornerShape(8.dp)) {
                Text(task.tag, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = task.accentColor)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

// ==========================================
// DATA MODELS
// ==========================================
enum class AttendanceStatus { PRESENT, LATE, ABSENT, PENDING }
data class ScheduleData(val subject: String, val time: String, val room: String, val teacherName: String, val status: AttendanceStatus)
data class EnrolledCourseData(val id: String, val courseName: String, val teacherName: String, val progress: Float, val icon: String, val bgColor: Color, val accentColor: Color)
data class GradeData(val subject: String, val score: Double, val date: String, val bgColor: Color, val accentColor: Color)
data class TaskData(val title: String, val deadline: String, val tag: String, val bgColor: Color, val accentColor: Color)