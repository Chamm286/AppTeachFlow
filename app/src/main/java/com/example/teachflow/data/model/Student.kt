package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Student(
    @DocumentId
    val id: String = "",
    val studentCode: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: String = "",
    val gender: String = "",
    val birthday: String = "",
    val address: String = "",
    val schoolId: String = "",
    val schoolName: String = "",
    val classId: String = "",
    val className: String = "",
    val major: String = "",
    val homeroomTeacherId: String = "",
    val homeroomTeacherName: String = "",
    val parentName: String = "",
    val parentPhone: String = "",
    val parentEmail: String = "",  // THÊM
    val notes: String = "",        // THÊM
    val locationName: String = "",  // THÊM
    val classIds: List<String> = emptyList(),  // THÊM
    val location: String = "",      // THÊM
    val enrollmentYear: Int = 0,
    val status: String = "active",
    val averageScore: Double = 0.0,
    val ranking: String = "",
    val conduct: String = "",
    val extracurricular: List<String> = emptyList(),
    val awards: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
