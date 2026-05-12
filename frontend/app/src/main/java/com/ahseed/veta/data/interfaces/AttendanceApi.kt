package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.AttendanceResponse
import com.ahseed.veta.data.modelclass.AttendanceSession
import retrofit2.Response
import retrofit2.http.GET

interface AttendanceApi {
    @GET("api/attendance/my-session")
    suspend fun getSessions(): Response<AttendanceResponse>
}