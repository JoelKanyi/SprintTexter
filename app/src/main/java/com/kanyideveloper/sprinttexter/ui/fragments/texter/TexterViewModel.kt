package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Application
import android.app.PendingIntent
import android.content.IntentFilter
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import timber.log.Timber

class TexterViewModel(
    application: Application,
    private val smsSentBroadcastReciever: SmsSentBroadcastReciever,
    private val smsDeliveredBroadcastReceiver: SmsDeliveredBroadcastReceiver
) :
    AndroidViewModel(application) {

    private val mApplication: Application

    private val _smsCount = smsSentBroadcastReciever.smssCount

    val smsCount : LiveData<Int>
        get() = _smsCount

    init {
        IntentFilter("SMS_SENT_ACTION").also {
            application.registerReceiver(smsSentBroadcastReciever, it)
        }

        IntentFilter("SMS_DELIVERED_ACTION").also {
            application.registerReceiver(smsDeliveredBroadcastReceiver, it)
        }

        mApplication = application
    }

    fun doneCounting(){
        _smsCount.value = 0
    }

    fun sendSms(
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
            for (i in 0..count) {
                Thread.sleep(1000)
                val sms = SmsManager.getDefault()
                sms.sendTextMessage(
                    phoneNumber,
                    null,
                    message,
                    sentPI,
                    deliveredPI
                )
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