package com.ahseed.veta.screen.student.modelClass

data class AttendanceItem(
    val date: String,
    val checkIn: String,
    val checkOut: String,
    val remainingHours: String,
    val totalHours: String
)
