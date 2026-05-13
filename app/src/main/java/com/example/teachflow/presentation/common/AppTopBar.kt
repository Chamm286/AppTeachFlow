package com.example.teachflow.presentation.common

import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onMenuClick: () -> Unit,
    onNotifyClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title, color = Color.White) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onNotifyClick) {
                Badge(
                    containerColor = Color.Red,
                    modifier = Modifier.offset(x = 8.dp, y = (-4).dp)
                ) {
                    Text("3", fontSize = 9.sp, color = Color.White)
                }
                Icon(Icons.Default.Notifications, contentDescription = "Thông báo", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6366F1)
        )
    )
}
