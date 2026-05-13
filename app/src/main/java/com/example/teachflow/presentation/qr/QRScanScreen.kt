package com.example.teachflow.presentation.qr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScanScreen(navController: NavController, onScanResult: (String) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quét mã QR", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFF0F4C81)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📷", fontSize = 80.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Đưa mã QR vào khung hình", fontSize = 16.sp, color = Color.White)
                Text("Đang phát triển...", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { onScanResult("QR_DEMO_123"); navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text("Demo Scan", color = Color(0xFF0F4C81))
                }
            }
        }
    }
}
