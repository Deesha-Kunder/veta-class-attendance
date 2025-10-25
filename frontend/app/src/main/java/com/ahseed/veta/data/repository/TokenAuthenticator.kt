package com.ahseed.veta.data.repository

import android.util.Log
import com.ahseed.veta.data.interfaces.RefreshApi
import com.ahseed.veta.data.modelclass.RefreshTokenRequest
import com.ahseed.veta.sharedpreferences.AuthPrefs
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val authPrefs: AuthPrefs,
    private val refreshApi: RefreshApi
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2)
            return null

        val refreshToken = authPrefs.getRefreshToken() ?: return null
        Log.d("Refresh Token in Token Authenticator", refreshToken)
        return try {
            val newTokens = runBlocking {
                refreshApi.refresh(RefreshTokenRequest(refreshToken))
            }
            if (newTokens.accessToken.isNotBlank()) {
                authPrefs.saveAuthData(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken,
                    userId = authPrefs.getUserId() ?: "",
                    role = authPrefs.getRole() ?: "",
                    email = authPrefs.getEmail() ?: "",
                    isNewUser = authPrefs.isNewUser() ?: false,
                    username = authPrefs.getUsername() ?: ""
                )
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("AUTH debug", "refresh Token generation failed${e.message}")
            authPrefs.clearAuthData()
            null
        }
    }

    fun responseCount(response: Response): Int {
        var result = 1;
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = response.priorResponse
        }
        return result
    }
}