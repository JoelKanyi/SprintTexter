package com.kanyideveloper.sprinttexter.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TextsHistoryDao {

    @Insert
    suspend fun insert(textsHistory: TextsHistory)

    @Query("SELECT * FROM history_table")
    suspend fun getAllHistory() : LiveData<List<TextsHistory>>

}