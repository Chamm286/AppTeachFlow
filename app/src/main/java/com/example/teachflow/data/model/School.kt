// data/model/School.kt
package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class School(
    @DocumentId
    val id: String = "",
    val schoolCode: String = "",
    val name: String = "",
    val shortName: String = "",  // THÊM DÒNG NÀY
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val website: String = "",
    val logo: String = "",
    val principal: String = "",
    val foundedYear: Int = 0,
    val studentCount: Int = 0,
    val teacherCount: Int = 0,
    val classCount: Int = 0,
    val lat: Double = 0.0,  // Vĩ độ cho map
    val lng: Double = 0.0,  // Kinh độ cho map
    val status: String = "active",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)