package com.example.teachflow.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MenuItem(
    val id: Int,
    val title: String,
    val iconOn: @Composable () -> Unit,
    val iconOff: @Composable () -> Unit
)

@Composable
fun BottomNavBar(
    items: List<MenuItem>,
    selectedId: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedId == item.id,
                onClick = { onSelect(item.id) },
                icon = {
                    if (selectedId == item.id) {
                        item.iconOn()
                    } else {
                        item.iconOff()
                    }
                },
                label = {
                    Text(
                        item.title,
                        fontSize = 11.sp,
                        color = if (selectedId == item.id) Color(0xFF6366F1) else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6366F1),
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

fun defaultMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(0, "Trang chủ",
            iconOn = { Icon(Icons.Filled.Home, null) },
            iconOff = { Icon(Icons.Outlined.Home, null) }
        ),
        MenuItem(1, "Lớp học",
            iconOn = { Icon(Icons.Filled.School, null) },
            iconOff = { Icon(Icons.Outlined.School, null) }
        ),
        MenuItem(2, "Bảng điểm",
            iconOn = { Icon(Icons.Filled.Grade, null) },
            iconOff = { Icon(Icons.Outlined.Grade, null) }
        ),
        MenuItem(3, "Thông báo",
            iconOn = { Icon(Icons.Filled.Notifications, null) },
            iconOff = { Icon(Icons.Outlined.Notifications, null) }
        ),
        MenuItem(4, "Cá nhân",
            iconOn = { Icon(Icons.Filled.Person, null) },
            iconOff = { Icon(Icons.Outlined.Person, null) }
        )
    )
}
