package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.AttendanceApi
import com.ahseed.veta.data.modelclass.AttendanceResponse
import com.ahseed.veta.data.modelclass.AttendanceSession
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceApi: AttendanceApi
) {
    suspend fun getSessions(): Result<AttendanceResponse> {
        return try {
            val res = attendanceApi.getSessions()
            if (res.isSuccessful) {
                Result.success(res.body()!!)
            } else {

                Result.failure(
                    Exception("No attendance sessions found")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}