package com.example.teachflow.presentation.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val authState by authViewModel.authState.collectAsState()
    
    // Set test data để đăng nhập nhanh
    LaunchedEffect(Unit) {
        email = "chamm4930@gmail.com"
        password = "12345678"
        Log.d("LOGIN_SCREEN", "📧 Đã set email test: $email")
        Log.d("LOGIN_SCREEN", "🔑 Đã set password test: $password")
    }
    
    // Lắng nghe đăng nhập thành công
    LaunchedEffect(authState.isAuthenticated, authState.userRole) {
        if (authState.isAuthenticated) {
            Log.d("LOGIN_SCREEN", "🎯 Đăng nhập thành công! Role: ${authState.userRole}")
            when (authState.userRole) {
                "admin" -> {
                    Log.d("LOGIN_SCREEN", "🚀 Chuyển sang Admin Dashboard")
                    navController.navigate("admin_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "teacher" -> {
                    Log.d("LOGIN_SCREEN", "🚀 Chuyển sang Teacher Dashboard")
                    navController.navigate("teacher_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "student" -> {
                    Log.d("LOGIN_SCREEN", "🚀 Chuyển sang Student Dashboard")
                    navController.navigate("student_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                else -> {
                    Log.d("LOGIN_SCREEN", "⚠️ Role không xác định: ${authState.userRole}")
                }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F4C81), Color(0xFF1A5BA8))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(100.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("📚", fontSize = 56.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "TeachFlow",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                "Hệ thống quản lý giáo dục thông minh",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Đăng nhập",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F4C81)
                    )
                    
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    
                    if (authState.error != null) {
                        Text(
                            authState.error!!,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                    
                    Button(
                        onClick = { 
                            Log.d("LOGIN_SCREEN", "🔘 Bấm nút đăng nhập")
                            Log.d("LOGIN_SCREEN", "   - Email: $email")
                            Log.d("LOGIN_SCREEN", "   - Password: $password")
                            authViewModel.login(email, password) 
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F4C81))
                    ) {
                        if (authState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Đăng nhập", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Chưa có tài khoản? ", color = Color.Gray)
                        Text(
                            "Đăng ký ngay",
                            color = Color(0xFF0F4C81),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                navController.navigate("register")
                            }
                        )
                    }
                }
            }
        }
    }
}
