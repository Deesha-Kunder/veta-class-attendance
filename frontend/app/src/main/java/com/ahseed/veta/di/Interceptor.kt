package com.ahseed.veta.di

import android.util.Log
import com.ahseed.veta.sharedpreferences.AuthPrefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authPrefs: AuthPrefs
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = authPrefs.getAccessToken()
        Log.d("AccessTokennnn:",accessToken.toString())
        val request = if (accessToken != null) {
            Log.d("AccessTokennnn if not null:",accessToken.toString())
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}