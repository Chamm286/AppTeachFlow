package com.example.teachflow.data.model

data class GradeTable(
    val classInfo: Class,
    val columns: List<GradeColumn>,
    val students: List<StudentWithScores>
)
