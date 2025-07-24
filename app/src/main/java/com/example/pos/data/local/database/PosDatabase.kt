package com.example.pos.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.pos.data.local.dao.PosDao
import com.example.pos.data.local.database.entities.SessionEntity

@Database(
    entities = [SessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PosDatabase : RoomDatabase() {
    abstract fun posDao(): PosDao
    
    companion object {
        @Volatile
        private var INSTANCE: PosDatabase? = null
        
        fun getDatabase(context: Context): PosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PosDatabase::class.java,
                    "pos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}