package com.ahseed.veta.data.repository

import android.util.Log
import com.ahseed.veta.sharedpreferences.AuthPrefs
import com.ahseed.veta.data.interfaces.AuthApi
import com.ahseed.veta.data.interfaces.RefreshApi
import com.ahseed.veta.data.modelclass.ErrorResponse
import com.ahseed.veta.data.modelclass.LoginRequest
import com.ahseed.veta.data.modelclass.LoginResponse
import com.ahseed.veta.data.modelclass.RefreshTokenRequest
import com.ahseed.veta.data.modelclass.RefreshTokenResponse
import com.ahseed.veta.data.modelclass.SignUpRequest
import com.ahseed.veta.data.modelclass.SignUpResponse
import com.google.gson.GsonBuilder
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authPrefs: AuthPrefs,
    private val authApi: AuthApi,
    private val refreshApi: RefreshApi
) {
    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        val response = authApi.login(request)

        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(response.body())
        return if (response.isSuccessful && response.body() != null) {

            Log.d("LOGIN RESPONSE:", json)
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
        } else {

            val errorJson = response.errorBody()?.string()
            val errorResponse = try{
                GsonBuilder()
                    .create()
                    .fromJson(errorJson, ErrorResponse::class.java)
            }catch (e: Exception){
                null
            }
            Result.failure(Exception(errorResponse?.message?:"Login Failed"))
        }
    }

    suspend fun signup(request: SignUpRequest): Result<SignUpResponse> {
        val response = authApi.signup(request)
        Log.d("login", response.message())
        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            val errorJson = response.errorBody()?.string()
            val errorResponse = try{
                GsonBuilder()
                    .create()
                    .fromJson(errorJson, ErrorResponse::class.java)
            }catch (e: Exception){
                null
            }
            Result.failure(Exception(errorResponse?.message?:"Signup Failed"))
        }
    }

//    suspend fun refreshToken(request: RefreshTokenRequest): Result<RefreshTokenResponse> {
//        val response = refreshApi.refresh(request)
//        return if (response.isSuccessful && response.body() != null) {
//            val body = response.body()!!
//            authPrefs.saveAuthData(
//                accessToken = body.accessToken,
//                refreshToken = body.refreshToken,
//                userId = authPrefs.getUserId() ?: "",
//                username = authPrefs.getUsername() ?: "",
//                email = authPrefs.getEmail() ?: "",
//                role = authPrefs.getRole() ?: "",
//                isNewUser = false
//            )
//            Result.success(body)
//        } else {
//            Result.failure(Exception(response.message()))
//        }
//    }
}