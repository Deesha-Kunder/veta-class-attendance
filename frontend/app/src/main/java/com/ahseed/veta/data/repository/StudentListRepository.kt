package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.StudentsListApi
import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.StudentListResponse
import javax.inject.Inject

class StudentListRepository @Inject constructor(
    private val api: StudentsListApi
) {
    suspend fun getRegisteredStudents():Result<List<StudentListResponse>>{
        return try {
            val res = api.getRegisteredStudents()
            if(res.isSuccessful && res.body() != null){
                Result.success(res.body()!!)
            }else{
                Result.failure(Exception("failed to fetch the student list ${res.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}