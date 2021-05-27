package com.kanyideveloper.sprinttexter.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import timber.log.Timber

class SmsSentBroadcastReciever() : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Timber.d("onReceive: sms sent")
            }
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                Timber.d("onReceive: generic failure")
            }
            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                Timber.d("onReceive:  no service")
            }
            SmsManager.RESULT_ERROR_NULL_PDU -> {
                Timber.d("onReceive:  null pdu")
            }
            SmsManager.RESULT_ERROR_RADIO_OFF -> {
                Timber.d("onReceive:  radio off")
            }
        }
    }
}