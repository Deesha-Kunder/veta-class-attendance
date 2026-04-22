package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.FaceRecognizeRequest
import com.ahseed.veta.data.modelclass.FaceRecognizeResponse
import com.ahseed.veta.data.modelclass.FaceRegister
import com.ahseed.veta.data.modelclass.FaceRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceApi {
    @POST("api/face/register")
    suspend fun registerFace(
        @Body request: FaceRegister
    ): Response<FaceRegisterResponse>

    @POST("api/face/recognize")
    suspend fun recognizeFace(
        @Body request: FaceRecognizeRequest
    ): Response<FaceRecognizeResponse>
}