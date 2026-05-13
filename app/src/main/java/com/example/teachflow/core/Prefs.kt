package com.example.teachflow.core

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("teachflow_prefs", Context.MODE_PRIVATE)
    
    fun isFirstTime(): Boolean = prefs.getBoolean("first_time", true)
    
    fun setFirstTimeDone() {
        prefs.edit().putBoolean("first_time", false).apply()
    }
    
    companion object {
        @Volatile
        private var instance: Prefs? = null
        
        fun getInstance(context: Context): Prefs {
            return instance ?: synchronized(this) {
                instance ?: Prefs(context.applicationContext).also { instance = it }
            }
        }
    }
}
