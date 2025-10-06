package com.ahseed.veta.navigation

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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahseed.veta.screen.admin.AdminProfileScreen
import com.ahseed.veta.screen.admin.RecordsScreen
import com.ahseed.veta.screen.admin.RegisterStudentScreen
import com.ahseed.veta.screen.admin.announcementscreen.AnnouncementScreen
import com.ahseed.veta.screen.admin.announcementscreen.UploadedFileScreen
import com.ahseed.veta.ui.theme.Purple40
import com.ahseed.veta.ui.theme.Purple80

@Composable
fun AdminMainScreen(
    parentNavController: NavHostController
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavAdminItem.Register,
        BottomNavAdminItem.Announcement,
        BottomNavAdminItem.Records,
        BottomNavAdminItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in items.map { it.route }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
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
                            icon = { Icon(imageVector = item.icon, contentDescription = "icon") },
                            label = { Text(text = item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Purple80,
                                unselectedIconColor = Purple40,
                                selectedTextColor = Purple80,
                                unselectedTextColor = Purple40
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavAdminItem.Records.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BottomNavAdminItem.Register.route) { RegisterStudentScreen()}
            composable(route = BottomNavAdminItem.Announcement.route) {
                AnnouncementScreen(
                    navController = navController
                )
            }
            composable(route = BottomNavAdminItem.Records.route) { RecordsScreen() }
            composable(route = BottomNavAdminItem.Profile.route) {
                AdminProfileScreen(navController =  parentNavController)
            }
            composable("uploaded_file_screen") { UploadedFileScreen(navController = navController) }


        }

    }
}