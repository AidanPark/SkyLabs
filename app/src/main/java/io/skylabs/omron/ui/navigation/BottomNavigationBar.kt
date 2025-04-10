package io.skylabs.omron.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.skylabs.omron.ui.theme.NavigationItem
import io.skylabs.omron.ui.theme.DividerGray

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<Screen>
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(DividerGray)
        )

        NavigationBar(containerColor = backgroundColor) {
            items.forEach { screen ->
                val selected = currentRoute == screen.route
                val iconRes = if (selected) screen.selectedIconRes else screen.unselectedIconRes

                val iconColor = if (selected) Color.White else NavigationItem
                val textColor = if (selected) Color.White else NavigationItem

                NavigationBarItem(
                    icon = {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = screen.label,
                                modifier = Modifier.size(24.dp),
                                tint = iconColor
                            )
                            Spacer(modifier = Modifier.height(2.dp)) // ✅ 아이콘-텍스트 간격 좁힘
                            Text(
                                text = screen.label,
                                fontSize = 12.sp, // ✅ 폰트 사이즈 증가
                                color = textColor
                            )
                        }
                    },
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = iconColor,
                        unselectedIconColor = iconColor,
                        selectedTextColor = textColor,
                        unselectedTextColor = textColor
                    )
                )
            }
        }
    }
}

