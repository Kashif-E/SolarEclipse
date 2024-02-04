package com.kashif.yyyyeeeeeehhhhhhhaaaaa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.kashif.yyyyeeeeeehhhhhhhaaaaa.helper.SensorDataManager
import com.kashif.yyyyeeeeeehhhhhhhaaaaa.theme.AppTheme
import kotlinx.coroutines.flow.receiveAsFlow

val colorsList = listOf(
    Color(0xFFF4143C),
    Color(0xFFEA8CA5),
    Color(0xFF251473),
    Color(0xFF726CA0),
    Color(0xFFD0ADB4),
    Color(0xFF544C94)
)


@Composable
fun App() = AppTheme {
    val dataManager = remember { SensorDataManager() }

    Box(modifier = Modifier.fillMaxSize()) {
        DisposableEffect(Unit) {
            onDispose {
                dataManager.cancelSensorUpdates()
            }
        }

        val data by dataManager.data.receiveAsFlow().collectAsState(null)

        // Glowing Sun remains stationary
        GlowingSun()

        data?.let { sensorData ->
            DarkSun(
                modifier = Modifier
                    .offset(x = (sensorData.roll).dp, y = (-sensorData.pitch).dp)

            )
        }
    }
}

@Composable
fun GlowingSun(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val sunRadius = size.minDimension / 8
            val gradientRadius = sunRadius * 3


            val glowPaint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                shader = RadialGradientShader(
                    center = Offset(centerX, centerY),
                    radius = gradientRadius,
                    colors = listOf(Color(0xFFFFD700), Color.Transparent),
                    tileMode = TileMode.Clamp
                )
            }

            // Draw the glow around the sun
            drawIntoCanvas { canvas ->
                canvas.drawCircle(
                    Offset(centerX, centerY),
                    gradientRadius,
                    glowPaint.asComposePaint()
                )
            }


            val sunPaint = Paint().apply {
                color = Color.Yellow
            }

            // Draw the sun's core
            drawIntoCanvas {
                it.drawCircle(
                    center = Offset(centerX, centerY),
                    radius = sunRadius,
                    sunPaint
                )
            }
        }
    }
}


@Composable
fun DarkSun(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val sunRadius = size.minDimension / 8
            val gradientRadius = sunRadius * 3


            val glowPaint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                shader = RadialGradientShader(
                    center = Offset(centerX, centerY),
                    radius = gradientRadius,
                    colors = listOf(Color.Black, Color.Transparent),
                    tileMode = TileMode.Clamp
                )
            }

            drawIntoCanvas { canvas ->
                canvas.drawCircle(
                    Offset(centerX, centerY),
                    gradientRadius,
                    glowPaint.asComposePaint()
                )
            }

            val sunPaint = Paint().apply {
                color = Color.Black
            }

            drawIntoCanvas {
                it.drawCircle(
                    center = Offset(centerX, centerY),
                    radius = sunRadius,
                    sunPaint
                )
            }
        }
    }
}
