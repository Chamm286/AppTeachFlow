// components/AdminBottomNav.kt
package com.example.teachflow.presentation.admin.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.presentation.admin.NavyPrimary
import com.example.teachflow.presentation.admin.TextGray

@Composable
fun AdminBottomNav(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem(0, "Trang chủ", Icons.Filled.Home),
        BottomNavItem(1, "Người dùng", Icons.Filled.People),
        BottomNavItem(2, "Khóa học", Icons.Filled.School),
        BottomNavItem(3, "Cuộc thi", Icons.Filled.Star),
        BottomNavItem(4, "Cài đặt", Icons.Filled.Settings)
    )

    // Sử dụng Box để có thể cho nút nhô lên trên nền trắng
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Quan trọng: Tránh đè lên thanh điều hướng Android
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 1. Nền trắng của thanh Bottom Nav
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .shadow(15.dp, shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White, shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
        )

        // 2. Các Items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp), // Tăng chiều cao tổng thể để chứa nút nhô lên
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val isCenter = index == 2

                if (isCenter) {
                    // Item chính giữa (Nhô lên)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Nút tròn nhô lên
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .shadow(10.dp, CircleShape)
                                    .scale(if (isSelected) 1.1f else 1f)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = if (isSelected) 
                                                listOf(NavyPrimary, Color(0xFF1E88E5)) 
                                            else 
                                                listOf(Color(0xFF455A64), Color(0xFF263238))
                                        ),
                                        shape = CircleShape
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { onItemSelected(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                item.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) NavyPrimary else TextGray
                            )
                        }
                    }
                } else {
                    // Các items thông thường
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(65.dp) // Bằng chiều cao nền trắng
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onItemSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                item.icon,
                                null,
                                tint = if (isSelected) NavyPrimary else TextGray.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                item.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) NavyPrimary else TextGray
                            )
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavItem(
    val id: Int,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
