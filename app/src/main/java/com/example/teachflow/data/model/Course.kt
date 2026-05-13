package com.example.teachflow.data.model

data class Course(
    val id: String = "",
    val courseCode: String = "",
    val name: String = "",
    val description: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val schoolId: String = "",
    val grade: String = "",
    val semester: String = "",
    val thumbnail: String = "",
    val price: Double = 0.0,
    val enrolledCount: Int = 0,
    val lessonCount: Int = 0,
    val status: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
