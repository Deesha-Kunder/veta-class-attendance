package com.ahseed.veta.screen.admin.announcementscreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.data.modelclass.SignedUrlResponse
import com.ahseed.veta.data.repository.UploadRepository
import com.ahseed.veta.sharedpreferences.AuthPrefs
import com.ahseed.veta.utils.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@HiltViewModel
class UploadViewmodel @Inject constructor(
    private val repository: UploadRepository,
    private val prefs: AuthPrefs
) : ViewModel() {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> get() = _uploadState

    private val _fileState = MutableStateFlow<FileState>(FileState.Idle)
    val fileState: StateFlow<FileState> get() = _fileState

    private val _materials = MutableStateFlow<List<MaterialItem>>(emptyList())
    val materials: MutableStateFlow<List<MaterialItem>> = _materials

    private val _selectedUrl = MutableStateFlow<String?>(null)
    val selectedUrl: StateFlow<String?> = _selectedUrl

    fun uploadFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val multiPart = FileUtil.uriToMultiPart(context, uri)
                val adminId = prefs.getUserId() ?: ""
                uploadFile(multiPart, adminId)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Error")
            }
        }
    }

    fun uploadFile(file: MultipartBody.Part, uploadedBy: String) {
        val uploadedPart = uploadedBy.toRequestBody("text/plain".toMediaTypeOrNull())
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            val response = repository.uploadPdf(file, uploadedPart)
            _uploadState.value = response.fold(
                onSuccess = { UploadState.Success(it) },
                onFailure = { UploadState.Error(it.message ?: "Error While uploading") }
            )
        }
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

    fun viewFile(fileId: String) {
        viewModelScope.launch {
            val response = repository.getFileByFileFromIdFromAdminPage(fileId)
            response.fold(
                onSuccess = { _selectedUrl.value = it.url },
                onFailure = { _selectedUrl.value = null }
            )
        }
    }
    fun cleanSelectedFleUrl(){
        _selectedUrl.value = null
    }
}