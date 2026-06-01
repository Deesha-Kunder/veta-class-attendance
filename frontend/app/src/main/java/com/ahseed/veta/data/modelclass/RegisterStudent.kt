package com.ahseed.veta.data.modelclass

import com.google.gson.annotations.SerializedName

data class RegisterStudent(
    val name: String,
    val email: String,
    val profession: String,
    val courseHour: Int,
    val batch: String,
    val joinedDate: String
)

data class RegisterStudentResponse(
    @SerializedName("user_data") val userData: RegisterStudent,
    val success: Boolean,
    val message: String
)

data class StudentListResponse(
    val studentId: String,
    val name: String,
    val email: String,
    val remainingHours: Double,
    val totalCompletedHours: Double
)