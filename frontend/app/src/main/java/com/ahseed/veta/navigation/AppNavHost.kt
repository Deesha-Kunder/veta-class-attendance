package com.ahseed.veta.navigation

import android.content.pm.LauncherApps
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahseed.veta.screen.auth.LoginScreen
import com.ahseed.veta.screen.auth.SignUpScreen
import com.ahseed.veta.sharedpreferences.AuthPrefs

@Composable
fun AppNavHost(
    startDestination: String,
    authPrefs: AuthPrefs
) {
    val navController = rememberNavController()
    val isLoggedIn by authPrefs.isLoggedIn.collectAsState(initial = !authPrefs.getAccessToken().isNullOrEmpty())

    LaunchedEffect(isLoggedIn) {
        if(!isLoggedIn){
            navController.navigate("login"){
                popUpTo(0)
            }
        }
    }
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