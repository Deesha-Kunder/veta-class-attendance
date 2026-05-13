package com.ahseed.veta.screen.admin.recordscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.StudentListResponse
import com.ahseed.veta.data.repository.StudentListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val repository: StudentListRepository
) : ViewModel(){
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _studentList = MutableStateFlow<List<StudentListResponse>>(emptyList())
    val studentList: StateFlow<List<StudentListResponse>> = _studentList


    fun getAllStudents(){
        _isLoading.value = true
        try{
            viewModelScope.launch {
                val res = repository.getAllStudents()
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
}