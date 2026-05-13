package com.example.teachflow.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.presentation.theme.TeachFlowBlue
import com.example.teachflow.presentation.theme.TeachFlowError
import com.example.teachflow.presentation.theme.TeachFlowSurface
import com.example.teachflow.presentation.theme.TeachFlowText
import com.example.teachflow.presentation.theme.TeachFlowTextSecondary

@Composable
fun ModernDrawer(
    userName: String,
    userEmail: String,
    userRole: String,
    onItemClick: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = TeachFlowSurface,
        drawerShape = RoundedCornerShape(0.dp, 16.dp, 16.dp, 0.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TeachFlowBlue)
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar cố định
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (userRole == "teacher") "👨‍🏫" else "👨‍🎓",
                        fontSize = 36.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(userName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(userEmail, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        if (userRole == "teacher") "Giáo viên" else "Học sinh",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
        
        // Menu items
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            DrawerMenuItem(
                icon = Icons.Default.Dashboard,
                title = "Trang chủ",
                onClick = { onItemClick("home") }
            )
            DrawerMenuItem(
                icon = Icons.Default.School,
                title = "Lớp học",
                onClick = { onItemClick("class") }
            )
            DrawerMenuItem(
                icon = Icons.Default.Grade,
                title = "Bảng điểm",
                onClick = { onItemClick("grade") }
            )
            DrawerMenuItem(
                icon = Icons.Default.CalendarMonth,
                title = "Lịch học",
                onClick = { onItemClick("schedule") }
            )
            DrawerMenuItem(
                icon = Icons.Default.Chat,
                title = "Tin nhắn",
                onClick = { onItemClick("chat") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(8.dp))
            
            DrawerMenuItem(
                icon = Icons.Default.Settings,
                title = "Cài đặt",
                onClick = { onItemClick("settings") }
            )
            DrawerMenuItem(
                icon = Icons.Default.Info,
                title = "Giới thiệu",
                onClick = { onItemClick("about") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(8.dp))
            
            DrawerMenuItem(
                icon = Icons.Default.Logout,
                title = "Đăng xuất",
                onClick = onLogout,
                isDestructive = true
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(icon, null, tint = if (isDestructive) TeachFlowError else TeachFlowTextSecondary)
        },
        label = {
            Text(
                title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) TeachFlowError else TeachFlowText
            )
        },
        selected = false,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = TeachFlowBlue.copy(alpha = 0.1f),
            unselectedContainerColor = Color.Transparent
        )
    )
}
