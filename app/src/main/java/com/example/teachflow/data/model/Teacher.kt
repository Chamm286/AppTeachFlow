package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Teacher(
    @DocumentId
    val id: String = "",
    val teacherCode: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatar: String = "",
    val gender: String = "",
    val birthday: String = "",
    val address: String = "",
    val yearsOfExperience: Int = 0,
    val qualifications: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val subjects: List<String> = emptyList(),
    val position: String = "",
    val schoolId: String = "",
    val schoolName: String = "",
    val officeRoom: String = "",
    val consultationHours: String = "",
    val classes: List<String> = emptyList(),        // THÊM - danh sách lớp đang dạy
    val roomLocation: String = "",                   // THÊM
    val homeroomClassId: String = "",                // THÊM - lớp chủ nhiệm
    val homeroomClassName: String = "",              // THÊM - tên lớp chủ nhiệm
    val locationName: String = "",                   // THÊM
    val schedule: String = "",                       // THÊM
    val location: String = "",                       // THÊM
    val status: String = "active",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
