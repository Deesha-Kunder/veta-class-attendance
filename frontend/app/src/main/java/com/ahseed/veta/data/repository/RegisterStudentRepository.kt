package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.RegisterStudentApi
import com.ahseed.veta.data.interfaces.StudentsListApi
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.RegisterStudentResponse
import javax.inject.Inject

class RegisterStudentRepository @Inject constructor(
    private val api: RegisterStudentApi
) {
    suspend fun registerStudent(request: RegisterStudent): Result<RegisterStudentResponse> {
        return try {
            val response = api.registerStudent(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to register the students ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}