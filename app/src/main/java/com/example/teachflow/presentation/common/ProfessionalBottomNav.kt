package com.example.teachflow.presentation.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teachflow.presentation.theme.TeachFlowBlue
import com.example.teachflow.presentation.theme.TeachFlowTextSecondary

data class BottomNavItem(
    val id: Int,
    val title: String,
    val iconSelected: @Composable () -> Unit,
    val iconUnselected: @Composable () -> Unit
)

@Composable
fun ProfessionalBottomNav(
    selectedId: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(0, "Trang chủ",
            iconSelected = { Icon(Icons.Filled.Home, null, tint = TeachFlowBlue) },
            iconUnselected = { Icon(Icons.Outlined.Home, null, tint = TeachFlowTextSecondary) }
        ),
        BottomNavItem(1, "Lớp học",
            iconSelected = { Icon(Icons.Filled.School, null, tint = TeachFlowBlue) },
            iconUnselected = { Icon(Icons.Outlined.School, null, tint = TeachFlowTextSecondary) }
        ),
        BottomNavItem(2, "Bảng điểm",
            iconSelected = { Icon(Icons.Filled.Grade, null, tint = TeachFlowBlue) },
            iconUnselected = { Icon(Icons.Outlined.Grade, null, tint = TeachFlowTextSecondary) }
        ),
        BottomNavItem(3, "Thông báo",
            iconSelected = { Icon(Icons.Filled.Notifications, null, tint = TeachFlowBlue) },
            iconUnselected = { Icon(Icons.Outlined.Notifications, null, tint = TeachFlowTextSecondary) }
        ),
        BottomNavItem(4, "Cá nhân",
            iconSelected = { Icon(Icons.Filled.Person, null, tint = TeachFlowBlue) },
            iconUnselected = { Icon(Icons.Outlined.Person, null, tint = TeachFlowTextSecondary) }
        )
    )
    
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = selectedId == item.id
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.id) },
                icon = {
                    if (isSelected) item.iconSelected() else item.iconUnselected()
                },
                label = {
                    Text(
                        item.title,
                        fontSize = 11.sp,
                        color = if (isSelected) TeachFlowBlue else TeachFlowTextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TeachFlowBlue,
                    selectedTextColor = TeachFlowBlue,
                    unselectedIconColor = TeachFlowTextSecondary,
                    unselectedTextColor = TeachFlowTextSecondary,
                    indicatorColor = TeachFlowBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}

