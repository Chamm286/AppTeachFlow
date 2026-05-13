package com.example.teachflow.core

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object AvatarManager {
    private const val PREFS_NAME = "teachflow_prefs"
    private const val KEY_AVATAR_PATH = "avatar_path"
    
    fun saveAvatarUri(context: Context, uri: Uri): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val avatarPath = uri.toString()
        prefs.edit().putString(KEY_AVATAR_PATH, avatarPath).apply()
        return avatarPath
    }
    
    fun getAvatarUri(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AVATAR_PATH, null)
    }
    
    fun saveAvatarBitmap(context: Context, bitmap: Bitmap): String {
        val filename = "avatar_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_AVATAR_PATH, file.absolutePath).apply()
        return file.absolutePath
    }
    
    fun clearAvatar(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_AVATAR_PATH).apply()
    }
}
