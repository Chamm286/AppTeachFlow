package com.example.teachflow.presentation.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StatData(val icon: String, val value: String, val title: String, val change: String, val color: Color)

@Composable
fun AdminStatCard(stat: StatData, modifier: Modifier = Modifier) {
    Card(modifier = modifier.height(100.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = stat.color.copy(alpha = 0.1f), modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(stat.icon, fontSize = 24.sp) }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(stat.value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = stat.color)
                Text(stat.title, fontSize = 12.sp, color = TextGray)
                Text(stat.change, fontSize = 10.sp, color = if (stat.change.contains("+")) SuccessGreen else ErrorRed)
            }
        }
    }
}

