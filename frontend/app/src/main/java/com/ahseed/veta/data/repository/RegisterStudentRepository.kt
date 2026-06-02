package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.RegisterStudentApi
import com.ahseed.veta.data.interfaces.StudentsListApi
import com.ahseed.veta.data.modelclass.ErrorResponse
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.RegisterStudentResponse
import com.google.gson.Gson
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
                val errorBody = response.errorBody()?.string()
                val message = try{
                    Gson().fromJson(
                        errorBody,
                        ErrorResponse::class.java
                    ).message
                }catch (e: Exception){
                    "Failed. Please try again"
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}