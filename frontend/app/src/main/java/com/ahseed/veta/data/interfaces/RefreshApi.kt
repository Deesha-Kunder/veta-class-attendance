package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.RefreshTokenRequest
import com.ahseed.veta.data.modelclass.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse
}