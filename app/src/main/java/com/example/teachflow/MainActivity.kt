// MainActivity.kt
package com.example.teachflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.teachflow.core.Prefs
import com.example.teachflow.core.RepoHolder
import com.example.teachflow.data.remote.Firebase
import com.example.teachflow.data.repo.AppRepo
import com.example.teachflow.presentation.navigation.NavigationGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Prefs
        Prefs.getInstance(this)

        // Khởi tạo Data Sources & Repository (Dependency Injection thủ công)
        val firebaseService = Firebase()
        val repo = AppRepo(firebaseService)
        RepoHolder.init(repo)

        // CHẠY SEED DATA - Bỏ comment nếu muốn seed dữ liệu lần đầu
        // runBlocking {
        //     Log.d("SEED", "🚀 Bắt đầu seed data...")
        //     SeedData.seedAllData()
        //     Log.d("SEED", "✅ Seed data hoàn tất!")
        // }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}
