package com.ahseed.veta.data.interfaces

import com.ahseed.veta.data.modelclass.OnboardingAdmin
import com.ahseed.veta.data.modelclass.OnboardingStudent
import retrofit2.Response
import retrofit2.http.GET

interface OnboardingApi {
    @GET("api/onboarding/admin")
    suspend fun getOnboardingData(): Response<OnboardingAdmin>

    @GET("api/onboarding/student")
    suspend fun getOnboardingStudentData(): Response<OnboardingStudent>
}