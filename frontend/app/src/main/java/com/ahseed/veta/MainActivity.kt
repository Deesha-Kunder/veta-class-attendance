package com.ahseed.veta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ahseed.veta.navigation.AppNavHost
import com.ahseed.veta.navigation.UserMainScreen
import com.ahseed.veta.sharedpreferences.AuthPrefs
import com.ahseed.veta.ui.theme.VetaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authPrefs: AuthPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VetaTheme {
                val startDestination = if (authPrefs.getAccessToken().isNullOrEmpty()) {
                    "login"
                } else {
                    when (authPrefs.getRole()) {
                        "ADMIN" -> "admin_main"
                        "STUDENT" -> "student_main"
                        else -> "login"
                    }
                }
                AppNavHost(startDestination = startDestination)
            }
        }
    }
}