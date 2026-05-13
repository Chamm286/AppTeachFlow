package com.example.teachflow.presentation.search

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSearchScreen(
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    var isListening by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }
    
    val pulse by animateFloatAsState(
        targetValue = if (isListening) 1.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tìm kiếm bằng giọng nói", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A73E8)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F7FA)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Hiệu ứng sóng âm
            if (isListening) {
                SoundWaveAnimation()
            }
            
            // Nút mic
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulse),
                shape = CircleShape,
                color = if (isListening) Color(0xFFEA4335) else Color(0xFF1A73E8),
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Mic,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = if (isListening) "Đang lắng nghe..." else "Nhấn vào mic để nói",
                fontSize = 16.sp,
                color = Color(0xFF5F6368)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Kết quả nhận dạng
            if (recognizedText.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Kết quả:", fontSize = 12.sp, color = Color.Gray)
                        Text(recognizedText, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { /* TODO: Search */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
                        ) {
                            Text("Tìm kiếm")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Nút bắt đầu/dừng
            Button(
                onClick = {
                    scope.launch {
                        if (isListening) {
                            isListening = false
                            recognizedText = "Tìm kiếm bài tập Toán lớp 10"
                        } else {
                            isListening = true
                            delay(3000)
                            isListening = false
                            recognizedText = "Tìm kiếm bài tập Toán lớp 10"
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isListening) Color(0xFFEA4335) else Color(0xFF1A73E8)
                )
            ) {
                Text(if (isListening) "DỪNG" else "BẮT ĐẦU")
            }
        }
    }
}

@Composable
fun SoundWaveAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val heights = List(5) { index ->
        infiniteTransition.animateFloat(
            initialValue = 20f,
            targetValue = if (index % 2 == 0) 40f else 20f,
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    
    Row(
        modifier = Modifier.padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        heights.forEach { height ->
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(height.value.dp)
                    .background(Color(0xFF1A73E8), CircleShape)
            )
        }
    }
}
