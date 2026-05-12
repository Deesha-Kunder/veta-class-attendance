package com.ahseed.veta.screen.admin.registerStudent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.repository.RegisterStudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterStudentViewModel @Inject constructor(
    private val repository: RegisterStudentRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiMessage = _uiMessage.asSharedFlow()

    fun registerStudent(request: RegisterStudent) {
        viewModelScope.launch {
            try {
                val response = repository.registerStudent(request)
                response.onSuccess {
                    _uiMessage.emit("Registered Successfully")
                }
                    .onFailure {
                        _uiMessage.emit("Failed to Register")
                    }
            } catch (e: Exception) {
                _uiMessage.emit("Something went wrong")
                Log.e("RegisterStudentViewModel", "Failed with code ${e.message}")
            }
        }

    }
}