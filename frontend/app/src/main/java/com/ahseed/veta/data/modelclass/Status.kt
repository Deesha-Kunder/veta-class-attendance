package com.ahseed.veta.data.modelclass

import java.time.LocalDateTime

data class StudentStatus(
    val faceRegistered: Boolean,
    val checkIn: Boolean,
    val checkOut: Boolean,
    val checkInTime: String?,
    val checkOutTime: String?
)