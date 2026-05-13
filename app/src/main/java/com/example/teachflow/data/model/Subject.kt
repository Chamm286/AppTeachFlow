// data/model/Subject.kt
package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Subject(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val credits: Int = 0,
    val hours: Int = 0,  // THÊM DÒNG NÀY
    val topics: List<String> = emptyList(),  // THÊM DÒNG NÀY
    val gradeLevel: List<String> = emptyList(),
    val department: String = "",  // THÊM DÒNG NÀY - Khoa
    val description: String = ""  // THÊM DÒNG NÀY
)