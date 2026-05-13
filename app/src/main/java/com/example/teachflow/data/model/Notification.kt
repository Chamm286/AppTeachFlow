// data/model/Notification.kt
package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class Notification(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val type: String = "",  // exam, event, announcement, grade
    val targetRoles: List<String> = emptyList(),  // admin, teacher, student, parent
    val targetClassIds: List<String> = emptyList(),
    val senderId: String = "",
    val senderName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)