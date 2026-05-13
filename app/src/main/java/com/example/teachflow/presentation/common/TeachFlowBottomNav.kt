package com.example.teachflow.presentation.common

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
import androidx.navigation.NavController

val TeachFlowBlue = Color(0xFF1A73E8)
val TeachFlowDark = Color(0xFF0D47A1)
val TeachFlowGreen = Color(0xFF34A853)
val TeachFlowOrange = Color(0xFFFBBC05)
val TeachFlowRed = Color(0xFFEA4335)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachFlowBottomNav(
    navController: NavController,
    selectedRoute: String,
    onNavigateToHome: () -> Unit,
    onNavigateToClasses: () -> Unit,
    onNavigateToGrades: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onScanQR: () -> Unit,
    onVoiceSearch: () -> Unit,
    onQuickTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showQuickMenu by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    
    val centerScale by animateFloatAsState(
        targetValue = if (isScanning) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
    )
    
    Box(modifier = modifier.fillMaxWidth()) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), clip = false)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationBarItem(
                    selected = selectedRoute == "home",
                    onClick = { isScanning = false; showQuickMenu = false; onNavigateToHome() },
                    icon = { Icon(if (selectedRoute == "home") Icons.Filled.Home else Icons.Outlined.Home, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Trang chủ", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TeachFlowBlue, selectedTextColor = TeachFlowBlue,
                        unselectedIconColor = Color(0xFF9AA0A6), unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
                
                NavigationBarItem(
                    selected = selectedRoute == "classes",
                    onClick = { isScanning = false; showQuickMenu = false; onNavigateToClasses() },
                    icon = { Icon(if (selectedRoute == "classes") Icons.Filled.School else Icons.Outlined.School, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Lớp học", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TeachFlowBlue, selectedTextColor = TeachFlowBlue,
                        unselectedIconColor = Color(0xFF9AA0A6), unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
                
                // Nút Center
                Box(modifier = Modifier.size(56.dp).scale(centerScale), contentAlignment = Alignment.Center) {
                    FloatingActionButton(
                        onClick = { isScanning = !isScanning; showQuickMenu = !showQuickMenu; onScanQR() },
                        shape = CircleShape,
                        containerColor = TeachFlowBlue,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 12.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(if (isScanning) Icons.Filled.QrCodeScanner else Icons.Outlined.QrCodeScanner, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
                
                NavigationBarItem(
                    selected = selectedRoute == "grades",
                    onClick = { isScanning = false; showQuickMenu = false; onNavigateToGrades() },
                    icon = { Icon(if (selectedRoute == "grades") Icons.Filled.Grade else Icons.Outlined.Grade, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Bảng điểm", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TeachFlowBlue, selectedTextColor = TeachFlowBlue,
                        unselectedIconColor = Color(0xFF9AA0A6), unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
                
                NavigationBarItem(
                    selected = selectedRoute == "profile",
                    onClick = { isScanning = false; showQuickMenu = false; onNavigateToProfile() },
                    icon = { Icon(if (selectedRoute == "profile") Icons.Filled.Person else Icons.Outlined.Person, null, modifier = Modifier.size(24.dp)) },
                    label = { Text("Cá nhân", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TeachFlowBlue, selectedTextColor = TeachFlowBlue,
                        unselectedIconColor = Color(0xFF9AA0A6), unselectedTextColor = Color(0xFF9AA0A6)
                    )
                )
            }
        }
        
        AnimatedVisibility(
            visible = showQuickMenu,
            enter = fadeIn() + scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)),
            exit = fadeOut() + scaleOut(targetScale = 0.8f)
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp).height(180.dp), contentAlignment = Alignment.BottomCenter) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.9f).height(140.dp).shadow(16.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuickActionItem(icon = "📷", title = "Quét QR", color = TeachFlowBlue, onClick = { showQuickMenu = false; onScanQR() })
                        QuickActionItem(icon = "🎤", title = "Voice Search", color = TeachFlowGreen, onClick = { showQuickMenu = false; onVoiceSearch() })
                        QuickActionItem(icon = "⚡", title = "Quick Task", color = TeachFlowOrange, onClick = { showQuickMenu = false; onQuickTask() })
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(icon: String, title: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(12.dp)
    ) {
        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(brush = Brush.linearGradient(colors = listOf(color, color.copy(alpha = 0.8f)))), contentAlignment = Alignment.Center) {
            Text(icon, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212529), maxLines = 1)
    }
}
