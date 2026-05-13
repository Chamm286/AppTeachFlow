package com.example.teachflow.presentation.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

// Màu sắc chủ đạo - Xanh dương chuyên nghiệp
val PrimaryBlue = Color(0xFF1A73E8)
val PrimaryLight = Color(0xFF4285F4)
val PrimaryDark = Color(0xFF0D47A1)

data class OnboardingItem(
    val icon: String,
    val title: String,
    val description: String,
    val gradient: List<Color>
)

val onboardingItems = listOf(
    OnboardingItem(
        icon = "📚",
        title = "Quản lý lớp học",
        description = "Tạo và quản lý lớp học, theo dõi sĩ số, điểm danh dễ dàng mọi lúc mọi nơi",
        gradient = listOf(Color(0xFF1A73E8), Color(0xFF4285F4))
    ),
    OnboardingItem(
        icon = "📊",
        title = "Bảng điểm thông minh",
        description = "Nhập điểm, tính điểm trung bình, xếp hạng học sinh tự động và chính xác",
        gradient = listOf(Color(0xFF34A853), Color(0xFF0F9D58))
    ),
    OnboardingItem(
        icon = "🎯",
        title = "Học tập tương tác",
        description = "Giao bài tập, làm quiz, theo dõi tiến độ học tập với AI thông minh",
        gradient = listOf(Color(0xFFFBBC05), Color(0xFFF9A825))
    )
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pagerState = rememberPagerState { onboardingItems.size }
    val scope = rememberCoroutineScope()
    
    // Auto slide
    LaunchedEffect(Unit) {
        while (pagerState.currentPage < onboardingItems.size - 1) {
            delay(4000)
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Skip button
            Text(
                text = "Skip",
                fontSize = 14.sp,
                color = PrimaryBlue,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(24.dp)
                    .clickable { onComplete() }
            )
            
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val item = onboardingItems[page]
                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                val scaleValue = 1f - abs(pageOffset) * 0.1f
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .scale(scaleValue.coerceIn(0.85f, 1f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Icon with gradient background
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(scaleValue.coerceIn(0.85f, 1f))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        item.gradient[0].copy(alpha = 0.1f),
                                        item.gradient[1].copy(alpha = 0.05f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.icon, fontSize = 72.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    Text(
                        text = item.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF202124),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = item.description,
                        fontSize = 16.sp,
                        color = Color(0xFF5F6368),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }
            
            // Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(onboardingItems.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (pagerState.currentPage == index) 28.dp else 8.dp,
                                height = 8.dp
                            )
                            .padding(horizontal = 4.dp)
                            .background(
                                if (pagerState.currentPage == index) PrimaryBlue else Color(0xFFE8EAED),
                                CircleShape
                            )
                    )
                }
            }
            
            // Get Started button
            if (pagerState.currentPage == onboardingItems.size - 1) {
                Button(
                    onClick = onComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text(
                        "GET STARTED",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                }
            } else {
                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue.copy(alpha = 0.9f)
                    )
                ) {
                    Text(
                        "NEXT",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
