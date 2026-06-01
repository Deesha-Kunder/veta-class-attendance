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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahseed.veta.screen.admin.AdminProfileScreen
import com.ahseed.veta.screen.admin.recordscreen.RecordsScreen
import com.ahseed.veta.screen.admin.announcementscreen.AnnouncementScreen
import com.ahseed.veta.screen.admin.announcementscreen.UploadedFileScreen
import com.ahseed.veta.screen.admin.recordscreen.AdminReportScreen
import com.ahseed.veta.screen.admin.registerStudent.RegisterStudentScreen
import com.ahseed.veta.screen.admin.registerStudent.RegisteredStudentsScreen
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
            composable(route = BottomNavAdminItem.Register.route) { RegisterStudentScreen(navController = navController)}
            composable(route = BottomNavAdminItem.Announcement.route) {
                AnnouncementScreen(
                    navController = navController
                )
            }
            composable(route = BottomNavAdminItem.Records.route) { RecordsScreen(navController = navController) }
            composable(route = BottomNavAdminItem.Profile.route) {
                AdminProfileScreen(navController =  parentNavController)
            }
            composable("uploaded_file_screen") { UploadedFileScreen(navController = navController) }
            composable ("registered_students_screen") { RegisteredStudentsScreen (navController = navController) }
            composable("admin_report_screen/{student_id}"){backStackEntry->
                val studentId = backStackEntry.arguments?.getString("student_id")
                AdminReportScreen(studentId = studentId)
            }


        }

    }
}