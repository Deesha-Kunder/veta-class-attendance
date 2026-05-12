package com.ahseed.veta.data.modelclass

data class FaceRegister(
    val studentId:String,
    val name:String?,
    val embedding: List<Float>
)
data class FaceRegisterResponse(
    val success: Boolean,
    val message: String
)
data class FaceRecognizeRequest(
    val embedding:List<Float>
)
data class FaceRecognizeResponse(
    val studentId:String,
    val studentName:String,
    val confidence: Float,
    val checkInTime: String?,
    val checkOutTime:String?
)
