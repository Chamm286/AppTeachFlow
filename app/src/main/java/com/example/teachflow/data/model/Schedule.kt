package com.example.teachflow.data.model

data class Schedule(
    val id: String = "",
    val classId: String = "",
    val className: String = "",
    val dayOfWeek: Int = 0, // 2: Thứ 2, 3: Thứ 3, ..., 7: Thứ 7, 8: Chủ nhật
    val period: Int = 0,
    val subjectId: String = "",
    val subjectName: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val room: String = ""
)
