package com.ahseed.veta.screen.student.materialScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.data.repository.UploadRepository
import com.ahseed.veta.screen.admin.announcementscreen.FileState
import com.ahseed.veta.sharedpreferences.AuthPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialStudentViewModel @Inject constructor(
    private val repository: UploadRepository,
    private val prefs: AuthPrefs
) : ViewModel() {
    private val _materials = MutableStateFlow<List<MaterialItem>>(emptyList())
    val materials: StateFlow<List<MaterialItem>> = _materials

    private val _fileState = MutableStateFlow<FileState>(FileState.Idle)
    val fileState: StateFlow<FileState> = _fileState

    private val _selectedUrl = MutableStateFlow<String?>(null)
    val selectedUrl: StateFlow<String?> = _selectedUrl

    private val _selectedUrlForDownload = MutableStateFlow<String?>(null)
    val selectedUrlForDownload: StateFlow<String?> = _selectedUrlForDownload

    init {
        getAllFiles()
    }

    fun getFilesByAdminId() {
        viewModelScope.launch {
            _fileState.value = FileState.Loading
            val adminId = prefs.getUserId() ?: ""
            val response = repository.getFilesByAdminId(adminId)
            response.fold(
                onSuccess = {
                    _materials.value = it
                    _fileState.value = FileState.Success(it)
                },
                onFailure = {
                    _fileState.value = FileState.Error(it.message ?: "File fetching error")
                }
            )
        }
    }

    fun getAllFiles() {
        viewModelScope.launch {
            _fileState.value = FileState.Loading
            val response = repository.getAllFiles()
            response.fold(
                onSuccess = {
                    _materials.value = it
                    _fileState.value = FileState.Success(it)
                },
                onFailure = {
                    _fileState.value = FileState.Error(it.message ?: "fetching error")
                }
            )
        }
    }

    fun viewFile(fileId: String) {
        viewModelScope.launch {
            val response = repository.getFileByFileFromID(fileId)
            response.fold(

                onSuccess = {
                    _selectedUrl.value = it.url
                    Log.d("selected url :", " ${it.url}")
                },
                onFailure = { _selectedUrl.value = null }
            )
        }
    }

    fun cleanSelectedFleUrl() {
        _selectedUrl.value = null
    }

    fun setSelectedUrl(fileId: String) {
        viewModelScope.launch {
            val response = repository.getFileByFileFromID(fileId)
            response.fold(
                onSuccess = { _selectedUrlForDownload.value = it.url },
                onFailure = { _selectedUrlForDownload.value = null }
            )
        }
    }

}