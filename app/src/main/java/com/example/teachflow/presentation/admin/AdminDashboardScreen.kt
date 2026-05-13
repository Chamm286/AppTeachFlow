package com.example.teachflow.presentation.admin

import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.presentation.admin.components.AdminBottomNav
import com.example.teachflow.presentation.admin.tabs.*
import kotlinx.coroutines.launch

// Màu sắc chính - Xanh Navy
val NavyPrimary = Color(0xFF0F4C81)
val NavyDark = Color(0xFF0A3A62)
val NavyLight = Color(0xFF1A5BA8)
val SuccessGreen = Color(0xFF10B981)
val WarningOrange = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val PurpleAccent = Color(0xFF8B5CF6)
val BgGray = Color(0xFFF8F9FA)
val CardWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1F2937)
val TextGray = Color(0xFF6B7280)
val TextLight = Color(0xFF9CA3AF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavController,
    adminId: String,
    adminName: String
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedTab by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // Dữ liệu thống kê từ Firebase
    var totalStudents by remember { mutableStateOf(0) }
    var totalTeachers by remember { mutableStateOf(0) }
    var totalClasses by remember { mutableStateOf(0) }
    var totalRevenue by remember { mutableStateOf(245) }

    // Load dữ liệu từ Firebase
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            totalStudents = RepoHolder.repo.getAllStudents().size
            totalTeachers = RepoHolder.repo.getAllTeachers().size
            totalClasses = RepoHolder.repo.getAllClasses().size
        } catch (e: Exception) {
            totalStudents = 1254
            totalTeachers = 48
            totalClasses = 32
        } finally {
            isLoading = false
        }
    }

    // Stats cards
    val stats = listOf(
        StatCardData("👥", formatNumber(totalStudents), "Học sinh", "+12%", SuccessGreen),
        StatCardData("👩‍🏫", totalTeachers.toString(), "Giáo viên", "+5%", NavyPrimary),
        StatCardData("📚", totalClasses.toString(), "Lớp học", "+8%", WarningOrange),
        StatCardData("💰", "${totalRevenue}M", "Doanh thu", "+18%", PurpleAccent)
    )

    // Tin tức hot
    val hotNews = listOf(
        HotNews("🎉", "Khai giảng năm học mới", "Chào đón hơn 1.200 tân học sinh", "15 phút trước"),
        HotNews("🏆", "Giải thưởng giáo viên xuất sắc", "Cô Trần Nguyễn Bảo Trâm đạt giải", "1 giờ trước"),
        HotNews("📢", "Thông báo lịch thi HK2", "Từ ngày 15/05 đến 30/05/2025", "2 giờ trước")
    )

    // Yêu cầu chờ duyệt
    val pendingRequests = listOf(
        PendingRequestData("Nguyễn Văn An", "Đăng ký giáo viên", "Chờ duyệt", NavyPrimary),
        PendingRequestData("Trần Thị Bích", "Đăng ký học sinh", "Chờ duyệt", SuccessGreen),
        PendingRequestData("Lê Thị Mai", "Tạo lớp học", "Chờ duyệt", WarningOrange)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminDrawerMenu(
                userName = adminName,
                userEmail = adminId,
                drawerState = drawerState,
                scope = scope,
                onNavigateToDashboard = { selectedTab = 0 },
                onNavigateToUsers = { selectedTab = 1 },
                onNavigateToCourses = { selectedTab = 2 },
                onNavigateToContests = { selectedTab = 3 },
                onNavigateToClasses = { selectedTab = 4 },
                onNavigateToSettings = { selectedTab = 5 },
                onLogout = {
                    scope.launch { drawerState.close() }
                    navController.navigate("login") {
                        popUpTo("admin_dashboard") { inclusive = true }
                    }
                }
            )
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
                                color = NavyPrimary.copy(alpha = 0.1f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📊", fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("TeachFlow Admin", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                Text("Quản trị hệ thống", fontSize = 11.sp, color = TextGray)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null, tint = NavyPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite),
                    actions = {
                        IconButton(onClick = {}) {
                            Badge(containerColor = ErrorRed) { Text("3", fontSize = 9.sp) }
                            Icon(Icons.Outlined.Notifications, null, tint = TextGray)
                        }
                        Surface(
                            shape = CircleShape,
                            color = NavyPrimary.copy(alpha = 0.1f),
                            modifier = Modifier.size(40.dp).clickable { }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(adminName.take(1).uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                            }
                        }
                    }
                )
            },
            bottomBar = {
                AdminBottomNav(
                    selectedIndex = selectedTab,
                    onItemSelected = { selectedTab = it }
                )
            }
        ) { padding ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NavyPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Đang tải dữ liệu...", fontSize = 14.sp, color = TextGray)
                    }
                }
            } else {
                when (selectedTab) {
                    0 -> HomeTab(padding, navController)
                    1 -> AdminUsersTab(padding, navController)
                    2 -> AdminCoursesTab(padding, navController)
                    3 -> AdminContestsTab(padding, navController)
                    4 -> AdminClassesTab(padding, navController)
                    5 -> SettingsTab(padding, navController)
                }
            }
        }
    }
}

@Composable
fun AdminDrawerMenu(
    userName: String,
    userEmail: String,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    onNavigateToDashboard: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToCourses: () -> Unit,
    onNavigateToContests: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp).shadow(8.dp),
        drawerContainerColor = CardWhite,
        drawerShape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(brush = Brush.verticalGradient(colors = listOf(NavyPrimary, NavyDark)))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(70.dp).shadow(8.dp, CircleShape).clip(CircleShape).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) { Text("👨‍💼", fontSize = 36.sp) }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(userName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(userEmail, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.15f), modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                        Text("Quản trị viên", fontSize = 12.sp, color = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                DrawerMenuItem(Icons.Outlined.Dashboard, "Tổng quan", true) {
                    scope.launch { drawerState.close() }; onNavigateToDashboard()
                }
                DrawerMenuItem(Icons.Outlined.People, "Quản lý người dùng") {
                    scope.launch { drawerState.close() }; onNavigateToUsers()
                }
                DrawerMenuItem(Icons.Outlined.School, "Quản lý khóa học", badge = "12") {
                    scope.launch { drawerState.close() }; onNavigateToCourses()
                }
                DrawerMenuItem(Icons.Outlined.Star, "Cuộc thi", badge = "3") {
                    scope.launch { drawerState.close() }; onNavigateToContests()
                }
                DrawerMenuItem(Icons.Outlined.Class, "Lớp học") {
                    scope.launch { drawerState.close() }; onNavigateToClasses()
                }
                DrawerMenuItem(Icons.Outlined.Settings, "Cài đặt") {
                    scope.launch { drawerState.close() }; onNavigateToSettings()
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                DrawerMenuItem(Icons.Outlined.Logout, "Đăng xuất", isDestructive = true) {
                    scope.launch { drawerState.close() }; onLogout()
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isSelected: Boolean = false,
    isDestructive: Boolean = false,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (isSelected) NavyPrimary.copy(alpha = 0.1f) else Color.Transparent, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(
                if (isDestructive) ErrorRed.copy(alpha = 0.1f)
                else if (isSelected) NavyPrimary.copy(alpha = 0.1f)
                else Color(0xFFF1F5F9)
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = when {
                isDestructive -> ErrorRed
                isSelected -> NavyPrimary
                else -> TextGray
            }, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, color = when {
            isDestructive -> ErrorRed
            isSelected -> NavyPrimary
            else -> TextDark
        }, modifier = Modifier.weight(1f))
        if (badge != null) {
            Surface(shape = CircleShape, color = ErrorRed, modifier = Modifier.size(18.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(badge, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold) }
            }
        } else {
            Icon(Icons.Outlined.ChevronRight, null, tint = TextLight, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun HomeTab(padding: PaddingValues, navController: NavController) {
    com.example.teachflow.presentation.admin.tabs.HomeTab(padding, navController)
}

fun formatNumber(number: Int): String = if (number >= 1000) String.format("%.1fK", number / 1000.0) else number.toString()

data class StatCardData(val icon: String, val value: String, val title: String, val change: String, val color: Color)
data class HotNews(val icon: String, val title: String, val description: String, val time: String)
data class PendingRequestData(val userName: String, val requestType: String, val status: String, val color: Color)