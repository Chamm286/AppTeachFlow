package com.example.teachflow.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ModernAnimatedBackground(
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    // Gradient rotation animation
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    // Particle animation
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    // Floating shapes animation
    val floatOffset1 by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val floatOffset2 by infiniteTransition.animateFloat(
        initialValue = 50f,
        targetValue = -50f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val floatOffset3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val primaryBlue = Color(0xFF1A73E8)
    val primaryLight = Color(0xFF4285F4)
    val primaryDark = Color(0xFF0D47A1)
    
    val bgStart = if (isDarkMode) Color(0xFF0F172A) else Color(0xFFF0F9FF)
    val bgEnd = if (isDarkMode) Color(0xFF1E1B4B) else Color(0xFFE0F2FE)
    
    Box(modifier = modifier.fillMaxSize()) {
        // Main gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(bgStart, bgEnd)
                    )
                )
        )
        
        // Rotating gradient circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2 + floatOffset1
            val centerY = size.height / 2 + floatOffset2
            val radius = size.width * 0.6f
            
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryBlue.copy(alpha = 0.08f),
                        primaryLight.copy(alpha = 0.04f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = radius
                ),
                radius = radius
            )
        }
        
        // Orbiting circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val rad = Math.toRadians(angle.toDouble())
            val orbitRadius = size.width * 0.4f
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            val x1 = centerX + orbitRadius * cos(rad).toFloat()
            val y1 = centerY + orbitRadius * sin(rad).toFloat()
            val x2 = centerX + orbitRadius * cos(rad + Math.PI).toFloat()
            val y2 = centerY + orbitRadius * sin(rad + Math.PI).toFloat()
            
            drawCircle(
                color = primaryBlue.copy(alpha = 0.1f),
                radius = 80f,
                center = Offset(x1, y1)
            )
            drawCircle(
                color = primaryLight.copy(alpha = 0.08f),
                radius = 60f,
                center = Offset(x2, y2)
            )
        }
        
        // Floating particles
        FloatingParticles(progress = particleProgress)
        
        // Wave lines at bottom
        WaveLines(offset = floatOffset3)
    }
}

@Composable
fun FloatingParticles(progress: Float) {
    val particles = remember {
        List(25) { index ->
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextInt(2, 6),
                speedX = Random.nextFloat() * 0.01f + 0.003f,
                speedY = Random.nextFloat() * 0.01f + 0.002f,
                alpha = Random.nextFloat() * 0.3f + 0.1f
            )
        }
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val animatedX = (particle.x + progress * particle.speedX) % 1f
            val animatedY = (particle.y + progress * particle.speedY) % 1f
            
            drawCircle(
                color = Color(0xFF1A73E8).copy(alpha = particle.alpha),
                radius = particle.size.toFloat(),
                center = Offset(animatedX * size.width, animatedY * size.height)
            )
        }
    }
}

@Composable
fun WaveLines(offset: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        val path1 = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, height - 50f)
            for (x in 0..width.toInt() step 20) {
                val y = height - 40f + (offset * 0.5f).toFloat() * sin(x * 0.02f)
                lineTo(x.toFloat(), y)
            }
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        
        val path2 = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, height - 30f)
            for (x in 0..width.toInt() step 20) {
                val y = height - 20f + (offset * 0.8f).toFloat() * cos(x * 0.025f)
                lineTo(x.toFloat(), y)
            }
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        
        drawPath(
            path = path1,
            color = Color(0xFF1A73E8).copy(alpha = 0.06f)
        )
        drawPath(
            path = path2,
            color = Color(0xFF4285F4).copy(alpha = 0.04f)
        )
    }
}

data class Particle(
    val x: Float,
    val y: Float,
    val size: Int,
    val speedX: Float,
    val speedY: Float,
    val alpha: Float
)
