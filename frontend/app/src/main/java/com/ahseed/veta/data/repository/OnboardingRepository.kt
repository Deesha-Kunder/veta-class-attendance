package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.OnboardingApi
import com.ahseed.veta.data.modelclass.OnboardingAdmin
import com.ahseed.veta.data.modelclass.OnboardingStudent
import jakarta.inject.Inject

class OnboardingRepository @Inject constructor(
    private val api: OnboardingApi
) {
    suspend fun getOnboardingAdminData(): Result<OnboardingAdmin>{
        return try{
            val response = api.getOnboardingData()
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Failed to load admin data ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun getOnboardingStudentData(): Result<OnboardingStudent>{
        return try {
            val response = api.getOnboardingStudentData()
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Failed to load student data ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}