package com.example.teachflow.data.model

data class Assignment(
    val id: String = "",
    val assignmentCode: String = "",
    val courseId: String = "",
    val lessonId: String = "",
    val title: String = "",
    val description: String = "",
    val maxScore: Int = 0,
    val dueDate: Long = 0L,
    val createdAt: Long = 0L
)
