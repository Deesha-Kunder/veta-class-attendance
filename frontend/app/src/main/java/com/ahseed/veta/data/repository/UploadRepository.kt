package com.ahseed.veta.data.repository

import android.util.Log
import com.ahseed.veta.data.interfaces.UploadApi
import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.data.modelclass.SignedUrlResponse
import com.ahseed.veta.data.modelclass.UploadedResponse
import com.ahseed.veta.screen.student.screen.RegisterScreen
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val api: UploadApi
) {
    val gson = GsonBuilder().setPrettyPrinting().create()

    suspend fun uploadPdf(file: MultipartBody.Part, uploadedBy: RequestBody): Result<UploadedResponse> {
        return try {
            Log.d("Upload:", file.headers?.get("Content-Disposition")?:"error")
            Log.d("Uploadedby:",uploadedBy.toString())
            val response = api.uploadPdf(file, uploadedBy)
            val json = gson.toJson(response)
            Log.d("upload response :", json)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun getFilesByAdminId(adminId: String): Result<List<MaterialItem>> {
        return try {
            Log.d("Admin id :",adminId)
            val response = api.getFilesUploadedByAdmin(adminId)
            Log.d("Get files :",gson.toJson(response))
            Result.success(response)
        } catch (e: Exception) {
            Log.d("GetFiles :","Failed")
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