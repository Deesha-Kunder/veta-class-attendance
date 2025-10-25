package com.ahseed.veta.di

import com.ahseed.veta.data.interfaces.RefreshApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    @Named("authGson")
    fun provideAuthGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        @Named("authGson") gson: Gson,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideRefresh(
        @Named("authRetrofit") retrofit: Retrofit
    ): RefreshApi =
        retrofit.create(RefreshApi::class.java)
}