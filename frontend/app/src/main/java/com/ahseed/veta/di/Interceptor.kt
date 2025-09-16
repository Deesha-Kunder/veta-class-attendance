package com.ahseed.veta.di

import com.ahseed.veta.sharedpreferences.AuthPrefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class Interceptor @Inject constructor(
    private val authPrefs: AuthPrefs
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = authPrefs.getAccessToken()
        val request = if (accessToken != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}