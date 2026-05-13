package com.example.teachflow.data.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String = "",
    val uid: String = "",
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val fullName: String = "",
    val role: String = "",
    val loginType: String = "email",
    val phone: String = "",
    val avatar: String = "",
    val avatarUrl: String = "",
    val avatarResId: String = "",
    val gender: String = "",
    val birthday: String = "",
    val address: String = "",
    val schoolId: String = "",
    val schoolName: String = "",
    val profileId: String = "",
    val teacherCode: String = "",
    val studentCode: String = "",
    val isActive: Boolean = true,  // CHỈ GIỮ LẠI isActive, XÓA active
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
