package com.example.teachflow.presentation.admin.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.teachflow.presentation.admin.components.*

data class SettingItem(val icon: String, val title: String, val desc: String)

@Composable
fun SettingsTab(padding: PaddingValues, navController: NavController) {
    val settings = listOf(
        SettingItem("🔔", "Thông báo", "Quản lý thông báo hệ thống"),
        SettingItem("🔒", "Bảo mật", "Đổi mật khẩu, xác thực 2 lớp"),
        SettingItem("💾", "Sao lưu dữ liệu", "Sao lưu và khôi phục dữ liệu"),
        SettingItem("🌐", "Ngôn ngữ", "Tiếng Việt / English"),
        SettingItem("📊", "Báo cáo", "Xuất báo cáo thống kê")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("⚙️ Cài đặt hệ thống", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Quản lý cấu hình hệ thống", fontSize = 13.sp, color = TextGray)
        }
        items(settings) { setting ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(setting.icon, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(setting.title, fontWeight = FontWeight.SemiBold, color = TextDark)
                        Text(setting.desc, fontSize = 12.sp, color = TextGray)
                    }
                    Switch(
                        checked = setting.title == "Thông báo",
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NavyPrimary,
                            checkedTrackColor = NavyPrimary.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}



