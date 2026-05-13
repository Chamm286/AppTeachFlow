package com.example.teachflow.data.model

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val userId: String = ""
)
