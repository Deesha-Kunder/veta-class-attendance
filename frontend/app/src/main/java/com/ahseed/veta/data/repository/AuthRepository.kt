package com.ahseed.veta.data.repository

import android.util.Log
import com.ahseed.veta.sharedpreferences.AuthPrefs
import com.ahseed.veta.data.interfaces.AuthApi
import com.ahseed.veta.data.modelclass.LoginRequest
import com.ahseed.veta.data.modelclass.LoginResponse
import com.ahseed.veta.data.modelclass.RefreshTokenRequest
import com.ahseed.veta.data.modelclass.RefreshTokenResponse
import com.ahseed.veta.data.modelclass.SignUpRequest
import com.ahseed.veta.data.modelclass.SignUpResponse
import com.ahseed.veta.screen.auth.LoginScreen
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val authPrefs: AuthPrefs,
    private val authApi: AuthApi
) {
    suspend fun login(request: LoginRequest): Result<LoginResponse>{
        val response = authApi.login(request)
        Log.d("login",response.message())
        return if(response.isSuccessful && response.body() != null){
            val body = response.body()!!
            authPrefs.saveAuthData(
                accessToken = body.accessToken,
                refreshToken = body.refreshToken,
                userId = body.userInfo.id,
                username = body.userInfo.username,
                email = body.userInfo.email,
                role = body.userInfo.role,
                isNewUser = false

            )
            Result.success(body)
        }else{
            Result.failure(Exception("Login Failed"))
        }
    }
    suspend fun signup(request: SignUpRequest): Result<SignUpResponse>{
        val response = authApi.signup(request)
        Log.d("login",response.message())
        return if(response.isSuccessful && response.body() != null){
            Result.success(response.body()!!)
        }else{
            Result.failure(Exception(response.message()))
        }
    }
    suspend fun refreshToken(request: RefreshTokenRequest): Result<RefreshTokenResponse>{
        val response = authApi.refresh(request)
        return if (response.isSuccessful && response.body() != null){
            val body = response.body()!!
            authPrefs.saveAuthData(
                accessToken = body.accessToken,
                refreshToken = body.refreshToken,
                userId = authPrefs.getUserId()?:"",
                username = authPrefs.getUsername()?:"",
                email = authPrefs.getEmail()?:"",
                role = authPrefs.getRole()?:"",
                isNewUser = false
            )
            Result.success(body)
        }else{
            Result.failure(Exception(response.message()))
        }
    }
}