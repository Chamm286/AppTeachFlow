// data/model/Attendance.kt
package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Attendance(
    @DocumentId
    val id: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val classId: String = "",
    val className: String = "",
    val date: String = "",
    val status: String = "",  // present, absent, late, permission
    val note: String = "",
    val semester: String = "",
    val week: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)