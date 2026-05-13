package com.example.teachflow.presentation.admin.tabs

import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Course
import com.example.teachflow.data.model.Student
import com.example.teachflow.data.model.Teacher
import com.example.teachflow.presentation.admin.PurpleAccent
import com.example.teachflow.presentation.admin.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data classes
data class QuickActionItem(
    val icon: String,
    val title: String,
    val subtitle: String,
    val color: Color,
    val route: String
)

data class CourseItem(
    val id: String,
    val title: String,
    val instructor: String,
    val students: Int,
    val rating: Double,
    val price: String,
    val color: Color
)

data class ContestItem(
    val icon: String,
    val title: String,
    val date: String,
    val description: String,
    val color: Color
)

data class NotificationData(
    val icon: String,
    val title: String,
    val content: String,
    val time: String
)

@Composable
fun HomeTab(padding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var currentBannerIndex by remember { mutableStateOf(0) }

    // State cho dữ liệu từ Firebase
    var isLoading by remember { mutableStateOf(true) }
    var totalStudents by remember { mutableStateOf(0) }
    var totalTeachers by remember { mutableStateOf(0) }
    var totalClasses by remember { mutableStateOf(0) }
    var totalRevenue by remember { mutableStateOf(0.0) }
    var topCourses by remember { mutableStateOf<List<CourseItem>>(emptyList()) }

    // Banner data
    val banners = listOf(
        Triple("🎓", "KHAI GIẢNG KHÓA HỌC MỚI", "Ưu đãi lên đến 50%"),
        Triple("🏆", "CUỘC THI LẬP TRÌNH", "Tổng giải thưởng 100 triệu"),
        Triple("📚", "THƯ VIỆN KHÓA HỌC", "Hơn 500+ khóa học chất lượng")
    )

    // Colors cho banner
    val bannerColors = listOf(
        Color(0xFF0F4C81),  // Navy
        Color(0xFF1A5BA8),  // Navy Light
        Color(0xFF0A3A62)   // Navy Dark
    )

    // Quick Actions
    val quickActions = listOf(
        QuickActionItem("👥", "Quản lý\nngười dùng", "Thêm/sửa/xóa", NavyPrimary, "users"),
        QuickActionItem("📚", "Quản lý\nkhóa học", "Tạo/sửa/xóa", SuccessGreen, "courses"),
        QuickActionItem("💰", "Doanh thu", "Thống kê", WarningOrange, "revenue"),
        QuickActionItem("📊", "Báo cáo", "Xuất báo cáo", PurpleAccent, "reports")
    )

    // Contests mock data
    val contests = listOf(
        ContestItem("🏆", "Olympic Tin học 2026", "15/05/2026", "Đăng ký đến 10/05", NavyPrimary),
        ContestItem("💻", "Hackathon Sáng tạo", "20/05/2025", "Giải thưởng 50tr", SuccessGreen),
        ContestItem("🚀", "Code Challenge", "25/05/2025", "Thử thách 24h", WarningOrange)
    )

    // Notifications mock data
    val notifications = listOf(
        NotificationData("🎉", "Chào mừng Admin mới", "Hệ thống TeachFlow chính thức ra mắt", "5 phút trước"),
        NotificationData("📢", "Cập nhật hệ thống", "Phiên bản mới 2.0 đã có", "1 giờ trước"),
        NotificationData("💰", "Doanh thu tháng 5", "Đạt 245 triệu đồng", "2 giờ trước")
    )

    // Load dữ liệu từ Firebase
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            // Lấy số liệu thống kê
            val students = RepoHolder.repo.getAllStudents()
            totalStudents = students.size

            val teachers = RepoHolder.repo.getAllTeachers()
            totalTeachers = teachers.size

            val classes = RepoHolder.repo.getAllClasses()
            totalClasses = classes.size

            // Mock doanh thu (sau này lấy từ collection payments)
            totalRevenue = 245_000_000.0

            // Lấy danh sách khóa học từ Firebase
            val courses = RepoHolder.repo.getAllCourses()
            topCourses = courses.take(3).mapIndexed { index, course ->
                val colors = listOf(NavyPrimary, SuccessGreen, WarningOrange)
                CourseItem(
                    id = course.id,
                    title = course.name,
                    instructor = course.teacherName ?: "Giảng viên",
                    students = course.enrolledCount ?: 0,
                    rating = 4.8 + (index * 0.05),
                    price = formatMoney(course.price ?: 0.0),
                    color = colors[index % colors.size]
                )
            }

            // Nếu không có khóa học, dùng mock data
            if (topCourses.isEmpty()) {
                topCourses = listOf(
                    CourseItem("1", "Lập trình Android nâng cao", "Trần Nguyễn Bảo Trâm", 156, 4.9, "1.2M", NavyPrimary),
                    CourseItem("2", "Kotlin & Coroutines", "Lê Minh Tuấn", 89, 4.8, "890K", SuccessGreen),
                    CourseItem("3", "Firebase toàn tập", "Phạm Thị Hồng", 234, 4.9, "1.5M", WarningOrange)
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            // Dùng mock data nếu lỗi
            totalStudents = 1254
            totalTeachers = 48
            totalClasses = 32
            totalRevenue = 245_000_000.0
            topCourses = listOf(
                CourseItem("1", "Lập trình Android nâng cao", "Trần Nguyễn Bảo Trâm", 156, 4.9, "1.2M", NavyPrimary),
                CourseItem("2", "Kotlin & Coroutines", "Lê Minh Tuấn", 89, 4.8, "890K", SuccessGreen),
                CourseItem("3", "Firebase toàn tập", "Phạm Thị Hồng", 234, 4.9, "1.5M", WarningOrange)
            )
        } finally {
            isLoading = false
        }
    }

    // Animation cho banner
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentBannerIndex = (currentBannerIndex + 1) % banners.size
        }
    }

    // Stats data
    val stats = listOf(
        StatItem("👥", formatNumber(totalStudents), "Học sinh", "+12%", SuccessGreen),
        StatItem("👩‍🏫", totalTeachers.toString(), "Giáo viên", "+5%", NavyPrimary),
        StatItem("📚", totalClasses.toString(), "Lớp học", "+8%", WarningOrange),
        StatItem("💰", formatMoney(totalRevenue), "Doanh thu", "+18%", PurpleAccent)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Banner
        item {
            WelcomeCard()
        }

        // Loading indicator
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NavyPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Đang tải dữ liệu...", fontSize = 12.sp, color = TextGray)
                    }
                }
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                stats.forEach { stat ->
                    StatCard(stat, Modifier.weight(1f))
                }
            }
        }

        // Banner Carousel
        item {
            BannerCarousel(banners, bannerColors, currentBannerIndex)
        }

        // Quick Actions
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text("⚡ Tác vụ nhanh", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    quickActions.take(2).forEach { action ->
                        QuickActionCard(action, Modifier.weight(1f), navController)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    quickActions.drop(2).forEach { action ->
                        QuickActionCard(action, Modifier.weight(1f), navController)
                    }
                }
            }
        }

        // Top Courses Section
        item {
            SectionHeaderWithAction("🔥 Khóa học nổi bật", "Xem tất cả") { }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(topCourses) { course ->
                    CourseCard(course)
                }
            }
        }

        // Upcoming Contests
        item {
            SectionHeaderWithAction("🏆 Cuộc thi sắp diễn ra", "Xem tất cả") { }
        }

        items(contests) { contest ->
            ContestCard(contest)
        }

        // Recent Notifications
        item {
            SectionHeaderWithAction("🔔 Thông báo mới", "Xem tất cả") { }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    notifications.forEach { noti ->
                        NotificationRow(noti)
                        if (noti != notifications.last()) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NavyPrimary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("✨ Chào mừng trở lại,", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                Text("Quản trị viên", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Hôm nay là một ngày tuyệt vời!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
            }
            Surface(
                shape = CircleShape,
                color = NavyPrimary,
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("👨‍💼", fontSize = 32.sp)
                }
            }
        }
    }
}

@Composable
fun StatCard(stat: StatItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = stat.color.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(stat.icon, fontSize = 20.sp)
                    }
                }
                Text(
                    stat.change,
                    fontSize = 11.sp,
                    color = if (stat.change.contains("+")) SuccessGreen else ErrorRed
                )
            }
            Column {
                Text(stat.value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = stat.color)
                Text(stat.title, fontSize = 11.sp, color = TextGray)
            }
        }
    }
}

@Composable
fun BannerCarousel(banners: List<Triple<String, String, String>>, colors: List<Color>, currentIndex: Int) {
    val banner = banners[currentIndex]
    val bannerColor = colors[currentIndex % colors.size]

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bannerColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(banner.second, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(banner.third, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    modifier = Modifier.width(90.dp)
                ) {
                    Text(
                        "Xem ngay",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = bannerColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            Text(banner.first, fontSize = 48.sp)
        }
    }
}

@Composable
fun QuickActionCard(action: QuickActionItem, modifier: Modifier = Modifier, navController: NavController) {
    Card(
        modifier = modifier.height(90.dp).clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = action.color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(action.icon, fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(action.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark, lineHeight = 18.sp)
                Text(action.subtitle, fontSize = 10.sp, color = TextGray)
            }
        }
    }
}

@Composable
fun CourseCard(course: CourseItem) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = course.color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📱", fontSize = 24.sp)
                }
            }
            Text(course.title, fontWeight = FontWeight.SemiBold, color = TextDark, fontSize = 14.sp, maxLines = 2)
            Text(course.instructor, fontSize = 11.sp, color = TextGray, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        if (index < course.rating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                        null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFFFFB800)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("${course.rating}", fontSize = 11.sp, color = TextGray)
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = course.color.copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "💰 ${course.price}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = course.color,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = course.color),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Xem chi tiết", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun ContestCard(contest: ContestItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = contest.color.copy(alpha = 0.1f),
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(contest.icon, fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(contest.title, fontWeight = FontWeight.SemiBold, color = TextDark, fontSize = 14.sp)
                Text(contest.date, fontSize = 11.sp, color = TextGray)
                Text(contest.description, fontSize = 10.sp, color = contest.color)
            }
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = contest.color),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đăng ký", fontSize = 11.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun NotificationRow(notification: NotificationData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(notification.icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(notification.title, fontWeight = FontWeight.Medium, color = TextDark, fontSize = 13.sp)
            Text(notification.content, fontSize = 11.sp, color = TextGray, maxLines = 1)
            Text(notification.time, fontSize = 9.sp, color = TextLight)
        }
        Icon(Icons.Outlined.ChevronRight, null, tint = TextLight)
    }
}

@Composable
fun SectionHeaderWithAction(title: String, actionText: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        TextButton(onClick = onAction) {
            Text(actionText, fontSize = 12.sp, color = NavyPrimary)
        }
    }
}

// Helper functions
fun formatNumber(number: Int): String {
    return if (number >= 1000) String.format("%.1fK", number / 1000.0) else number.toString()
}

fun formatMoney(amount: Double): String {
    return if (amount >= 1_000_000) String.format("%.1fM", amount / 1_000_000) else amount.toInt().toString()
}

data class StatItem(
    val icon: String,
    val value: String,
    val title: String,
    val change: String,
    val color: Color
)