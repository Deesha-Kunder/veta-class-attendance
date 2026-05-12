package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.StudentStatus
import retrofit2.Response
import retrofit2.http.GET

interface StudentStatusApi {
    @GET("api/status")
    suspend fun getStudentStatus(): Response<StudentStatus>
}