package com.ahseed.veta.screen.admin.recordscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.AttendanceResponse
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.StudentListResponse
import com.ahseed.veta.data.repository.AttendanceRepository
import com.ahseed.veta.data.repository.StudentListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: StudentListRepository,
    private val attendanceRepo: AttendanceRepository
) : ViewModel(){
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _studentList = MutableStateFlow<List<StudentListResponse>>(emptyList())
    val studentList: StateFlow<List<StudentListResponse>> = _studentList

    private val _sessions = MutableStateFlow<AttendanceResponse?>(null)
    val sessions: StateFlow<AttendanceResponse?> = _sessions

    private val _message = MutableStateFlow<String?>(null)
    val message : StateFlow<String?> = _message


    fun getRegisteredStudents(){
        _isLoading.value = true
        try{
            viewModelScope.launch {
                val res = repository.getRegisteredStudents()
                res.onSuccess {
                    _studentList.value = it
                }
                res.onFailure {
                    Log.e("RecordViewModel","failed to fetch student list")
                }
            }
        }finally {
            _isLoading.value = false
        }

    }
    fun getSessions(id: String?){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                if (id == null) return@launch
                attendanceRepo.getSessions(id)
                    .onSuccess {
                        _sessions.value = it
                    }
                    .onFailure {
                        _message.value = it.message
                    }
            } catch (e: Exception){
                Log.e("AttendanceViewModel","Error while fetching teh attendance session")
            }finally {
                _isLoading.value = false
            }
        }
    }
}