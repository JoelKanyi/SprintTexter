package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Application
import android.app.PendingIntent
import android.content.IntentFilter
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class TexterViewModel(
    application: Application,
    private val smsSentBroadcastReciever: SmsSentBroadcastReciever,
    private val smsDeliveredBroadcastReceiver: SmsDeliveredBroadcastReceiver
) : AndroidViewModel(application) {

    private val mApplication: Application

    val smsCount: LiveData<Int>
        get() = smsSentBroadcastReciever.smssCount

    init {
        IntentFilter("SMS_SENT_ACTION").also {
            application.registerReceiver(smsSentBroadcastReciever, it)
        }

        IntentFilter("SMS_DELIVERED_ACTION").also {
            application.registerReceiver(smsDeliveredBroadcastReceiver, it)
        }

        mApplication = application
    }

    fun doneCounting() {
        smsSentBroadcastReciever.smssCount.value = 0
    }

    suspend fun sendSms(
        count: Int,
        phoneNumber: String,
        message: String,
        sentPI: PendingIntent,
        deliveredPI: PendingIntent
    ) {
        if (count > 501) {
            return
        }
        try {
            viewModelScope.launch {
                for (i in 1..count) {
                    delay(3000)
                    val sms = SmsManager.getDefault()
                    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
                }

            }
        } catch (e: Exception) {
            Timber.d("sendSmss: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        mApplication.unregisterReceiver(smsSentBroadcastReciever)
        mApplication.unregisterReceiver(smsDeliveredBroadcastReceiver)
    }
}