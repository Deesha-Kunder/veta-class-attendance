package com.ahseed.veta.data.modelclass

data class OnboardingAdmin(
    val id : String,
    val username: String,
    val email: String,
    val role: String
)

data class OnboardingStudent(
    val id: Long,
    val name: String,
    val email: String,
    val courseHour: Int,
    val batch: String,
    val profession: String,
    val joinedDate: String
)