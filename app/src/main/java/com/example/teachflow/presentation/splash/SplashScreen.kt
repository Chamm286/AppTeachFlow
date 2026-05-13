package com.example.teachflow.presentation.splash

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Colors - Bảng màu TeachFlow
    val primaryBlue = Color(0xFF2563EB)
    val primaryDark = Color(0xFF1E40AF)
    val accentTeal = Color(0xFF10B981)
    val warmOrange = Color(0xFFF59E0B)
    val whitePure = Color.White
    val lightBg = Color(0xFFEFF6FF)

    // Slogan
    val slogans = listOf(
        "Học tập thông minh • Mọi lúc",
        "Bảng điểm • 1 chạm",
        "Quản lý lớp học • Dễ dàng",
        "Bài tập tương tác • Hấp dẫn",
        "Kết nối giáo viên • Nhanh chóng"
    )
    var currentSloganIndex by remember { mutableStateOf(0) }

    // Animations
    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleSlide = remember { Animatable(50f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val ringScale = remember { Animatable(0f) }
    val particleProgress = remember { Animatable(0f) }
    val statsSlide = remember { Animatable(100f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2200)
            currentSloganIndex = (currentSloganIndex + 1) % slogans.size
        }
    }

    LaunchedEffect(Unit) {
        logoScale.animateTo(1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ))
        logoAlpha.animateTo(1f, animationSpec = tween(600))
        ringScale.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        titleSlide.animateTo(0f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ))
        titleAlpha.animateTo(1f, animationSpec = tween(500))
        subtitleAlpha.animateTo(1f, animationSpec = tween(600))
        statsSlide.animateTo(0f, animationSpec = tween(700, easing = FastOutSlowInEasing))
        particleProgress.animateTo(1f, animationSpec = tween(1500))
        delay(2800)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(primaryBlue, primaryDark, Color(0xFF0F172A)),
                    startY = 0f,
                    endY = 1f
                )
            )
    ) {
        // Pattern background
        EducationPatternBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.weight(0.25f))

            // Logo với rings
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                val ringAlpha by animateFloatAsState(
                    targetValue = if (ringScale.value > 0.5f) 0.6f else 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Canvas(modifier = Modifier
                    .size(170.dp)
                    .scale(ringScale.value)
                ) {
                    drawCircle(
                        color = accentTeal.copy(alpha = ringAlpha * 0.5f),
                        radius = size.width / 2f,
                        style = Stroke(width = 2f)
                    )
                    drawCircle(
                        color = warmOrange.copy(alpha = ringAlpha * 0.3f),
                        radius = size.width / 2f + 10f,
                        style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f)))
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value),
                    shape = CircleShape,
                    color = Color.Transparent,
                    shadowElevation = 16.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(whitePure, lightBg),
                                    radius = 1f
                                )
                            )
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📚", fontSize = 52.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name
            ShimmerText(
                text = "TeachFlow",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = whitePure,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .offset(y = titleSlide.value.dp)
                    .alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hệ thống quản lý giáo dục thông minh",
                fontSize = 14.sp,
                color = whitePure.copy(alpha = 0.85f),
                letterSpacing = 1.sp,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Feature stats
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .alpha(subtitleAlpha.value)
                    .offset(y = statsSlide.value.dp)
            ) {
                FeatureStatTeachFlow(
                    icon = "📚",
                    value = "500+",
                    label = "Lớp học",
                    color = accentTeal
                )
                FeatureStatTeachFlow(
                    icon = "👨‍🏫",
                    value = "1000+",
                    label = "Giáo viên",
                    color = warmOrange
                )
                FeatureStatTeachFlow(
                    icon = "🎓",
                    value = "10K+",
                    label = "Học sinh",
                    color = accentTeal
                )
            }

            Spacer(modifier = Modifier.weight(0.4f))

            // Rotating slogan
            val sloganFade by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(400),
                label = "sloganFade"
            )

            Surface(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(sloganFade * subtitleAlpha.value),
                shape = RoundedCornerShape(40.dp),
                color = Color.White.copy(alpha = 0.12f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✨", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = slogans[currentSloganIndex],
                        fontSize = 13.sp,
                        color = whitePure,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Loading
            EducationLoadingIndicator(subtitleAlpha.value)

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Floating particles
        EducationFloatingParticles(particleProgress.value, screenWidth, screenHeight)

        // Corner decorations
        EducationCornerDecorations()
    }
}

@Composable
fun ShimmerText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight,
    color: Color,
    letterSpacing: androidx.compose.ui.unit.TextUnit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = modifier) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color,
            letterSpacing = letterSpacing,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color(0xFF2563EB).copy(alpha = 0.5f),
                    blurRadius = 12f,
                    offset = Offset(0f, 4f)
                )
            )
        )
        Canvas(modifier = Modifier.matchParentSize()) {
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.White.copy(alpha = 0.6f),
                    Color.Transparent
                ),
                start = Offset(shimmerOffset, 0f),
                end = Offset(shimmerOffset + 150f, 0f)
            )
            drawRect(brush = gradient, blendMode = BlendMode.Plus)
        }
    }
}

@Composable
fun EducationPatternBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val patternSize = 60f
        for (x in (0..(width / patternSize).toInt())) {
            for (y in (0..(height / patternSize).toInt())) {
                val centerX = x * patternSize + patternSize / 2
                val centerY = y * patternSize + patternSize / 2
                drawLine(
                    color = Color.White.copy(alpha = 0.03f),
                    start = Offset(centerX, centerY - 12f),
                    end = Offset(centerX, centerY + 12f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.03f),
                    start = Offset(centerX - 12f, centerY),
                    end = Offset(centerX + 12f, centerY),
                    strokeWidth = 2f
                )
            }
        }
    }
}

@Composable
fun FeatureStatTeachFlow(
    icon: String,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun EducationLoadingIndicator(visible: Float) {
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.alpha(visible)
    ) {
        Canvas(modifier = Modifier
            .width(120.dp)
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
        ) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.2f),
                size = size,
                cornerRadius = CornerRadius(2f)
            )
            drawRoundRect(
                color = Color(0xFF10B981),
                size = Size(size.width * progress, size.height),
                cornerRadius = CornerRadius(2f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Đang khởi tạo...",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun EducationFloatingParticles(progress: Float, screenWidth: androidx.compose.ui.unit.Dp, screenHeight: androidx.compose.ui.unit.Dp) {
    val particles = remember {
        List(30) { index ->
            EducationParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextInt(2, 5),
                speedX = Random.nextFloat() * 0.015f + 0.005f,
                speedY = Random.nextFloat() * 0.015f + 0.003f,
                alpha = Random.nextFloat() * 0.3f + 0.1f,
                shape = Random.nextInt(0, 3)
            )
        }
    }

    particles.forEach { particle ->
        val animatedX = (particle.x + progress * particle.speedX) % 1f
        val animatedY = (particle.y + progress * particle.speedY) % 1f

        when (particle.shape) {
            1 -> {
                Box(
                    modifier = Modifier
                        .offset(
                            x = (animatedX * screenWidth.value).dp,
                            y = (animatedY * screenHeight.value).dp
                        )
                        .size(particle.size.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = Color.White.copy(alpha = particle.alpha),
                            start = Offset(center.x, 0f),
                            end = Offset(center.x, size.height),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = Color.White.copy(alpha = particle.alpha),
                            start = Offset(0f, center.y),
                            end = Offset(size.width, center.y),
                            strokeWidth = 1f
                        )
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .offset(
                            x = (animatedX * screenWidth.value).dp,
                            y = (animatedY * screenHeight.value).dp
                        )
                        .size(particle.size.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = particle.alpha))
                )
            }
        }
    }
}

@Composable
fun EducationCornerDecorations() {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .align(Alignment.TopStart)
            .size(60.dp)
            .padding(16.dp)
        ) {
            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(center.x, center.y - 15f),
                end = Offset(center.x, center.y + 15f),
                strokeWidth = 2f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(center.x - 15f, center.y),
                end = Offset(center.x + 15f, center.y),
                strokeWidth = 2f
            )
        }

        Canvas(modifier = Modifier
            .align(Alignment.TopEnd)
            .size(60.dp)
            .padding(16.dp)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height / 2)
                lineTo(size.width * 0.3f, size.height / 2)
                lineTo(size.width * 0.4f, size.height / 2 - 10f)
                lineTo(size.width * 0.5f, size.height / 2 + 8f)
                lineTo(size.width * 0.6f, size.height / 2 - 10f)
                lineTo(size.width * 0.7f, size.height / 2)
                lineTo(size.width, size.height / 2)
            }
            drawPath(
                path = path,
                color = Color.White.copy(alpha = 0.15f),
                style = Stroke(width = 2f)
            )
        }

        Canvas(modifier = Modifier
            .align(Alignment.BottomStart)
            .size(60.dp)
            .padding(16.dp)
        ) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.15f),
                topLeft = Offset(0f, size.height / 2 - 8f),
                size = Size(size.width, 16f),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }

        Canvas(modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(60.dp)
            .padding(16.dp)
        ) {
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 8f,
                center = center,
                style = Stroke(width = 2f)
            )
        }
    }
}

data class EducationParticle(
    val x: Float,
    val y: Float,
    val size: Int,
    val speedX: Float,
    val speedY: Float,
    val alpha: Float,
    val shape: Int
)
