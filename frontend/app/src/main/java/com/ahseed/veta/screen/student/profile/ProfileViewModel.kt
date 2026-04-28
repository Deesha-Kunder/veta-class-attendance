package com.ahseed.veta.screen.student.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahseed.veta.data.modelclass.OnboardingAdmin
import com.ahseed.veta.data.modelclass.OnboardingStudent
import com.ahseed.veta.data.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: OnboardingRepository,
) : ViewModel() {

    private val _adminProfile = MutableStateFlow<OnboardingAdmin?>(null)
    val adminProfile: StateFlow<OnboardingAdmin?> = _adminProfile

    private val _studentProfile = MutableStateFlow<OnboardingStudent?>(null)
    val studentProfile: StateFlow<OnboardingStudent?> = _studentProfile

    fun getAdminProfileInfo() {
        viewModelScope.launch {
            val result = repository.getOnboardingAdminData()
            result.onSuccess { response ->
                _adminProfile.value = response
            }.onFailure {
                Log.e("AdminProfileViewModel", "Failed to fetch admin profile: ${it.message}")
            }
        }
    }

    fun getStudentProfileInfo() {
        viewModelScope.launch {
            val result = repository.getOnboardingStudentData()
            result.onSuccess { response ->
                _studentProfile.value = response
            }.onFailure {
                Log.e(
                    "StudentProfileViewModel", "Failed to fetch student profile: ${it.message}"
                )
            }
        }
    }
}
