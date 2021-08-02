package com.kanyideveloper.sprinttexter.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TextsHistoryDao {

    @Insert
    suspend fun insert(textsHistory: TextsHistory)

    @Query("SELECT * FROM history_table ORDER BY date DESC")
    fun getAllHistory(): LiveData<List<TextsHistory>>

    @Query("DELETE * FROM history_table")
    fun deleteAllHistory()

}