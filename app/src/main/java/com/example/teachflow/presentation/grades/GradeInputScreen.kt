package com.example.teachflow.presentation.grades

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// GradeInputScreen.kt - Màn hình nhập điểm
@Composable
fun GradeInputScreen(
    navController: NavController,
    classId: String,
    subjectId: String
) {
    // Giao diện đơn giản để nhập điểm cho từng học sinh
    Text("Nhập điểm", fontSize = 20.sp)
}