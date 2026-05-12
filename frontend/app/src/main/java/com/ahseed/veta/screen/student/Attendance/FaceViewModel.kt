package com.ahseed.veta.screen.student.Attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.FaceRecognizeRequest
import com.ahseed.veta.data.modelclass.FaceRecognizeResponse
import com.ahseed.veta.data.modelclass.FaceRegister
import com.ahseed.veta.data.modelclass.StudentStatus
import com.ahseed.veta.data.repository.FaceRepository
import com.ahseed.veta.data.repository.StatusRepository
import com.ahseed.veta.sharedpreferences.AuthPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FaceViewModel @Inject constructor(
    private val faceRepository: FaceRepository,
    private val authPrefs: AuthPrefs,
    private val statusRepo: StatusRepository
): ViewModel()
{
    private val _message = MutableStateFlow<String?>(null)
    val message : StateFlow<String?> = _message

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered : StateFlow<Boolean> = _isRegistered

    private val _status = MutableStateFlow<StudentStatus?>(null)
    val status: MutableStateFlow<StudentStatus?> = _status

    private val _faceRecognizeResponse = MutableStateFlow<FaceRecognizeResponse?>(null)
    val faceRecognizeResponse: MutableStateFlow<FaceRecognizeResponse?> = _faceRecognizeResponse

    fun getStudentStatus(){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val response = statusRepo.getStudentStatus()
                response.onSuccess {
                    _status.value = it
                    Log.d("FaceViewModel","Student status fetched")
                }
                response.onFailure {
                    Log.e("FaceViewModel error","Failed to fetch student status")
                }
            }finally {
                _isLoading.value = false
            }
        }
    }

    fun registerFace(embedding:List<Float>){
        if(isRegistered.value || isLoading.value ) return
        val studentId = authPrefs.getUserId()?:"0"
        val name = authPrefs.getUsername()
        val request= FaceRegister(
            studentId,
            name,
            embedding
        )
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val response = faceRepository.registerFace(request)
                if(response.isSuccess){
                    _message.value = response.getOrNull()?.message
                    _isRegistered.value = true
                }else{
                    _message.value = response.exceptionOrNull()?.message

                }
            }catch (e: Exception){
                _message.value = e.message
            }finally {
                _isLoading.value = false
            }
        }
    }
    fun recognizeFace(request: FaceRecognizeRequest){
        if(_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try{
                val response = faceRepository.recognizeFace(request)
                    .onSuccess {
                        _message.value = "Marked attendance"
                        getStudentStatus()
                    }
                    .onFailure {
                        _message.value = "Failed to mark attendance"
                    }
            }catch (e:Exception){
                Log.e("FaceViewModel","Error while recognizing face")
            }finally {
                _isLoading.value = false
            }
        }
    }
}
