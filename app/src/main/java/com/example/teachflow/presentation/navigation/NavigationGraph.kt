package com.example.teachflow.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.teachflow.presentation.auth.LoginScreen
import com.example.teachflow.presentation.auth.RegisterScreen
import com.example.teachflow.presentation.auth.AuthViewModel
import com.example.teachflow.presentation.admin.AdminDashboardScreen
import com.example.teachflow.presentation.teacher.TeacherDashboardScreen
import com.example.teachflow.presentation.student.StudentDashboardScreen
import com.example.teachflow.presentation.profile.ProfileScreen
import com.example.teachflow.presentation.chat.ChatScreen
import com.example.teachflow.presentation.settings.SettingsScreen
import com.example.teachflow.presentation.statistics.StatisticsScreen
import com.example.teachflow.presentation.notification.NotificationScreen
import com.example.teachflow.presentation.qr.QRScanScreen
import com.example.teachflow.presentation.splash.SplashScreen
import com.example.teachflow.presentation.onboarding.OnboardingScreen
import com.example.teachflow.presentation.student.StudentChatDetailScreen
import com.example.teachflow.presentation.student.StudentChatListScreen
import com.example.teachflow.presentation.student.viewmodel.StudentViewModel
import com.example.teachflow.core.RepoHolder

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    
    Log.d("NAV_GRAPH", "🎯 NavigationGraph khởi tạo")
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen - KHÔNG có navController
        composable("splash") {
            SplashScreen(
                onTimeout = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        
        // Onboarding Screen - KHÔNG có navController
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        
        // Login Screen
        composable("login") { 
            LoginScreen(
                navController = navController, 
                authViewModel = authViewModel
            ) 
        }
        
        // Register Screen
        composable("register") { 
            RegisterScreen(
                navController = navController, 
                authViewModel = authViewModel
            ) 
        }
        
        // Admin Dashboard
        composable("admin_dashboard") {
            AdminDashboardScreen(
                navController = navController,
                adminId = authState.userId ?: "AD01",
                adminName = authState.userName ?: "Admin"
            )
        }
        
        // Teacher Dashboard
        composable("teacher_dashboard") {
            TeacherDashboardScreen(
                navController = navController,
                teacherId = authState.userId ?: "GV01",
                teacherName = authState.userName ?: "Giáo viên"
            )
        }
        
        // Student Dashboard
        composable("student_dashboard") {
            // 1. Khởi tạo ViewModel
            val studentViewModel: StudentViewModel = viewModel()

            // 2. Nạp Repo và Load dữ liệu ngay khi vào màn hình
            // Sử dụng LaunchedEffect để đảm bảo dữ liệu chỉ tải 1 lần khi vào màn hình
            LaunchedEffect(Unit) {
                // Nạp Repository từ RepoHolder
                studentViewModel.initRepo(RepoHolder.repo)

                // Gọi hàm loadData với ID học sinh để kéo dữ liệu từ Firebase về
                studentViewModel.loadData("HS01")
            }

            // 3. Hiển thị màn hình
            StudentDashboardScreen(
                navController = navController,
                studentId = "HS01",
                studentName = "Nguyễn Minh Quân",
                viewModel = studentViewModel
            )
        }
        
        // Common Screens
        composable("profile") { 
            ProfileScreen(
                navController = navController,
                userRole = authState.userRole ?: "",
                userName = authState.userName ?: ""
            ) 
        }
        
        composable("chat") { 
            ChatScreen(
                navController = navController,
                userId = authState.userId ?: "",
                userRole = authState.userRole ?: ""
            ) 
        }
        
        composable("settings") { 
            SettingsScreen(navController = navController) 
        }
        
        composable("statistics") { 
            StatisticsScreen(
                navController = navController,
                userRole = authState.userRole ?: ""
            ) 
        }
        
        composable("notifications") { 
            NotificationScreen(
                navController = navController,
                userId = authState.userId ?: "",
                userRole = authState.userRole ?: ""
            ) 
        }
        
        composable("qr_scan") { 
            QRScanScreen(
                navController = navController,
                onScanResult = { result ->
                    Log.d("QR_SCAN", "Kết quả: $result")
                    navController.popBackStack()
                }
            ) 
        }

        // Màn hình danh sách tin nhắn của học sinh
        composable("student_chat_list") {
            StudentChatListScreen(navController = navController)
        }

        // Màn hình chi tiết tin nhắn
        composable("student_chat_detail/{chatId}/{teacherName}") { backStackEntry ->
            val teacherName = backStackEntry.arguments?.getString("teacherName") ?: "Giảng viên"
            StudentChatDetailScreen(navController = navController, teacherName = teacherName)
        }
    }
}
