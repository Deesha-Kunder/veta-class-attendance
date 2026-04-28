package com.ahseed.veta.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahseed.veta.screen.student.Attendance.MarkAttendance
import com.ahseed.veta.screen.student.Attendance.RegisterFaceScreen
import com.ahseed.veta.screen.student.materialScreen.MaterialScreen
import com.ahseed.veta.screen.student.profile.ProfileScreen
import com.ahseed.veta.screen.student.screen.ReportScreen
import com.ahseed.veta.ui.theme.Purple80

@Composable
fun UserMainScreen(
    parentNavController: NavHostController
) {
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
                            selectedIconColor = Purple80,
                            unselectedIconColor = Color.LightGray,
                            selectedTextColor = Purple80,
                            unselectedTextColor = Color.LightGray
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
            composable(route = BottomNavItem.Material.route) { MaterialScreen(
                navController= navController
            ) }
            composable(route = BottomNavItem.Attendance.route) { MarkAttendance(navController = navController) }
            composable(route = BottomNavItem.Report.route) { ReportScreen() }
            composable(route = BottomNavItem.Profile.route) { ProfileScreen(navController= navController) }

            composable("registerFaceScreen"){
                RegisterFaceScreen(navController = navController)
            }
        }
    }
}