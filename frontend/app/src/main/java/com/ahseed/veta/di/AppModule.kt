package com.ahseed.veta.di

import android.content.Context
import com.ahseed.veta.data.repository.Repository
import com.ahseed.veta.sharedpreferences.AuthPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun repository(): Repository {
        return Repository()
    }

    @Provides
    @Singleton
    fun provideAuthPrefs(@ApplicationContext context: Context): AuthPrefs{
        return AuthPrefs(context)
    }
}