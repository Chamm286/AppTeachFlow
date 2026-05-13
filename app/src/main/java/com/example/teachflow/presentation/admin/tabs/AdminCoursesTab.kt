package com.example.teachflow.presentation.admin.tabs

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.model.Course
import com.example.teachflow.data.model.Teacher
import com.example.teachflow.presentation.admin.*
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCoursesTab(padding: PaddingValues, navController: NavController) {
    val scope = rememberCoroutineScope()
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var teachers by remember { mutableStateOf<List<Teacher>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    
    var showCourseDialog by remember { mutableStateOf(false) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var courseToDelete by remember { mutableStateOf<Course?>(null) }

    fun loadData() {
        scope.launch {
            isLoading = true
            try {
                courses = RepoHolder.repo.getAllCourses().sortedByDescending { it.createdAt }
                teachers = RepoHolder.repo.getAllTeachers()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadData() }

    val filteredCourses = if (searchQuery.isEmpty()) courses else courses.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        (it.courseCode.contains(searchQuery, ignoreCase = true)) ||
        (it.teacherName.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize().padding(padding).background(BgGray)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("📚 Quản lý khóa học", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Hệ thống có ${courses.size} khóa học", fontSize = 13.sp, color = TextGray)
            }
            Button(
                onClick = { 
                    selectedCourse = null
                    showCourseDialog = true 
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Thêm mới", color = Color.White)
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Tìm theo tên, mã khóa học, giảng viên...") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextGray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = NavyPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NavyPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCourses) { course ->
                    CourseItem(
                        course = course,
                        onEdit = {
                            selectedCourse = course
                            showCourseDialog = true
                        },
                        onDelete = {
                            courseToDelete = course
                            showDeleteConfirm = true
                        }
                    )
                }
            }
        }
    }

    if (showCourseDialog) {
        CourseDialog(
            course = selectedCourse,
            teachers = teachers,
            onDismiss = { showCourseDialog = false },
            onConfirm = { updatedCourse ->
                scope.launch {
                    if (selectedCourse == null) {
                        RepoHolder.repo.createCourse(updatedCourse)
                    } else {
                        RepoHolder.repo.updateCourse(updatedCourse)
                    }
                    showCourseDialog = false
                    loadData()
                }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Xác nhận xóa", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có muốn xóa khóa học '${courseToDelete?.name}'? Dữ liệu liên quan có thể bị ảnh hưởng.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            courseToDelete?.let { RepoHolder.repo.deleteCourse(it.id) }
                            showDeleteConfirm = false
                            loadData()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Xóa ngay", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Hủy") }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun CourseItem(
    course: Course,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NavyPrimary.copy(alpha = 0.1f),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("📚", fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(course.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                    Text("Mã: ${course.courseCode}", fontSize = 12.sp, color = TextGray)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (course.status == "active") SuccessGreen.copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.2f)
                ) {
                    Text(
                        if (course.status == "active") "Active" else "Draft",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (course.status == "active") SuccessGreen else TextGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    LabelValue("👨‍🏫 Giảng viên", course.teacherName)
                    LabelValue("💰 Giá", "${course.price}đ")
                }
                Column {
                    LabelValue("👥 Học viên", "${course.enrolledCount}")
                    LabelValue("📖 Bài học", "${course.lessonCount}")
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = BgGray)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Outlined.Edit, null, tint = NavyPrimary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, null, tint = ErrorRed)
                }
            }
        }
    }
}

@Composable
fun LabelValue(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 11.sp, color = TextGray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDark)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDialog(
    course: Course?,
    teachers: List<Teacher>,
    onDismiss: () -> Unit,
    onConfirm: (Course) -> Unit
) {
    var name by remember { mutableStateOf(course?.name ?: "") }
    var code by remember { mutableStateOf(course?.courseCode ?: "") }
    var price by remember { mutableStateOf(course?.price?.toString() ?: "0") }
    var status by remember { mutableStateOf(course?.status ?: "active") }
    var selectedTeacherId by remember { mutableStateOf(course?.teacherId ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (course == null) "Thêm khóa học" else "Sửa khóa học", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên khóa học") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Mã khóa học") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Giá tiền") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                Text("Trạng thái", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = status == "active",
                        onClick = { status = "active" },
                        label = { Text("Active") }
                    )
                    FilterChip(
                        selected = status == "draft",
                        onClick = { status = "draft" },
                        label = { Text("Draft") }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalCourse = course?.copy(
                        name = name,
                        courseCode = code,
                        price = price.toDoubleOrNull() ?: 0.0,
                        status = status,
                        updatedAt = System.currentTimeMillis()
                    ) ?: Course(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        courseCode = code,
                        price = price.toDoubleOrNull() ?: 0.0,
                        status = status,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    onConfirm(finalCourse)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                enabled = name.isNotBlank() && code.isNotBlank()
            ) { Text("Lưu", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}
