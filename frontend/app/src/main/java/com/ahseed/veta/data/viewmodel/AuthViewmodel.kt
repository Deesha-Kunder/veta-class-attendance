package com.ahseed.veta.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ahseed.veta.data.repository.AuthRepository
import com.ahseed.veta.screen.auth.LoginUiState
import com.ahseed.veta.screen.auth.SignUpUiState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.LoginRequest
import com.ahseed.veta.data.modelclass.SignUpRequest
import com.ahseed.veta.screen.auth.UiEvent
import com.ahseed.veta.utils.ValidationUtil
import com.ahseed.veta.utils.getMessage
import com.ahseed.veta.utils.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    private val _signupState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val signupState: MutableStateFlow<SignUpUiState> = _signupState

    private val _role = MutableStateFlow("ADMIN")
    val role: StateFlow<String> = _role

    private val _usernameValidate = MutableStateFlow<String?>(null)
    val usernameValidate : MutableStateFlow<String?> = _usernameValidate

    private val _passwordValidate = MutableStateFlow<String?>(null)
    val passwordValidate : MutableStateFlow<String?> = _passwordValidate

    private val _emailValidate = MutableStateFlow<String?>(null)
    val emailValidate : MutableStateFlow<String?> = _emailValidate

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun validateUsername(username:String){
        val result = ValidationUtil.validateUsername(username)
        _usernameValidate.value = result.getMessage()
    }
    fun validateEmail(email:String){
        val result = ValidationUtil.validateEmail(email)
        _emailValidate.value = result.getMessage()
    }
    fun validatePassword(password:String){
        val result = ValidationUtil.validatePassword(password)
        _passwordValidate.value = result.getMessage()
    }

    fun login(email: String, password: String) {
        val validateEmail = ValidationUtil.validateEmail(email);
        val validatePassword = ValidationUtil.validatePassword(password)

        if (!validateEmail.isValid()) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(validateEmail.getMessage()?:"Invalid email"))
            }
        }
        if (!validatePassword.isValid()) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(validatePassword.getMessage()?:"Invalid password"))
            }
        }
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading

            val result = repository.login(LoginRequest(email, password, role.value))
            Log.d("loginRequest", email + "" + password + "" + role.value)
            result.onSuccess {
                _loginState.value = LoginUiState.Success(it)
                if (role.value == "ADMIN") {
                    _uiEvent.emit(UiEvent.Navigate("admin_main"))
                    _uiEvent.emit(UiEvent.ShowToast("Login successful"))
                } else {
                    _uiEvent.emit(UiEvent.Navigate("student_main"))
                    _uiEvent.emit(UiEvent.ShowToast("Login successful"))
                }
            }
            result.onFailure {
                Log.e("AuthViewModel", "Login failed", it)
                _loginState.value = LoginUiState.Error(it.message ?: "Login failed")
                _uiEvent.emit(UiEvent.ShowToast(it.message ?: "login failed"))
            }
        }
    }

    fun signUp(username: String, email: String, password: String) {

        val validateEmail = ValidationUtil.validateEmail(email);
        val validatePassword = ValidationUtil.validatePassword(password)
        val validateUsername = ValidationUtil.validateUsername(username)

        if (!validateEmail.isValid()) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(validateEmail.getMessage()?:"Invalid email"))
            }
        }
        if (!validatePassword.isValid()) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(validatePassword.getMessage()?:"Invalid password"))
            }
        }
        if (!validateUsername.isValid()) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(validateUsername.getMessage()?:"Invalid username"))
            }
        }
        viewModelScope.launch {
            _signupState.value = SignUpUiState.Loading
            val result = repository.signup(SignUpRequest(username, email, password, role.value))
            Log.d("SignupRequest", email + "" + password + "" + role.value)
            result.onSuccess {
                _signupState.value = SignUpUiState.Success(it)
                _uiEvent.emit(UiEvent.Navigate("login"))
                _uiEvent.emit(UiEvent.ShowToast("Signup successful"))

            }
            result.onFailure {
                Log.e("AuthViewModel", "signup failed", it)
                _signupState.value = SignUpUiState.Error(it.message ?: "Signup failed")
                _uiEvent.emit(UiEvent.ShowToast(it.message ?: "Signup failed"))
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun setRole(newRole: String) {
        _role.value = newRole
    }

}