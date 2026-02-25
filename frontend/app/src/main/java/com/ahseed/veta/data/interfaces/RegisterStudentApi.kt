package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.RegisterStudent
import com.ahseed.veta.data.modelclass.RegisterStudentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterStudentApi {
    @POST("api/admin/register-student")
    suspend fun registerStudent(
        @Body request: RegisterStudent
    ): Response<RegisterStudentResponse>
}