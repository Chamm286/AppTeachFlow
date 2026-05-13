package com.example.teachflow.presentation.teacher.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val NavBlue = Color(0xFF1A73E8)
val NavSuccess = Color(0xFF34A853)
val NavWarning = Color(0xFFFBBC05)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onQuickAction: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showQuickMenu by remember { mutableStateOf(false) }
    var isCenterPressed by remember { mutableStateOf(false) }
    val centerScale by animateFloatAsState(targetValue = if (isCenterPressed || showQuickMenu) 1.15f else 1f)
    
    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), clip = false)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { onTabSelected(0) },
                    icon = { Icon(if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Trang chủ", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavBlue,
                        selectedTextColor = NavBlue,
                        unselectedIconColor = Color(0xFF9AA0A6),
                        unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
                
                // Classes
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { onTabSelected(1) },
                    icon = { Icon(if (selectedTab == 1) Icons.Filled.School else Icons.Outlined.School, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Lớp học", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavBlue,
                        selectedTextColor = NavBlue,
                        unselectedIconColor = Color(0xFF9AA0A6),
                        unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
                
                // Center Button
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .scale(centerScale),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = {
                            isCenterPressed = true
                            showQuickMenu = !showQuickMenu
                            if (showQuickMenu) onQuickAction("quick_menu")
                            scope.launch {
                                delay(200)
                                isCenterPressed = false
                            }
                        },
                        shape = CircleShape,
                        containerColor = NavBlue,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            if (showQuickMenu) Icons.Filled.Close else Icons.Filled.Add,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                // Grades
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { onTabSelected(2) },
                    icon = { Icon(if (selectedTab == 2) Icons.Filled.Grade else Icons.Outlined.Grade, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Bảng điểm", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavBlue,
                        selectedTextColor = NavBlue,
                        unselectedIconColor = Color(0xFF9AA0A6),
                        unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
                
                // Profile
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { onTabSelected(3) },
                    icon = { Icon(if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Cá nhân", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavBlue,
                        selectedTextColor = NavBlue,
                        unselectedIconColor = Color(0xFF9AA0A6),
                        unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
            }
        }
        
        // Quick Menu Popup
        AnimatedVisibility(
            visible = showQuickMenu,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 72.dp)
                    .height(160.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(120.dp)
                        .shadow(16.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickMenuItem(icon = "📝", title = "Nhập điểm", color = NavBlue) {
                            showQuickMenu = false
                            onQuickAction("grade_input")
                        }
                        QuickMenuItem(icon = "📖", title = "Điểm danh", color = NavSuccess) {
                            showQuickMenu = false
                            onQuickAction("attendance")
                        }
                        QuickMenuItem(icon = "📊", title = "Thống kê", color = NavWarning) {
                            showQuickMenu = false
                            onQuickAction("statistics")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickMenuItem(icon: String, title: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(color, color.copy(alpha = 0.8f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
    }
}
