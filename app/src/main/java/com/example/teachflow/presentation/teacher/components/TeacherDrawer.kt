package com.example.teachflow.presentation.teacher.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DrawerBlue = Color(0xFF1A73E8)
val DrawerDark = Color(0xFF0D47A1)
val DrawerTextDark = Color(0xFF202124)
val DrawerTextGray = Color(0xFF5F6368)
val DrawerError = Color(0xFFEA4335)

@Composable
fun TeacherDrawer(
    userName: String,
    userEmail: String,
    selectedItem: String,
    onItemClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp).shadow(8.dp),
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(0.dp, 20.dp, 20.dp, 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(DrawerBlue, DrawerDark)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    var scale by remember { mutableStateOf(0.8f) }
                    LaunchedEffect(Unit) { scale = 1f }
                    
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .scale(scale)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) { Text("👨‍🏫", fontSize = 36.sp) }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(userName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(userEmail, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Giáo viên", fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
            }
            
            // Menu Items
            LazyColumn(modifier = Modifier.fillMaxSize().padding(vertical = 8.dp)) {
                item { DrawerMenuItem(icon = Icons.Outlined.Dashboard, title = "Trang chủ", isSelected = selectedItem == "home", onClick = { onItemClick("home") }) }
                item { DrawerMenuItem(icon = Icons.Outlined.School, title = "Lớp học", isSelected = selectedItem == "classes", onClick = { onItemClick("classes") }) }
                item { DrawerMenuItem(icon = Icons.Outlined.Grade, title = "Bảng điểm", isSelected = selectedItem == "grades", onClick = { onItemClick("grades") }) }
                item { DrawerMenuItem(icon = Icons.Outlined.Person, title = "Hồ sơ", isSelected = selectedItem == "profile", onClick = { onItemClick("profile") }) }
                item { Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color(0xFFE0E0E0), thickness = 0.5.dp) }
                item { DrawerMenuItem(icon = Icons.Outlined.Settings, title = "Cài đặt", isSelected = false, onClick = { onItemClick("settings") }) }
                item { DrawerMenuItem(icon = Icons.Outlined.Help, title = "Trợ giúp", isSelected = false, onClick = { onItemClick("help") }) }
                item { Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = Color(0xFFE0E0E0), thickness = 0.5.dp) }
                item { DrawerMenuItem(icon = Icons.Outlined.Logout, title = "Đăng xuất", isSelected = false, isDestructive = true, onClick = onLogout) }
                
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp, 16.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📚", fontSize = 20.sp)
                            Text("TeachFlow", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DrawerBlue)
                            Text("Version 1.0.0", fontSize = 10.sp, color = DrawerTextGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1f)
    val bgColor = when {
        isSelected -> DrawerBlue.copy(alpha = 0.1f)
        isPressed -> Color(0xFFF5F5F5)
        else -> Color.Transparent
    }
    val iconColor = when {
        isDestructive -> DrawerError
        isSelected -> DrawerBlue
        else -> DrawerTextGray
    }
    val textColor = when {
        isDestructive -> DrawerError
        isSelected -> DrawerBlue
        else -> DrawerTextDark
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { isPressed = true; onClick(); isPressed = false }
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isSelected) DrawerBlue.copy(alpha = 0.1f) else Color(0xFFF1F3F4)),
            contentAlignment = Alignment.Center
        ) { Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp)) }
        
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, color = textColor, modifier = Modifier.weight(1f))
        
        if (isSelected) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(DrawerBlue))
        } else {
            Icon(Icons.Outlined.ChevronRight, null, tint = DrawerTextGray, modifier = Modifier.size(16.dp))
        }
    }
}
