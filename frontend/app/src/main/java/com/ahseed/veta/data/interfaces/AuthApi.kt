package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.LoginRequest
import com.ahseed.veta.data.modelclass.LoginResponse
import com.ahseed.veta.data.modelclass.RefreshTokenRequest
import com.ahseed.veta.data.modelclass.RefreshTokenResponse
import com.ahseed.veta.data.modelclass.SignUpRequest
import com.ahseed.veta.data.modelclass.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/signup")
    suspend fun signup(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>
}