package io.skylabs.omron.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.skylabs.omron.ui.theme.BlueLight
import io.skylabs.omron.ui.theme.CircularIndicatorBackground

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val text by viewModel.text.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val circularIndicatorValue = remember {
            mutableFloatStateOf(100f)
        }

        val colorIndexState = remember { mutableFloatStateOf(0f) }
        val colors = listOf(
            Color(0xFF00f0ff), // 0
            Color(0xFF0079f1), // 1
            Color(0xFFef813a), // 2
            Color(0xFFef4337)  // 3
        )
        val selectedColor = colors[colorIndexState.floatValue.toInt().coerceIn(0, colors.lastIndex)]

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularIndicator(
                startAngle = 48f,
                sweepAngle = 264f,
                color = selectedColor,
                backgroundColor = CircularIndicatorBackground,
                strokeWidth = 30f,
                strokeCap = StrokeCap.Round,
                alphaFrom = 0.4f,
                alphaTo = 1.0f,
                indicatorSize = 326.dp,
                indicatorValue = circularIndicatorValue.floatValue,
                maxIndicator = 100f
            )

            Slider(
                modifier = Modifier.padding(horizontal = 60.dp),
                value = circularIndicatorValue.floatValue,
                onValueChange = {
                    circularIndicatorValue.floatValue = it
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = BlueLight,
                    activeTrackColor = BlueLight,
                    inactiveTrackColor = BlueLight.copy(alpha = 0.3f)
                )
            )

            Slider(
                modifier = Modifier.padding(horizontal = 60.dp),
                value = colorIndexState.floatValue,
                onValueChange = {
                    colorIndexState.floatValue = it
                },
                steps = 2,
                valueRange = 0f..3f,
                colors = SliderDefaults.colors(
                    thumbColor = selectedColor,
                    activeTrackColor = selectedColor,
                    inactiveTrackColor = selectedColor.copy(alpha = 0.3f)
                )
            )
        }
    }
}

/**
 * 6시 방향이 0도.
 */
@Composable
fun CircularIndicator(
    startAngle: Float = 48f,
    sweepAngle: Float = 264f,
    color: Color,
    backgroundColor: Color? = null,
    strokeWidth: Float,
    strokeCap: StrokeCap = StrokeCap.Round,
    alphaFrom: Float = 0.3f,
    alphaTo: Float = 1.0f,
    indicatorSize: Dp,
    indicatorValue: Float,
    maxIndicator: Float
) {
    val availableIndicator = if (indicatorValue <= maxIndicator) indicatorValue else maxIndicator
    val progress = animateFloatAsState(
        targetValue = availableIndicator,
        animationSpec = tween(1000), label = ""
    )

    Box(
        Modifier.size(indicatorSize),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .rotate(90f),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val componentSize = size / 1.25f
                        backgroundColor?.let {
                            circularIndicatorBackground(
                                componentSize = componentSize,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                color = backgroundColor,
                                strokeWidth = strokeWidth,
                                strokeCap = strokeCap
                            )
                        }
                        circularIndicatorForeground(
                            componentSize = componentSize,
                            startAngle = startAngle,
                            sweepAngle = progress.value * sweepAngle / 100,
                            color = color,
                            strokeWidth = strokeWidth,
                            strokeCap = strokeCap,
                            alphaFrom = alphaFrom,
                            alphaTo = alphaTo
                        )
                    }

            )
        }
        Text(
            text = "${progress.value.toInt()}",
            fontSize = 64.sp,
            fontWeight = FontWeight.Thin
        )
    }
}

fun DrawScope.circularIndicatorForeground(
    componentSize: Size,
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
    alphaFrom: Float,
    alphaTo: Float,
) {
    val arcRect = Rect(
        offset = Offset(
            x = (size.width - componentSize.width) / 2,
            y = (size.height - componentSize.height) / 2
        ),
        size = componentSize
    )

    val steps = 360
    val gradientColors = (0 until steps).map { i ->
        val angle = i.toFloat()
        val effectiveRatio = (angle / sweepAngle).coerceAtMost(1f)
        color.copy(alpha = alphaFrom + (alphaTo - alphaFrom) * effectiveRatio)
    }

    drawArc(
        brush = Brush.sweepGradient(
            colors = gradientColors,
            center = arcRect.center
        ),
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = strokeWidth, cap = strokeCap),
        topLeft = arcRect.topLeft,
        size = arcRect.size
    )
}

fun DrawScope.circularIndicatorBackground(
    componentSize: Size,
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    drawArc(
        size = componentSize,
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = strokeWidth,
            cap = strokeCap
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2,
            y = (size.height - componentSize.height) / 2
        )
    )
}






