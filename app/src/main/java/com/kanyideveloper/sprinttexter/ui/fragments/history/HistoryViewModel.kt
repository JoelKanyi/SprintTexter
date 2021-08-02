package com.kanyideveloper.sprinttexter.ui.fragments.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.sprinttexter.data.database.TextsHistory
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao
import kotlinx.coroutines.launch

class HistoryViewModel(historyDao: TextsHistoryDao, application: Application) :
    AndroidViewModel(application) {
    val history = historyDao.getAllHistory()
}