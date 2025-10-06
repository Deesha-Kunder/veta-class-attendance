package com.ahseed.veta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahseed.veta.screen.auth.LoginScreen
import com.ahseed.veta.screen.auth.SignUpScreen

@Composable
fun AppNavHost(
    startDestination: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("admin_main") { AdminMainScreen(parentNavController= navController) }
        composable("student_main") { UserMainScreen(parentNavController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("signup") { SignUpScreen(navController = navController) }

    }
}