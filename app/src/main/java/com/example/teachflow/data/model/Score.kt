package com.example.teachflow.data.model

data class Score(
    val id: String = "",
    val name: String,
    val score: Float,
    val coefficient: Int,
    val date: String,
    val type: String,
    val isFinalized: Boolean = false

)
