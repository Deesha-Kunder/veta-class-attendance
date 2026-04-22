package com.ahseed.veta.data.modelclass

data class AttendanceItem(
    val date: String,
    val checkIn: String,
    val checkOut: String,
    val remainingHours: String,
    val totalHours: String
)