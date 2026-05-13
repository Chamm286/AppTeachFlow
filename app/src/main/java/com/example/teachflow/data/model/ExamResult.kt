package com.example.teachflow.data.model

data class ExamResult(
    val id: String = "",
    val examId: String = "",
    val examTitle: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val score: Double = 0.0,
    val maxScore: Int = 100,
    val rank: Int = 0,
    val submittedAt: Long = 0L,
    val startedAt: Long = 0L,
    val finishedAt: Long = 0L
)
