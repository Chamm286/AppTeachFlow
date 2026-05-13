// data/model/Class.kt
package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Class(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val grade: String = "",
    val subject: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val credits: Int = 0,  // THÊM DÒNG NÀY
    val studentCount: Int = 0,
    val studentIds: List<String> = emptyList(),
    val room: String = "",
    val schedule: String = "",
    val schoolId: String = "",
    val schoolName: String = "",
    val academicYear: String = "",
    val status: String = "active",
    val startDate: Long = 0L,  // THÊM DÒNG NÀY
    val endDate: Long = 0L     // THÊM DÒNG NÀY
)