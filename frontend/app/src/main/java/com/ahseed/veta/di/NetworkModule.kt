package com.ahseed.veta.di

import android.util.Log
import com.ahseed.veta.data.interfaces.AttendanceApi
import com.ahseed.veta.data.interfaces.AuthApi
import com.ahseed.veta.data.interfaces.FaceApi
import com.ahseed.veta.data.interfaces.OnboardingApi
import com.ahseed.veta.data.interfaces.RefreshApi
import com.ahseed.veta.data.interfaces.RegisterStudentApi
import com.ahseed.veta.data.interfaces.StudentStatusApi
import com.ahseed.veta.data.interfaces.UploadApi
import com.ahseed.veta.data.repository.TokenAuthenticator
import com.ahseed.veta.sharedpreferences.AuthPrefs
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkhttpClient(
        refreshApi: RefreshApi,
        authPrefs: AuthPrefs
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                authPrefs.getAccessToken().let { token->
                    Log.d("AuthDebug","sending Token Bearer $token")
                    requestBuilder.addHeader("Authorization","Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .authenticator(TokenAuthenticator(authPrefs, refreshApi))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
//            .baseUrl("http://192.168.124.144:8080/")
//            .baseUrl("172.10.1.124")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUpload(retrofit: Retrofit): UploadApi {
        return retrofit.create(UploadApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboarding(retrofit: Retrofit): OnboardingApi{
        return retrofit.create(OnboardingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFace(retrofit: Retrofit): FaceApi{
        return retrofit.create(FaceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterStudent(retrofit: Retrofit): RegisterStudentApi{
        return retrofit.create(RegisterStudentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStudentStatus(retrofit: Retrofit): StudentStatusApi{
        return retrofit.create(StudentStatusApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetSessions(retrofit: Retrofit): AttendanceApi{
        return retrofit.create(AttendanceApi::class.java)
    }

}