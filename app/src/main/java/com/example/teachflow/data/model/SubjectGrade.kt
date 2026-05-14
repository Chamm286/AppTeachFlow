package com.example.teachflow.data.model


data class SubjectGrade(
    val subjectId: String,
    val subjectName: String,
    val scores: List<Subject>,
    val average: Float,
    val coefficient: Int = 1
)