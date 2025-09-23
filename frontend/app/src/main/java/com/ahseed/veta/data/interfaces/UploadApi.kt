package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.MaterialItem
import com.ahseed.veta.data.modelclass.SignedUrlResponse
import com.ahseed.veta.data.modelclass.UploadedResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UploadApi {
    @Multipart
    @POST("api/admin/materials/upload")
    suspend fun uploadPdf(
        @Part file: MultipartBody.Part,
        @Part("uploaded_by") uploadedBy: String
    ): UploadedResponse

    @GET("api/admin/{admin_id}/materials")
    suspend fun getFilesUploadedByAdmin(
        @Path("admin_id") adminId:String
    ):List<MaterialItem>

    @GET("api/admin/materials/{file_id}")
    suspend fun getFileByFileIdFromAdminPage(
        @Path("file_id") fileId:String
    ): SignedUrlResponse

}