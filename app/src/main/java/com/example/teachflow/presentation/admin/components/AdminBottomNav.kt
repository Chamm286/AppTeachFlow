// components/AdminBottomNav.kt
package com.example.teachflow.presentation.admin.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.presentation.admin.NavyPrimary
import com.example.teachflow.presentation.admin.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBottomNav(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem(0, "Trang chủ", Icons.Filled.Home),
        BottomNavItem(1, "Người dùng", Icons.Filled.People),
        BottomNavItem(2, "Khóa học", Icons.Filled.School),  // Nút giữa
        BottomNavItem(3, "Cuộc thi", Icons.Filled.Star),
        BottomNavItem(4, "Cài đặt", Icons.Filled.Settings)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .shadow(8.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index
                val isCenter = index == 2

                if (isCenter) {
                    // Nút giữa nhô lên
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-20).dp)
                                .size(56.dp)
                                .shadow(
                                    elevation = if (isSelected) 16.dp else 8.dp,
                                    shape = CircleShape,
                                    clip = false
                                )
                                .scale(if (isSelected) 1.1f else 1f)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(NavyPrimary, NavyPrimary.copy(alpha = 0.8f))
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
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            item.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NavyPrimary else TextGray,
                            modifier = Modifier.offset(y = (-8).dp)
                        )
                    }
                } else {
                    // Nút bên cạnh
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onItemSelected(index) }
                            .padding(bottom = 12.dp)
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) NavyPrimary else TextGray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            item.title,
                            fontSize = 10.sp,
                            color = if (isSelected) NavyPrimary else TextGray
                        )
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