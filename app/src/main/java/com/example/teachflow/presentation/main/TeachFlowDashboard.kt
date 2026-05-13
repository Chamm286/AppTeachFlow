package com.example.teachflow.presentation.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.util.Calendar

// Màu sắc TeachFlow
val PrimaryBlue = Color(0xFF1A73E8)
val PrimaryDark = Color(0xFF0D47A1)
val SecondaryGreen = Color(0xFF34A853)
val AccentOrange = Color(0xFFFBBC05)
val AccentRed = Color(0xFFEA4335)
val Purple = Color(0xFF9C27B0)
val BgLight = Color(0xFFF8F9FA)
val CardWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF202124)
val TextGray = Color(0xFF5F6368)

data class QuickAction(
    val icon: String,
    val title: String,
    val subtitle: String,
    val color: Color,
    val route: String
)

data class CourseItem(
    val id: String,
    val icon: String,
    val name: String,
    val students: Int,
    val color: Color
)

data class TeacherItem(
    val id: String,
    val name: String,
    val subject: String,
    val avatar: String,
    val rating: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachFlowDashboard(
    navController: NavController,
    userRole: String = "",
    userId: String = "",
    userName: String = ""
) {
    var currentBannerIndex by remember { mutableStateOf(0) }
    val banners = listOf(
        Triple("📚", "HỌC TẬP THÔNG MINH", "AI cá nhân hóa lộ trình học tập"),
        Triple("🏆", "THÀNH TÍCH VƯỢT TRỘI", "10,000+ học sinh đạt điểm cao"),
        Triple("🎯", "LỘ TRÌNH CÁ NHÂN", "Phù hợp với từng học sinh")
    )
    
    val quickActions = listOf(
        QuickAction("📝", "Nhập điểm", "Thêm điểm mới", PrimaryBlue, "grade_input"),
        QuickAction("📖", "Điểm danh", "Check-in học sinh", SecondaryGreen, "attendance"),
        QuickAction("📊", "Thống kê", "Báo cáo chi tiết", AccentOrange, "statistics"),
        QuickAction("🔔", "Thông báo", "Gửi thông báo", AccentRed, "notification")
    )
    
    val courses = listOf(
        CourseItem("1", "📐", "Toán 10A", 35, PrimaryBlue),
        CourseItem("2", "📖", "Văn 10B", 32, SecondaryGreen),
        CourseItem("3", "🔬", "Lý 11A", 28, AccentOrange),
        CourseItem("4", "🧪", "Hóa 11B", 25, Purple)
    )
    
    val teachers = listOf(
        TeacherItem("1", "Cô Nguyễn Thị A", "Toán", "👩‍🏫", 4.9f),
        TeacherItem("2", "Thầy Trần Văn B", "Văn", "👨‍🏫", 4.8f),
        TeacherItem("3", "Cô Lê Thị C", "Anh", "👩‍🏫", 4.9f)
    )
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentBannerIndex = (currentBannerIndex + 1) % banners.size
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("📚", fontSize = 24.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("TeachFlow", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Học tập thông minh", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue),
                actions = {
                    OutlinedButton(
                        onClick = { navController.navigate("login") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        shape = RoundedCornerShape(28.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(Icons.Outlined.Login, null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Đăng nhập", fontSize = 13.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { SearchBarTeachFlow(navController) }
            item { QuickActionsGrid(quickActions, navController) }
            item { BannerCarousel(banners, currentBannerIndex) }
            
            item {
                SectionHeader("🔥 KHÓA HỌC NỔI BẬT", "Xem thêm") { }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(courses) { course -> CourseCard(course) }
                }
            }
            
            item {
                SectionHeader("⭐ GIÁO VIÊN TIÊU BIỂU", "Xem tất cả") { }
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(teachers) { teacher -> TeacherCard(teacher, PrimaryBlue) }
                }
            }
            
            item { StudyTipCard() }
        }
    }
}

@Composable
fun SearchBarTeachFlow(navController: NavController) {
    val searchHints = listOf(
        "🔍 Tìm kiếm khóa học...",
        "📚 Tìm lớp học...",
        "👨‍🏫 Tìm giáo viên...",
        "📝 Tìm bài tập..."
    )
    var currentHint by remember { mutableStateOf(searchHints[0]) }
    
    LaunchedEffect(Unit) {
        var index = 0
        while (true) {
            delay(3000)
            index = (index + 1) % searchHints.size
            currentHint = searchHints[index]
        }
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp), color = CardWhite, shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { navController.navigate("search") }
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Search, "Tìm kiếm", tint = PrimaryBlue, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = currentHint, fontSize = 14.sp, color = TextGray, modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.Mic, "Tìm bằng giọng nói", tint = Color(0xFF9AA0A6), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun QuickActionsGrid(actions: List<QuickAction>, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(action = actions[0], modifier = Modifier.weight(1f)) { navController.navigate("grade_input") }
            QuickActionCard(action = actions[1], modifier = Modifier.weight(1f)) { navController.navigate("attendance") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(action = actions[2], modifier = Modifier.weight(1f)) { navController.navigate("statistics") }
            QuickActionCard(action = actions[3], modifier = Modifier.weight(1f)) { navController.navigate("notification") }
        }
    }
}

@Composable
fun QuickActionCard(action: QuickAction, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp), modifier = modifier.height(80.dp).clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(action.color.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Text(action.icon, fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(action.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark, maxLines = 1)
                Text(action.subtitle, fontSize = 11.sp, color = TextGray, maxLines = 1)
            }
        }
    }
}

@Composable
fun BannerCarousel(banners: List<Triple<String, String, String>>, currentIndex: Int) {
    val banner = banners[currentIndex]
    val colors = listOf(PrimaryBlue, SecondaryGreen, AccentOrange)
    val bannerColor = colors[currentIndex % colors.size]
    Card(
        shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(120.dp),
        colors = CardDefaults.cardColors(containerColor = bannerColor)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(banner.second, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(banner.third, fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                Spacer(modifier = Modifier.height(8.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = Color.White, modifier = Modifier.width(90.dp)) {
                    Text("Xem ngay", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = bannerColor, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
            Text(banner.first, fontSize = 48.sp)
        }
    }
}

@Composable
fun SectionHeader(title: String, actionText: String, onAction: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        TextButton(onClick = onAction) { Text(actionText, fontSize = 12.sp, color = PrimaryBlue) }
    }
}

@Composable
fun CourseCard(course: CourseItem) {
    Card(
        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier.width(140.dp).height(80.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(course.icon, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(course.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = course.color)
                Text("${course.students} học viên", fontSize = 10.sp, color = TextGray)
            }
        }
    }
}

@Composable
fun TeacherCard(teacher: TeacherItem, primaryColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.width(160.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(70.dp).clip(CircleShape).background(primaryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(teacher.avatar, fontSize = 36.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(teacher.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = TextDark)
            Text(teacher.subject, fontSize = 11.sp, color = primaryColor)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(if (index < teacher.rating.toInt()) Icons.Filled.Star else Icons.Outlined.Star, null, modifier = Modifier.size(12.dp), tint = AccentOrange)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("${teacher.rating}", fontSize = 11.sp, color = TextGray)
            }
        }
    }
}

@Composable
fun StudyTipCard() {
    val tips = listOf(
        TipItem(icon = "💡", title = "Pomodoro", subtitle = "Phương pháp học tập",
            description = "Học 25 phút, nghỉ 5 phút giúp tăng 40% năng suất học tập.",
            benefits = listOf("🎯 Tăng tập trung", "⚡ Giảm mệt mỏi", "📈 Hiệu quả cao"), color = PrimaryBlue),
        TipItem(icon = "📝", title = "Ghi chú thông minh", subtitle = "Take note hiệu quả",
            description = "Sử dụng sơ đồ tư duy và gạch đầu dòng để ghi nhớ tốt hơn.",
            benefits = listOf("🧠 Nhớ lâu hơn", "📚 Hệ thống kiến thức", "✍️ Dễ ôn tập"), color = SecondaryGreen),
        TipItem(icon = "🎯", title = "Mục tiêu SMART", subtitle = "Đặt mục tiêu học tập",
            description = "Cụ thể, Đo lường được, Khả thi, Thực tế, Có thời hạn.",
            benefits = listOf("🎯 Định hướng rõ", "📊 Dễ theo dõi", "🏆 Đạt kết quả"), color = AccentOrange)
    )
    
    var currentTipIndex by remember { mutableStateOf(0) }
    var isExpanded by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) { currentTipIndex = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % tips.size }
    val currentTip = tips[currentTipIndex]
    
    Card(
        shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(8.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(brush = Brush.verticalGradient(colors = listOf(currentTip.color.copy(alpha = 0.08f), CardWhite)))) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(brush = Brush.radialGradient(colors = listOf(currentTip.color, currentTip.color.copy(alpha = 0.7f)))),
                        contentAlignment = Alignment.Center) { Text(currentTip.icon, fontSize = 28.sp) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("✨ MẸO HỌC TẬP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = currentTip.color, letterSpacing = 1.sp)
                            Surface(shape = RoundedCornerShape(20.dp), color = currentTip.color.copy(alpha = 0.1f)) {
                                Text("${currentTipIndex + 1}/${tips.size}", fontSize = 9.sp, color = currentTip.color, modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp))
                            }
                        }
                        Text(currentTip.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(currentTip.subtitle, fontSize = 12.sp, color = TextGray)
                    }
                    Surface(shape = CircleShape, color = Color(0xFFF5F5F5), modifier = Modifier.size(36.dp).clickable { isExpanded = !isExpanded }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(if (isExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown, null, tint = currentTip.color, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(currentTip.description, fontSize = 13.sp, color = TextGray, lineHeight = 20.sp)
                
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🎯 LỢI ÍCH", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = currentTip.color)
                    Spacer(modifier = Modifier.height(12.dp))
                    currentTip.benefits.forEach { benefit ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(currentTip.color))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(benefit, fontSize = 13.sp, color = TextGray)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = RoundedCornerShape(30.dp), color = Color(0xFFF5F5F5), modifier = Modifier.weight(1f).height(44.dp).clickable { isExpanded = !isExpanded }) {
                        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Info, null, modifier = Modifier.size(18.dp), tint = currentTip.color)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isExpanded) "Thu gọn" else "Xem thêm", fontSize = 13.sp, color = currentTip.color, fontWeight = FontWeight.Medium)
                        }
                    }
                    Surface(shape = RoundedCornerShape(30.dp), color = currentTip.color, modifier = Modifier.weight(1f).height(44.dp).clickable {
                        currentTipIndex = (currentTipIndex + 1) % tips.size; isExpanded = false
                    }) {
                        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Refresh, null, modifier = Modifier.size(18.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mẹo khác", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

data class TipItem(
    val icon: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val benefits: List<String>,
    val color: Color
)

