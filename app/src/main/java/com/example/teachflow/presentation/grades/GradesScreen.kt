package com.example.teachflow.presentation.grades

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.example.teachflow.data.model.Grade
import com.example.teachflow.data.model.Student
import kotlinx.coroutines.tasks.await

// Màu sắc
val PrimaryBlue = Color(0xFF1A73E8)
val SecondaryGreen = Color(0xFF34A853)
val AccentOrange = Color(0xFFFBBC05)
val AccentRed = Color(0xFFEA4335)
val TextDark = Color(0xFF202124)
val TextGray = Color(0xFF5F6368)
val CardWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(
    navController: NavController,
    classId: String = "",
    className: String = "",
    subjectId: String = "",
    subjectName: String = ""
) {
    val db = FirebaseFirestore.getInstance()

    // State
    var students by remember { mutableStateOf<List<Student>>(emptyList()) }
    var grades by remember { mutableStateOf<List<Grade>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedGrade by remember { mutableStateOf<Grade?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Load data
    LaunchedEffect(classId, subjectId) {
        try {
            isLoading = true

            // Load students
            val studentsSnapshot = db.collection("students")
                .whereEqualTo("classId", classId)
                .whereEqualTo("status", "active")
                .get()
                .await()
            students = studentsSnapshot.documents.mapNotNull { it.toObject<Student>() }

            // Load grades
            val gradesSnapshot = db.collection("grades")
                .whereEqualTo("classId", classId)
                .whereEqualTo("subjectId", subjectId)
                .get()
                .await()
            grades = gradesSnapshot.documents.mapNotNull { it.toObject<Grade>() }

            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("📊 $subjectName", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("$className", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Quay lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("grade_input/$classId/$subjectId") },
                containerColor = PrimaryBlue
            ) {
                Icon(Icons.Default.Add, "Nhập điểm")
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            }
            students.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Chưa có học sinh", fontSize = 16.sp, color = TextGray)
                }
            }
            else -> {
                GradeTable(
                    students = students,
                    grades = grades,
                    onGradeClick = { grade ->
                        selectedGrade = grade
                        showEditDialog = true
                    },
                    paddingValues = paddingValues
                )
            }
        }
    }

    // Dialog sửa điểm
    if (showEditDialog && selectedGrade != null) {
        val grade = selectedGrade!!
        var score15 by remember { mutableStateOf(grade.score15min.toString()) }
        var score45 by remember { mutableStateOf(grade.score45min.toString()) }
        var scoreMid by remember { mutableStateOf(grade.scoreMidterm.toString()) }
        var scoreFinal by remember { mutableStateOf(grade.scoreFinal.toString()) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("✏️ Sửa điểm - ${grade.studentName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = score15,
                        onValueChange = { score15 = it },
                        label = { Text("Điểm 15 phút (hệ số 1)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = score45,
                        onValueChange = { score45 = it },
                        label = { Text("Điểm 45 phút (hệ số 2)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = scoreMid,
                        onValueChange = { scoreMid = it },
                        label = { Text("Điểm giữa kỳ (hệ số 2)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = scoreFinal,
                        onValueChange = { scoreFinal = it },
                        label = { Text("Điểm cuối kỳ (hệ số 3)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    val avg = calculateAverage(
                        score15.toDoubleOrNull() ?: 0.0,
                        score45.toDoubleOrNull() ?: 0.0,
                        scoreMid.toDoubleOrNull() ?: 0.0,
                        scoreFinal.toDoubleOrNull() ?: 0.0
                    )

                    Card(colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.1f))) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Điểm trung bình:", fontWeight = FontWeight.Medium)
                            Text(String.format("%.1f", avg), fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val updates = mapOf(
                        "score15min" to (score15.toDoubleOrNull() ?: 0.0),
                        "score45min" to (score45.toDoubleOrNull() ?: 0.0),
                        "scoreMidterm" to (scoreMid.toDoubleOrNull() ?: 0.0),
                        "scoreFinal" to (scoreFinal.toDoubleOrNull() ?: 0.0),
                        "average" to calculateAverage(
                            score15.toDoubleOrNull() ?: 0.0,
                            score45.toDoubleOrNull() ?: 0.0,
                            scoreMid.toDoubleOrNull() ?: 0.0,
                            scoreFinal.toDoubleOrNull() ?: 0.0
                        ),
                        "updatedAt" to System.currentTimeMillis()
                    )
                    db.collection("grades").document(grade.id).update(updates)
                    showEditDialog = false
                }) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
@Composable
fun GradeTable(
    students: List<Student>,
    grades: List<Grade>,
    onGradeClick: (Grade) -> Unit,
    paddingValues: PaddingValues
) {
    val gradeMap = grades.associateBy { it.studentId }

    // Tính thống kê
    val avgScores = gradeMap.values.map { it.average }.filter { it > 0 }
    val classAvg = if (avgScores.isNotEmpty()) avgScores.average() else 0.0
    val gradedCount = gradeMap.values.count { it.average > 0 }
    val passRate = if (students.isNotEmpty()) (gradeMap.values.count { it.average >= 5 } * 100 / students.size) else 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thống kê
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Điểm TB lớp", String.format("%.1f", classAvg), PrimaryBlue)
                    StatItem("Đã nhập điểm", "$gradedCount/${students.size}", SecondaryGreen)
                    StatItem("Tỷ lệ đạt", "$passRate%", AccentOrange)
                }
            }
        }

        // Header bảng
        item {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("STT", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    Text("Họ tên", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    Text("15'", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                    Text("45'", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                    Text("GK", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                    Text("CK", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                    Text("TB", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        }

        // Danh sách học sinh - SỬA Ở ĐÂY
        items(students) { student ->
            val grade = gradeMap[student.id]
            GradeRow(
                student = student,
                grade = grade,
                index = students.indexOf(student),  // 👈 THÊM DÒNG NÀY
                onRowClick = { if (grade != null) onGradeClick(grade) }
            )
        }
    }
}

@Composable
fun GradeRow(
    student: Student,
    grade: Grade?,
    index: Int,  // 👈 THÊM THAM SỐ NÀY
    onRowClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = grade != null) { onRowClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${index + 1}", modifier = Modifier.weight(0.5f), fontSize = 13.sp)  // 👈 DÙNG index
            Text(student.name, modifier = Modifier.weight(2f), fontSize = 13.sp, maxLines = 1)

            // Điểm 15 phút
            ScoreText(grade?.score15min ?: 0.0, Modifier.weight(1f))
            // Điểm 45 phút
            ScoreText(grade?.score45min ?: 0.0, Modifier.weight(1f))
            // Điểm giữa kỳ
            ScoreText(grade?.scoreMidterm ?: 0.0, Modifier.weight(1f))
            // Điểm cuối kỳ
            ScoreText(grade?.scoreFinal ?: 0.0, Modifier.weight(1f))
            // Điểm TB
            ScoreText(grade?.average ?: 0.0, Modifier.weight(1f), isBold = true)
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 11.sp, color = TextGray, textAlign = TextAlign.Center)
    }
}

// Hàm tính điểm
fun calculateAverage(score15: Double, score45: Double, scoreMid: Double, scoreFinal: Double): Double {
    val total = (score15 * 1) + (score45 * 2) + (scoreMid * 2) + (scoreFinal * 3)
    return if (total > 0) total / 8 else 0.0
}
@Composable
fun ScoreText(score: Double, modifier: Modifier = Modifier, isBold: Boolean = false) {
    val color = when {
        score >= 8.0 -> SecondaryGreen
        score >= 5.0 -> PrimaryBlue
        score > 0 -> AccentOrange
        else -> TextGray
    }

    Text(
        text = if (score > 0) String.format("%.1f", score) else "-",
        modifier = modifier,
        fontSize = 13.sp,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        textAlign = TextAlign.Center
    )
}
