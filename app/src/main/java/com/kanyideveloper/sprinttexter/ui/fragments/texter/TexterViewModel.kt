package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Application
import android.app.PendingIntent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.telephony.SmsManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import timber.log.Timber

class TexterViewModel(application: Application, private val smsSentBroadcastReciever: SmsSentBroadcastReciever, private val smsDeliveredBroadcastReceiver: SmsDeliveredBroadcastReceiver) : AndroidViewModel(application) {

    private val mApplication: Application

    private val _forCounter = MutableLiveData<Int>()

    /*val forCounter: LiveData<Int>
        get() = _forCounter*/

    fun seconds() : LiveData<Int>{
        return _forCounter
    }

    //private val _smsCount =

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
       /* if (count > 501) {
            return
        }

        try {*/
            for (i in 1..count) {
                Thread.sleep(3000)
                val sms = SmsManager.getDefault()
                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
                _forCounter.value = _forCounter.value!!.plus(1)
            }
       /* } catch (e: Exception) {
            Timber.d("sendSmss: ${e.message}")
        }*/
    }

    private lateinit var timer : CountDownTimer
    private val _seconds = MutableLiveData<Int>()
    var finished = MutableLiveData<Boolean>()
    var timerValue = MutableLiveData<Long>()

    /*fun seconds() : LiveData<Int>{
        return _seconds
    }*/

    fun startTimer(){
        timer = object : CountDownTimer(300000,1000){
            override fun onFinish() {
                finished.value = true
            }

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished/1000;
                _seconds.value = timeLeft.toInt()
            }

        }.start()
    }

    fun stopTimer(){
        timer.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        mApplication.unregisterReceiver(smsSentBroadcastReciever)
        mApplication.unregisterReceiver(smsDeliveredBroadcastReceiver)
    }
}