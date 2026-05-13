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
import com.example.teachflow.presentation.student.viewmodel.StudentUiState
import com.example.teachflow.presentation.theme.*

// Hằng số màu
val TeachFlowWarning = Color(0xFFFBBC05)
val TeachFlowError = Color(0xFFEA4335)
val TeachFlowBlue = Color(0xFF1A73E8)
val TeachFlowSuccess = Color(0xFF34A853)
val TeachFlowText = Color(0xFF202124)
val TeachFlowTextSecondary = Color(0xFF5F6368)
val BgLightGray = Color(0xFFF8FAFC)
val CardWhite = Color(0xFFFFFFFF)

@Composable
fun StudentDashboardScreen(
    navController: NavController,
    studentId: String,
    studentName: String,
    viewModel: com.example.teachflow.presentation.student.viewmodel.StudentViewModel
) {
    val state by viewModel.state.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(BgLightGray)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Header Section với Gradient nhô lên
            item {
                StudentHeader(studentName)
            }

            // 2. Stats Section - Hiệu ứng thẻ nổi (offset)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-30).dp), // Tạo hiệu ứng nhô lên đè vào Header
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StudentStatCard(
                        modifier = Modifier.weight(1f),
                        value = "8.5",
                        label = "GPA",
                        icon = "📈",
                        color = TeachFlowSuccess
                    )
                    StudentStatCard(
                        modifier = Modifier.weight(1f),
                        value = "12",
                        label = "Môn học",
                        icon = "📚",
                        color = TeachFlowBlue
                    )
                    StudentStatCard(
                        modifier = Modifier.weight(1f),
                        value = "05",
                        label = "Huy hiệu",
                        icon = "🏆",
                        color = TeachFlowWarning
                    )
                }
            }

            // 3. Section: Lịch học hôm nay & Điểm danh
            item {
                // Thêm spacer để bù lại khoảng offset (-30dp) của StatCard
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("📅 Lịch học hôm nay", onSeeAllClick = {
                    // navController.navigate("schedule_screen")
                })
            }

            // Dữ liệu mẫu cho Lịch học & Điểm danh
            val todaySchedule = listOf(
                ScheduleData("Toán Giải Tích", "Ca 1 (07:00 - 08:45)", "Phòng 301", "Thầy Nguyễn Văn A", AttendanceStatus.PRESENT),
                ScheduleData("Lập trình Android", "Ca 2 (09:00 - 10:45)", "Phòng 205", "Cô Trần Thị B", AttendanceStatus.LATE),
                ScheduleData("Tiếng Anh CN", "Ca 4 (13:00 - 14:45)", "Phòng 102", "Cô Lê C", AttendanceStatus.PENDING)
            )

            items(todaySchedule) { schedule ->
                ScheduleCardEnhanced(schedule)
            }

            // ==========================================
            // 4. Section: Khóa học của tôi (Nằm ngay dưới Lịch học)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Khi nhấn "Xem tất cả", mình đảo ngược trạng thái của biến isExpanded
                SectionHeader(
                    title = "📚 Khóa học của tôi",
                    onSeeAllClick = { isExpanded = !isExpanded } // Nhấn để hiện thêm hoặc thu gọn
                )
            }

            // Xử lý logic hiển thị: Nếu chưa nhấn "Xem tất cả" thì chỉ hiện 2 khóa đầu tiên
            val displayCourses = if (isExpanded) {
                state.classes // Hiện toàn bộ từ Firebase
            } else {
                state.classes.take(2) // Chỉ hiện 2 cái cho gọn dashboard
            }

            items(displayCourses) { course ->
                val uiData = EnrolledCourseData(
                    id = course.id,
                    courseName = course.name,
                    teacherName = course.teacherName,
                    progress = 0.5f,
                    icon = "📘",
                    bgColor = Color(0xFFE0F2FE),
                    accentColor = Color(0xFF0284C7)
                )

                EnrolledCourseCard(course = uiData, onClick = {
                    // Tạm thời mình chỉ Log ra hoặc thông báo, không navigate để tránh văng app
                    println("Click vào khóa học: ${course.name}")
                })
            }

            // Thêm một nút bấm nhỏ ở cuối nếu danh sách quá dài (Tùy chọn)
            if (state.classes.size > 2) {
                item {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isExpanded) "Thu gọn bớt" else "Xem thêm ${state.classes.size - 2} khóa học")
                    }
                }
            }
        }
    }
}

@Composable
fun StudentHeader(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TeachFlowSuccess, TeachFlowSuccess.copy(alpha = 0.8f))
                ),
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
                Text(
                    "Chào mừng trở lại,",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = CircleShape
                ) {
                    Text(
                        "Lớp 12A1 • Học sinh xuất sắc",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }

            // Avatar & Notification
            Box(contentAlignment = Alignment.TopEnd) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("👨‍🎓", fontSize = 32.sp)
                    }
                }
                Surface(
                    modifier = Modifier.size(14.dp),
                    shape = CircleShape,
                    color = TeachFlowError,
                    border = BorderStroke(2.dp, TeachFlowSuccess)
                ) {}
            }
        }
    }
}

@Composable
fun StudentStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TeachFlowText
            )
            Text(
                label,
                fontSize = 12.sp,
                color = TeachFlowTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TeachFlowText)
        Text(
            "Xem tất cả",
            fontSize = 13.sp,
            color = TeachFlowBlue,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }
}

// ==========================================
// THÀNH PHẦN MỚI: LỊCH HỌC & ĐIỂM DANH
// ==========================================

// 1. Data Classes
enum class AttendanceStatus { PRESENT, LATE, ABSENT, PENDING }

data class ScheduleData(
    val subject: String,
    val time: String,
    val room: String,
    val teacherName: String,
    val status: AttendanceStatus
)

// 2. Schedule Card Composable
@Composable
fun ScheduleCardEnhanced(data: ScheduleData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { /* TODO: Mở chi tiết lớp học */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon môn học (Dùng emoji hoặc vector tùy thích)
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = TeachFlowBlue.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = TeachFlowBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Thông tin lớp học
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

            // Tag điểm danh
            AttendanceStatusTag(data.status)
        }
    }
}

// 3. Attendance Status Tag
@Composable
fun AttendanceStatusTag(status: AttendanceStatus) {
    val (text, bgColor, textColor) = when (status) {
        AttendanceStatus.PRESENT -> Triple("CÓ MẶT", Color(0xFFDCFCE7), Color(0xFF16A34A)) // Xanh lá
        AttendanceStatus.LATE -> Triple("ĐI TRỄ", Color(0xFFFEF9C3), Color(0xFFCA8A04))    // Vàng cam
        AttendanceStatus.ABSENT -> Triple("VẮNG MẶT", Color(0xFFFEE2E2), Color(0xFFDC2626))  // Đỏ
        AttendanceStatus.PENDING -> Triple("CHỜ ĐIỂM DANH", Color(0xFFF1F5F9), Color(0xFF64748B)) // Xám
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
} // <--- ĐÃ SỬA: Đóng hàm AttendanceStatusTag tại đây

// ==========================================
// THÀNH PHẦN MỚI: KHÓA HỌC ĐÃ ĐĂNG KÝ
// ==========================================

data class EnrolledCourseData(
    val id: String,
    val courseName: String,
    val teacherName: String,
    val progress: Float, // Tiến độ từ 0.0f đến 1.0f
    val icon: String,
    val bgColor: Color,
    val accentColor: Color
)

@Composable
fun EnrolledCourseCard(course: EnrolledCourseData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon khóa học (Có background màu nhạt)
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = course.bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(course.icon, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin và Thanh tiến độ
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = course.courseName,
                    fontWeight = FontWeight.Bold,
                    color = TeachFlowText,
                    fontSize = 15.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Giảng viên: ${course.teacherName}",
                    fontSize = 12.sp,
                    color = TeachFlowTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Thanh Progress Bar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = course.progress,
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = course.accentColor,
                        trackColor = course.accentColor.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${(course.progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = course.accentColor
                    )
                }
            }
        }
    }
}