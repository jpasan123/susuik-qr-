package com.example.pos.di

import android.content.Context
import androidx.room.Room
import com.example.pos.data.local.database.PosDatabase
import com.example.pos.data.local.dao.PosDao
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
    fun provideDatabase(@ApplicationContext context: Context): PosDatabase {
        return Room.databaseBuilder(
            context,
            PosDatabase::class.java,
            "pos_database"
        ).build()
    }
    
    @Provides
    fun providePosDao(database: PosDatabase): PosDao {
        return database.posDao()
    }
}