package io.skylabs.omron.ui.navigation

import io.skylabs.omron.R

sealed class Screen(
    val route: String,
    val label: String,
    val selectedIconRes: Int,
    val unselectedIconRes: Int
) {
    data object Home : Screen(
        "home", "Home",
        R.drawable.ic_home,
        R.drawable.ic_home_outline
    )

    data object Timeline : Screen(
        "timeline", "Timeline",
        R.drawable.ic_timeline,
        R.drawable.ic_timeline_outline
    )

    data object MyPage : Screen(
        "mypage", "MyPage",
        R.drawable.ic_mypage,
        R.drawable.ic_mypage_outline
    )
}