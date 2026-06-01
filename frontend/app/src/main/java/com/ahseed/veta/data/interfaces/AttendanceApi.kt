package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.AttendanceResponse
import com.ahseed.veta.data.modelclass.AttendanceSession
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AttendanceApi {
    @GET("api/attendance/my-session")
    suspend fun getMySessions(): Response<AttendanceResponse>

    @GET("api/attendance/session/{student_id}")
    suspend fun getSessions(
        @Path("student_id") studentId: String
    ): Response<AttendanceResponse>
}