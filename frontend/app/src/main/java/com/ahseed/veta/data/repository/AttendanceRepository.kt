package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.AttendanceApi
import com.ahseed.veta.data.modelclass.AttendanceResponse
import com.ahseed.veta.data.modelclass.AttendanceSession
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceApi: AttendanceApi
) {
    suspend fun getMySessions(): Result<AttendanceResponse> {
        return try {
            val res = attendanceApi.getMySessions()
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
    suspend fun getSessions(id: String): Result<AttendanceResponse> {
        return try {
            val res = attendanceApi.getSessions(id)
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