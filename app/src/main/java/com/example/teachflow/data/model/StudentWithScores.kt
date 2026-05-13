package com.example.teachflow.data.model

data class StudentWithScores(
    val student: Student,
    val scores: List<Grade> = emptyList(),
    val averageScore: Float = 0f,
    val rank: String = ""
)
