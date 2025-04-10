package io.skylabs.omron.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.skylabs.omron.ui.home.HomeScreen
import io.skylabs.omron.ui.mypage.MyPageScreen
import io.skylabs.omron.ui.timeline.TimelineScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Timeline.route) { TimelineScreen() }
        composable(Screen.MyPage.route) { MyPageScreen() }
    }
}


