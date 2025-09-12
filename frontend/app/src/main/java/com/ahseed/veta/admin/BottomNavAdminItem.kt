package com.ahseed.veta.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavAdminItem(val route: String, val icon: ImageVector, val label: String) {
    object Register : BottomNavAdminItem("register", Icons.Filled.Add, "register")
    object Announcement : BottomNavAdminItem("Announcement", Icons.Outlined.Campaign,"Announcement")
    object Records : BottomNavAdminItem("Records", Icons.Outlined.Assessment,"Records")
    object Profile:BottomNavAdminItem("Profile",Icons.Outlined.Person,"Profile")

}