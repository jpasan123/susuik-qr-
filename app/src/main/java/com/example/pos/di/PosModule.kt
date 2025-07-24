package com.example.pos.di

import com.example.pos.data.local.dao.PosDao
import com.example.pos.data.remote.api.PosApi
import com.example.pos.data.repository.PosRepositoryImpl
import com.example.pos.domain.repository.PosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PosModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun providePosApi(retrofit: Retrofit): PosApi {
        return retrofit.create(PosApi::class.java)
    }
    
    @Provides
    @Singleton
    fun providePosRepository(
        localDao: PosDao,
        remoteApi: PosApi
    ): PosRepository {
        return PosRepositoryImpl(localDao, remoteApi)
    }
}