package com.kanyideveloper.sprinttexter.ui.fragments.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao

@Suppress("UNCHECKED_CAST")
class HistoryViewModelFactory(
    private val historyDao: TextsHistoryDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(historyDao, application) as T
        } else
            throw IllegalArgumentException("ViewModel class not found")
    }
}