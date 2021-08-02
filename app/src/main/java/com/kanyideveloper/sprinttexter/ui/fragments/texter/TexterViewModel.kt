package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Application
import android.app.PendingIntent
import android.content.IntentFilter
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.sprinttexter.data.database.TextsHistory
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TexterViewModel(
    application: Application, private val smsSentBroadcastReciever: SmsSentBroadcastReciever,
    private val smsDeliveredBroadcastReceiver: SmsDeliveredBroadcastReceiver,
    private val textsHistoryDao: TextsHistoryDao
) : AndroidViewModel(application) {

    private val mApplication: Application

    private var mTextsHistoryDao: TextsHistoryDao

    val smsCount: MutableLiveData<Int>
        get() = smsSentBroadcastReciever.smssCount

    init {
        Timber.d("Init Called")

        IntentFilter("SMS_SENT_ACTION").also {
            application.registerReceiver(smsSentBroadcastReciever, it)
        }

        IntentFilter("SMS_DELIVERED_ACTION").also {
            application.registerReceiver(smsDeliveredBroadcastReceiver, it)
        }
        mApplication = application

        mTextsHistoryDao = textsHistoryDao
    }


    suspend fun saveToDb(text: String, totalSms: Int, phoneNumber: String, simCard: String) {

        //Calculate Date
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val  date = sdf.format(Date())
        viewModelScope.launch {
            val textHistory = TextsHistory(0, text, phoneNumber, simCard, totalSms, date.toString())
            mTextsHistoryDao.insert(textHistory)
        }
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

        Timber.d("ViewModel: Sent sms method called")
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
            Timber.d("sendSms: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("ViewModel onCleared")
        mApplication.unregisterReceiver(smsSentBroadcastReciever)
        mApplication.unregisterReceiver(smsDeliveredBroadcastReceiver)
    }



    private val _save = MutableLiveData<Boolean>()
    val save: LiveData<Boolean>
        get() = _save


    fun youCanNowSave(){
        _save.value = true
    }

    fun doneSaving(){
        _save.value = false
    }
}

