package com.ahseed.veta.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AuthPrefs @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        userId: String,
        username: String,
        email: String,
        //profileUrl: String,
        role:String,
        isNewUser: Boolean
    ) {
        prefs.edit().apply {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            putString("userId", userId)
            putString("username", username)
            putString("email", email)
            //putString("profileImage", profileUrl)
            putString("role",role)
            putBoolean("isNewUser", isNewUser)
            apply()
        }
    }

    fun getAccessToken(): String? = prefs.getString("accessToken", null)
    fun getRefreshToken(): String? = prefs.getString("refreshToken", null)
    fun getUserId(): String? = prefs.getString("userId", null)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getEmail(): String? = prefs.getString("email", null)
    //fun getProfileImage(): String? = prefs.getString("profileImage", null)
    fun getRole():String? = prefs.getString("role",null)
    fun isNewUser(): Boolean? = prefs.getBoolean("profileImage", false)

    fun clearAuthData() {
        prefs.edit { clear() }
    }

}