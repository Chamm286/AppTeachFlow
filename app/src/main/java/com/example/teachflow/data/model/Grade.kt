// data/model/Grade.kt
package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Grade(
    @DocumentId
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val classId: String = "",
    val className: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val score15min: Double = 0.0,   // Điểm 15 phút
    val score45min: Double = 0.0,   // Điểm 45 phút/1 tiết
    val scoreMidterm: Double = 0.0, // Điểm giữa kỳ
    val scoreFinal: Double = 0.0,   // Điểm cuối kỳ
    val average: Double = 0.0,      // Điểm trung bình
    val semester: String = "",      // Học kỳ (HK1, HK2)
    val year: Int = 0,              // Năm học
    val rank: Int = 0,              // Thứ hạng
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)