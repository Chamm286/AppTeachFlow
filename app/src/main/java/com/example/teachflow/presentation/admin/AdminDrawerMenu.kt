package com.example.teachflow.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDrawerMenu(
    userName: String,
    userEmail: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    onNavigateToDashboard: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp).shadow(8.dp),
        drawerContainerColor = AdminCard,
        drawerShape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(brush = Brush.verticalGradient(colors = listOf(AdminNavy, AdminNavyDark)))
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
                    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.15f)) {
                        Text("Quản trị viên", fontSize = 12.sp, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                    }
                }
            }

            // Menu items
            Column(modifier = Modifier.padding(16.dp)) {
                DrawerMenuItem(Icons.Outlined.Dashboard, "Tổng quan") { scope.launch { drawerState.close() }; onNavigateToDashboard() }
                DrawerMenuItem(Icons.Outlined.People, "Quản lý người dùng") { scope.launch { drawerState.close() }; onNavigateToUsers() }
                DrawerMenuItem(Icons.Outlined.BarChart, "Thống kê") { scope.launch { drawerState.close() }; onNavigateToStats() }
                DrawerMenuItem(Icons.Outlined.School, "Lớp học") { scope.launch { drawerState.close() }; onNavigateToClasses() }
                DrawerMenuItem(Icons.Outlined.Settings, "Cài đặt") { scope.launch { drawerState.close() }; onNavigateToSettings() }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                DrawerMenuItem(Icons.Outlined.Logout, "Đăng xuất", isDestructive = true) { scope.launch { drawerState.close() }; onLogout() }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(icon: ImageVector, title: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isDestructive) AdminError.copy(alpha = 0.1f) else AdminNavy.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if (isDestructive) AdminError else AdminNavy, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 14.sp, color = if (isDestructive) AdminError else AdminTextPrimary, modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ChevronRight, null, tint = AdminTextSecondary, modifier = Modifier.size(16.dp))
    }
}