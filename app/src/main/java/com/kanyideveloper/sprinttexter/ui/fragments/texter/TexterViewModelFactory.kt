package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao
import com.kanyideveloper.sprinttexter.ui.fragments.history.HistoryViewModel
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever

@Suppress("UNCHECKED_CAST")
class TexterViewModelFactory(
    private val application: Application,
    private val smsSentBroadcastReciever: SmsSentBroadcastReciever,
    private val smsDeliveredBroadcastReceiver: SmsDeliveredBroadcastReceiver
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TexterViewModel::class.java)) {
            return TexterViewModel(application, smsSentBroadcastReciever, smsDeliveredBroadcastReceiver) as T
        } else
            throw IllegalArgumentException("ViewModel class not found")
    }
}