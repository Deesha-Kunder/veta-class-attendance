package com.ahseed.veta.screen.admin.registerStudent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.StudentListResponse
import com.ahseed.veta.data.repository.RegisterStudentRepository
import com.ahseed.veta.data.repository.StudentListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterStudentViewModel @Inject constructor(
    private val repository: RegisterStudentRepository,
    private val studentListRepo: StudentListRepository
) : ViewModel() {

    private val _uiMessage = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiMessage = _uiMessage.asSharedFlow()

    private val _studentList = MutableStateFlow<List<StudentListResponse>>(emptyList())
    val studentList: StateFlow<List<StudentListResponse>> = _studentList
    
    private val _loading = MutableStateFlow<Boolean>(   false)
    val loading: StateFlow<Boolean> = _loading

    fun registerStudent(request: RegisterStudent) {
        viewModelScope.launch {
            try {
                val response = repository.registerStudent(request)
                response.onSuccess {
                    _uiMessage.emit("Registered Successfully")
                }
                    .onFailure {
                        _uiMessage.emit(it.message?:"Failed! try again")
                    }
            } catch (e: Exception) {
                _uiMessage.emit("Something went wrong")
                Log.e("RegisterStudentViewModel", "Failed with code ${e.message}")
            }
        }

    }
    fun getRegisteredStudents(){
        viewModelScope.launch {
            _loading.value = true
            try{
                val res = studentListRepo.getRegisteredStudents()
                res.onSuccess {
                    _studentList.value = it
                }
                res.onFailure {
                    Log.e("RecordViewModel","failed to fetch student list")
                }
            }finally {
                _loading.value = false
            }
        }
    }
}