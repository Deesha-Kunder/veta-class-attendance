package com.ahseed.veta.screen.auth

import com.ahseed.veta.data.modelclass.LoginResponse
import com.ahseed.veta.data.modelclass.SignUpResponse

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val loginResponse: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

sealed class SignUpUiState {
    object Idle : SignUpUiState()
    object Loading : SignUpUiState()
    data class Success(val signupResponse: SignUpResponse) : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
}