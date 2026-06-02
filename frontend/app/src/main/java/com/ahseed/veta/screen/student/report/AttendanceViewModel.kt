package com.ahseed.veta.screen.student.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.AttendanceResponse
import com.ahseed.veta.data.modelclass.AttendanceSession
import com.ahseed.veta.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepository
): ViewModel() {
    private val _sessions = MutableStateFlow<AttendanceResponse?>(null)
    val sessions: StateFlow<AttendanceResponse?> = _sessions

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun getMySessions(){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                attendanceRepo.getMySessions()
                    .onSuccess {
                        _sessions.value = it
                    }
                    .onFailure {
                        _message.value = it.message
                    }
            } catch (e: Exception){
                Log.e("AttendanceViewModel","Error while fetching teh attendance sessions")
            }finally {
                _isLoading.value = false
            }
        }
    }



}