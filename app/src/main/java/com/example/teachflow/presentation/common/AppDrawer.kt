package com.example.teachflow.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.R

@Composable
fun AppDrawer(
    userName: String,
    userRole: String,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLanguageMenu by remember { mutableStateOf(false) }
    
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = Color.White
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A73E8))
                .padding(24.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (userRole == "Giáo viên") "👨‍🏫" else "👨‍🎓", fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(userName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(userRole, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }
        
        // Menu items
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            DrawerItemRow(
                icon = { Icon(Icons.Default.Dashboard, null) },
                title = "Trang chủ",
                onClick = { onItemClick("home") }
            )
            DrawerItemRow(
                icon = { Icon(Icons.Default.School, null) },
                title = "Lớp học",
                onClick = { onItemClick("class") }
            )
            DrawerItemRow(
                icon = { Icon(Icons.Default.Grade, null) },
                title = "Bảng điểm",
                onClick = { onItemClick("grade") }
            )
            DrawerItemRow(
                icon = { Icon(Icons.Default.People, null) },
                title = "Học sinh",
                onClick = { onItemClick("student") }
            )
            DrawerItemRow(
                icon = { Icon(Icons.Default.CalendarMonth, null) },
                title = "Lịch học",
                onClick = { onItemClick("schedule") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            // Language selector with flag icons
            Box {
                DrawerItemRow(
                    icon = { 
                        Icon(
                            if (currentLanguage == "VI") Icons.Default.Translate else Icons.Default.Translate,
                            null
                        ) 
                    },
                    title = "Ngôn ngữ",
                    trailing = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (currentLanguage == "VI") "Tiếng Việt" else "English",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
                        }
                    },
                    onClick = { showLanguageMenu = true }
                )
                
                DropdownMenu(
                    expanded = showLanguageMenu,
                    onDismissRequest = { showLanguageMenu = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = { Text("🇻🇳", fontSize = 18.sp) },
                        text = { Text("Tiếng Việt") },
                        onClick = {
                            onLanguageChange("VI")
                            showLanguageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = { Text("🇺🇸", fontSize = 18.sp) },
                        text = { Text("English") },
                        onClick = {
                            onLanguageChange("EN")
                            showLanguageMenu = false
                        }
                    )
                }
            }
            
            DrawerItemRow(
                icon = { Icon(Icons.Default.Settings, null) },
                title = "Cài đặt",
                onClick = { onItemClick("settings") }
            )
            DrawerItemRow(
                icon = { Icon(Icons.Default.Info, null) },
                title = "Giới thiệu",
                onClick = { onItemClick("about") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            DrawerItemRow(
                icon = { Icon(Icons.Default.Logout, null) },
                title = "Đăng xuất",
                onClick = onLogout,
                isDestructive = true
            )
        }
    }
}

@Composable
fun DrawerItemRow(
    icon: @Composable () -> Unit,
    title: String,
    trailing: @Composable (() -> Unit)? = null,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = icon,
        label = { 
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = if (isDestructive) Color.Red else Color.Black)
                trailing?.invoke()
            }
        },
        selected = false,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color(0xFF1A73E8).copy(alpha = 0.1f)
        )
    )
}
