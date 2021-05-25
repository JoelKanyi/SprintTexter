package com.kanyideveloper.sprinttexter.ui.fragments.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao

class HistoryViewModel(historyDao: TextsHistoryDao, application: Application) :
    AndroidViewModel(application) {
    val history = historyDao.getAllHistory()
}