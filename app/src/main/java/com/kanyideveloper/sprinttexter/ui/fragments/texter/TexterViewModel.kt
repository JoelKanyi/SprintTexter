package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Application
import android.app.PendingIntent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import timber.log.Timber

class TexterViewModel(application: Application, private val smsSentBroadcastReciever: SmsSentBroadcastReciever, private val smsDeliveredBroadcastReceiver: SmsDeliveredBroadcastReceiver) : AndroidViewModel(application) {

    private val mApplication: Application

    private val _forCounter = MutableLiveData(0)

    fun forCounter(): LiveData<Int>{
        return _forCounter
    }

    val smsCount : LiveData<Int>
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

    fun doneCounting(){
        _forCounter.value = 0
        smsSentBroadcastReciever.smssCount.value = 0
    }

    fun sendSms(count: Int, phoneNumber: String, message: String, sentPI: PendingIntent, deliveredPI: PendingIntent) {
        if (count > 501) {
            return
        }

        try {
            for (i in 1..count){
                Thread.sleep(5000)
                /* val sms = SmsManager.getDefault()
                 sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)*/
                _forCounter.value = _forCounter.value!!.plus(1)
                Timber.d(_forCounter.value.toString())
                Toast.makeText(mApplication, _forCounter.value.toString(), Toast.LENGTH_SHORT).show()

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