package io.skylabs.omron.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.skylabs.omron.R
import io.skylabs.omron.ui.components.CircularIndicator
import io.skylabs.omron.ui.theme.BackgroundDark
import io.skylabs.omron.ui.theme.BlueLight
import io.skylabs.omron.ui.theme.CircularIndicatorBackground

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val text by viewModel.text.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
//        contentAlignment = Alignment.CenterHorizontally
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
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.header),
                    contentDescription = "Header Image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Image(
                    painter = painterResource(id = R.drawable.sleepscore),
                    contentDescription = "sleepscore Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp),
                    contentScale = ContentScale.FillWidth
                )

                CircularIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 38.dp),
                    startAngle = 48f,
                    sweepAngle = 264f,
                    startColor = selectedColor,
                    backgroundColor = CircularIndicatorBackground,
                    strokeWidth = 29f,
                    strokeCap = StrokeCap.Round,
                    alphaFrom = 0.4f,
                    alphaTo = 1.0f,
                    indicatorSize = 326.dp,
                    indicatorValue = circularIndicatorValue.floatValue,
                    maxIndicator = 100f,
                    innerContent = { value ->
                        Text(
                            text = "$value",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.White
                        )
                    }
                )

                CircularIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 26.dp),
                    startAngle = 48f,
                    sweepAngle = 264f,
                    startColor = Color(0xFF015db6),
                    midColor = BackgroundDark,
                    endColor = Color(0xFF97525d),
                    strokeWidth = 6f,
                    strokeCap = StrokeCap.Round,
                    alphaFrom = 1.0f,
                    alphaTo = 1.0f,
                    indicatorSize = 352.dp,
                    indicatorValue = 100f,
                    maxIndicator = 100f
                )
            }

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







