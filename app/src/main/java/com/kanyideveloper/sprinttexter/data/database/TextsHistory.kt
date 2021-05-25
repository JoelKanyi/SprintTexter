package com.kanyideveloper.sprinttexter.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
class TextsHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sentText: String,
    val phoneNumber: String,
    val simCard: String,
    val date: String
)
