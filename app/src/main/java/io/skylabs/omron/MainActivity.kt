package io.skylabs.omron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.skylabs.omron.ui.navigation.BottomNavigationBar
import io.skylabs.omron.ui.navigation.NavigationHost
import io.skylabs.omron.ui.navigation.Screen
import io.skylabs.omron.ui.theme.OmronTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ 시스템 UI를 앱 영역과 통합 (Edge-to-Edge)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // ✅ 시스템 바 위에 앱 콘텐츠가 표시되도록 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            OmronTheme {
                val backgroundColor = MaterialTheme.colorScheme.background

                // ✅ 상태바/내비게이션바 아이콘 색상 & 배경색 설정
                SideEffect {
                    WindowInsetsControllerCompat(window, window.decorView).apply {
                        isAppearanceLightStatusBars = false
                        isAppearanceLightNavigationBars = false
                    }

                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
                        window.navigationBarColor = backgroundColor.toArgb()
                        window.statusBarColor = backgroundColor.toArgb()
                    } else {
                        window.setNavigationBarContrastEnforced(false)
                    }
                }

                Surface(color = backgroundColor) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Timeline,
        Screen.MyPage
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars), // ✅ SafeArea 고려
        bottomBar = {
            BottomNavigationBar(navController = navController, items = items)
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}