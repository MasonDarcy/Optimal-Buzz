package com.example.optimal_buzz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.optimal_buzz.data.entities.ChartEntry
import com.example.optimal_buzz.data.entities.DrinkData
import com.example.optimal_buzz.data.entities.TimeData
import com.example.optimal_buzz.data.entities.User

@Database(entities = [User::class, ChartEntry::class, DrinkData::class, TimeData::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class SessionDatabase: RoomDatabase() {
    abstract val sessionDatabaseDao: SessionDatabaseDao

    //allows clients to access the methods for creating/getting database without instantiating the class
    companion object {

        @Volatile
        private var INSTANCE: SessionDatabase? = null

        fun getInstance(context: Context): SessionDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SessionDatabase::class.java,
                        "previous_session_data"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}