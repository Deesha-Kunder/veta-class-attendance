    package com.ahseed.veta.data.modelclass

    data class MaterialItem(
        val fileId: String,
        val filename: String,
        val filePath: String,
        val uploadedBy: String,
        val addedAt: String,
    )

    data class UploadedResponse(
        val fileId: String,
        val fileName: String,
        val filePath: String,
        val uploadedBy: String,
        val createdAt: String,
        val message: String
    )

    data class SignedUrlResponse(
        val url:String
    )