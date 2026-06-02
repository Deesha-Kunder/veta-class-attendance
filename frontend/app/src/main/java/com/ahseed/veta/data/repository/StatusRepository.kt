package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.StudentStatusApi
import com.ahseed.veta.data.modelclass.StudentStatus
import javax.inject.Inject

class StatusRepository @Inject constructor(
    private val api: StudentStatusApi
){
    suspend fun getStudentStatus(): Result<StudentStatus>{
        return try{
            val response = api.getStudentStatus();
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Failed to load student status"))

            }
        }catch (e: Exception){
            Result.failure(e)

        }
    }

}