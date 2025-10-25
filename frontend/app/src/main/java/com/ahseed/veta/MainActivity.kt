package com.ahseed.veta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ahseed.veta.navigation.AppNavHost
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
                val isLoggedIn by authPrefs.isLoggedIn.collectAsState(initial = !authPrefs.getAccessToken().isNullOrEmpty())
                val startDestination = if (isLoggedIn) {
                    when (authPrefs.getRole()) {
                        "ADMIN" -> "admin_main"
                        "STUDENT" -> "student_main"
                        else -> "login"
                    }
                }else{
                    "login"
                }
                AppNavHost(startDestination = startDestination,
                    authPrefs = authPrefs)
            }
        }
    }
}