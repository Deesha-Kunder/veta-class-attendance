package com.ahseed.veta.data.repository

import com.ahseed.veta.data.interfaces.FaceApi
import com.ahseed.veta.data.modelclass.ErrorResponse
import com.ahseed.veta.data.modelclass.FaceRecognizeRequest
import com.ahseed.veta.data.modelclass.FaceRecognizeResponse
import com.ahseed.veta.data.modelclass.FaceRegister
import com.ahseed.veta.data.modelclass.FaceRegisterResponse
import com.google.gson.Gson
import jakarta.inject.Inject

class FaceRepository @Inject constructor(
    private val api: FaceApi
) {
    suspend fun registerFace(request: FaceRegister): Result<FaceRegisterResponse>{
        return try{
            val response = api.registerFace(request);
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Registration failed ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun recognizeFace(request: FaceRecognizeRequest): Result<FaceRecognizeResponse>{
        return try{
            val response = api.recognizeFace(request);
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                val errorBody = response.errorBody()?.string()
                val msg = try{
                    Gson().fromJson(
                        errorBody,
                        ErrorResponse::class.java
                    ).message
                }catch (e: Exception){
                    "Failed? try again"
                }
                Result.failure(Exception(msg))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
