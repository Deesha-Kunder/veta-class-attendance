package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.UploadApi
import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.data.modelclass.SignedUrlResponse
import com.ahseed.veta.data.modelclass.UploadedResponse
import com.ahseed.veta.screen.student.screen.RegisterScreen
import okhttp3.MultipartBody
import java.lang.Exception
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val api: UploadApi
) {
    suspend fun uploadPdf(file: MultipartBody.Part, uploadedBy: String): Result<UploadedResponse> {
        return try {
            val response = api.uploadPdf(file, uploadedBy)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getFilesByAdminId(adminId: String): Result<List<MaterialItem>> {
        return try {
            val response = api.getFilesUploadedByAdmin(adminId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getFileByFileIdFromAdminPage(fileId: String):Result<SignedUrlResponse>{
        return try{
            val response = api.getFileByFileIdFromAdminPage(fileId)
            Result.success(response)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}