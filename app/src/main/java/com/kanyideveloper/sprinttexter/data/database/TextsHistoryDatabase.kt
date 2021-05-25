package com.kanyideveloper.sprinttexter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TextsHistory::class], version = 1)
abstract class TextsHistoryDatabase : RoomDatabase() {

    abstract val textsHistoryDao: TextsHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: TextsHistoryDatabase? = null

        fun getInstance(context: Context): TextsHistoryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TextsHistoryDatabase::class.java,
                        "texts_history_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}