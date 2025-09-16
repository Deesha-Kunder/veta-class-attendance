package com.ahseed.veta.data.modelclass

data class LoginRequest(
    val email: String,
    val password: String,
    val role:String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userInfo: UserInfo
)

data class UserInfo(
    val id: String,
    val username: String,
    val email: String,
    val role: String

)

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String
)

data class SignUpResponse(
    val message: String,
    val userInfo: UserInfo
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)