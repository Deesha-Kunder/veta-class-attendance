package com.ahseed.veta.data.modelclass

data class AttendanceSession(
    val id: Long,
    val studentId: String,
    val courseId: Int,
    val checkInTime: String?,
    val checkOutTime: String?,
    val date: String?,
    val durationMinutes: Long?
)

data class ErrorResponse(
    val message: String
)

data class AttendanceResponse(
    val attendanceSessions :List<AttendanceSession>,
    val totalCompletedMinute: Long,
    val totalCompletedHours: Double,
    val remainingMinute: Long,
    val remainingHours: Double
)