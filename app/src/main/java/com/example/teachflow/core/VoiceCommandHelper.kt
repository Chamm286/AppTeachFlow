package com.example.teachflow.core

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.*
import kotlinx.coroutines.tasks.await

class VoiceCommandHelper(private val context: Context) {
    
    fun getVoiceInputIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói lệnh của bạn...")
        }
    }
    
    fun processVoiceCommand(command: String): VoiceActionResult {
        return when {
            command.contains("điểm", ignoreCase = true) -> VoiceActionResult("grade", "Xem bảng điểm")
            command.contains("lớp", ignoreCase = true) -> VoiceActionResult("class", "Mở lớp học")
            command.contains("bài tập", ignoreCase = true) -> VoiceActionResult("task", "Bài tập")
            command.contains("thông báo", ignoreCase = true) -> VoiceActionResult("notify", "Thông báo")
            else -> VoiceActionResult("unknown", "Không hiểu lệnh")
        }
    }
}

data class VoiceActionResult(val action: String, val message: String)
