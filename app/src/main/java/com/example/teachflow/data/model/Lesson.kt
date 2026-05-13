package com.example.teachflow.data.model

data class Lesson(
    val id: String = "",
    val lessonCode: String = "",
    val courseId: String = "",
    val title: String = "",
    val content: String = "",
    val videoUrl: String = "",
    val duration: Int = 0,
    val orderIndex: Int = 0,
    val attachmentUrls: List<String> = emptyList(),
    val isFree: Boolean = false,
    val createdAt: Long = 0L
)
