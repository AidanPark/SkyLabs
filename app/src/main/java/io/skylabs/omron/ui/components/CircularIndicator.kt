package io.skylabs.omron.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.lerp


/**
 * 6시 방향이 0도.
 */
@Composable
fun CircularIndicator(
    modifier: Modifier = Modifier,
    startAngle: Float = 48f,
    sweepAngle: Float = 264f,
    startColor: Color,
    midColor: Color? = null,
    endColor: Color? = null,
    backgroundColor: Color? = null,
    strokeWidth: Float,
    strokeCap: StrokeCap = StrokeCap.Round,
    alphaFrom: Float = 0.3f,
    alphaTo: Float = 1.0f,
    indicatorSize: Dp,
    indicatorValue: Float,
    maxIndicator: Float,
    innerContent: (@Composable (progress: Int) -> Unit)? = null
) {
    val availableIndicator = if (indicatorValue <= maxIndicator) indicatorValue else maxIndicator
    val progress = animateFloatAsState(
        targetValue = availableIndicator,
        animationSpec = tween(1000), label = ""
    )

    Box(
        modifier.size(indicatorSize),
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
                            startColor = startColor,
                            midColor = midColor,
                            endColor = endColor,
                            strokeWidth = strokeWidth,
                            strokeCap = strokeCap,
                            alphaFrom = alphaFrom,
                            alphaTo = alphaTo
                        )
                    }

            )
        }
        innerContent?.let {
            it(progress.value.toInt())
        }
    }
}

fun DrawScope.circularIndicatorForeground(
    componentSize: Size,
    startAngle: Float,
    sweepAngle: Float,
    startColor: Color,
    midColor: Color? = null,
    endColor: Color? = null,
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
    val gradientColors = when {
        midColor != null && endColor != null -> {
            // 🔷 3색 그라데이션: start → mid → end
            (0 until steps).map { i ->
                val ratio = i.toFloat() / steps
                val color = if (ratio <= 0.5f) {
                    // 첫 절반: start → mid
                    val t = ratio / 0.5f
                    lerpColor(startColor, midColor, t)
                } else {
                    // 후반: mid → end
                    val t = (ratio - 0.5f) / 0.5f
                    lerpColor(midColor, endColor, t)
                }
                color.copy(alpha = lerp(alphaFrom, alphaTo, ratio))
            }
        }

        endColor != null -> {
            // 🔷 2색 그라데이션: start → end
            (0 until steps).map { i ->
                val ratio = i.toFloat() / steps
                lerpColor(startColor, endColor, ratio)
                    .copy(alpha = lerp(alphaFrom, alphaTo, ratio))
            }
        }

        else -> {
            // 🔹 단색: startColor만
            (0 until steps).map { i ->
                val ratio = i.toFloat() / steps
                startColor.copy(alpha = lerp(alphaFrom, alphaTo, ratio))
            }
        }
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


fun lerpColor(start: Color, end: Color, t: Float): Color {
    return Color(
        red = lerp(start.red, end.red, t),
        green = lerp(start.green, end.green, t),
        blue = lerp(start.blue, end.blue, t),
        alpha = lerp(start.alpha, end.alpha, t) // 기본 알파도 고려 (단, 위에서 오버라이드됨)
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








