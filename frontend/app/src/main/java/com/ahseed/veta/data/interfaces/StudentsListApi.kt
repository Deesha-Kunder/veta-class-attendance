package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.StudentListResponse
import retrofit2.Response
import retrofit2.http.GET

interface StudentsListApi {
    @GET("api/admin/registered-students")
    suspend fun getRegisteredStudents(): Response<List<StudentListResponse>>
}