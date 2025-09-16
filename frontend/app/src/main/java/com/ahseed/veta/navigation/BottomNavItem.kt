package com.ahseed.veta.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Material : BottomNavItem("materials", Icons.Filled.LibraryBooks, "Material")
    object Attendance : BottomNavItem("attendance", Icons.Filled.HowToReg, "Attendance")
    object Report : BottomNavItem("report", Icons.Filled.Description, "Report")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")

}