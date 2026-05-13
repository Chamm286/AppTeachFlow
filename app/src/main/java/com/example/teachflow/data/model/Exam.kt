package com.example.teachflow.data.model

data class Exam(
    val id: String = "",
    val examCode: String = "",
    val title: String = "",
    val description: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val schoolId: String = "",
    val grade: String = "",
    val semester: String = "",
    val examType: String = "",
    val duration: Int = 0,
    val maxScore: Int = 0,
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val createdAt: Long = 0L
)
