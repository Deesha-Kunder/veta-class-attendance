package com.ahseed.veta.user.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahseed.veta.user.BottomNavItem
import com.ahseed.veta.user.screen.profile.ProfileScreen

@Composable
fun UserMainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Material,
        BottomNavItem.Attendance,
        BottomNavItem.Report,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Material.route,
            modifier = Modifier.padding(innerPadding)
        ) {
          composable(route = BottomNavItem.Material.route) { MaterialScreen(/*navController*/) }
            composable(route = BottomNavItem.Attendance.route) { AttendanceScreen() }
            composable(route = BottomNavItem.Report.route) { ReportScreen() }
            composable(route = BottomNavItem.Profile.route) { ProfileScreen() }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainPreview(){
    UserMainScreen()
}