package com.example.teachflow.data.datastore

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataStoreManager(private val context: Context) {
    
    companion object {
        private const val PREF_NAME = "teachflow_prefs"
        private const val KEY_ONBOARDING = "has_seen_onboarding"
        
        @Volatile
        private var instance: DataStoreManager? = null
        
        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(this) {
                instance ?: DataStoreManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    suspend fun setOnboardingSeen(hasSeen: Boolean) = withContext(Dispatchers.IO) {
        prefs.edit().putBoolean(KEY_ONBOARDING, hasSeen).apply()
    }
    
    suspend fun hasSeenOnboarding(): Boolean = withContext(Dispatchers.IO) {
        prefs.getBoolean(KEY_ONBOARDING, false)
    }
}
