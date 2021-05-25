package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kanyideveloper.sprinttexter.databinding.FragmentTexterBinding
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class TexterFragment : Fragment() {

    private val TAG = "TexterFragment"

    private lateinit var binding: FragmentTexterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTexterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonSend.setOnClickListener {
            sendSMS("+254706003891","Please work",10)
        }

        return view
    }


    private fun sendSMS(phoneNumber: String, message: String, count: Int) {
        if (count >= 501) {
            return
        }
        Log.d(TAG, "sendSMS: send sms called")

        val sentPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_SENT_ACTION"), 0)
        val deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_DELIVERED_ACTION"), 0)
        // ---when the SMS has been sent---
        activity?.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, "onReceive: sms sent")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        Toast.makeText(
                            context,
                            "Generic failure",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onReceive: generic failure")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        Toast.makeText(
                            context,
                            "No service",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onReceive:  no service")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        Toast.makeText(
                            context,
                            "Null PDU",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onReceive:  null pdu")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        Toast.makeText(
                            context,
                            "Radio off",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onReceive:  radio off")
                    }
                }
            }

        }, IntentFilter("SMS_SENT_ACTION"))


        activity?.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(
                            context, "SMS delivered",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "onReceive: sms delivered")
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(
                            context, "SMS not delivered",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "onReceive: sms not delivered")
                    }
                }
            }
        }, IntentFilter("SMS_DELIVERED_ACTION"))

        /*val delaySeconds: Int = 3
        SmsScheduler().sendSmsMessages(
            phoneNumber,
            message,
            sentPI,
            deliveredPI,
            delaySeconds,
            count
        )*/
        try {
            sendSmss()
        }catch (e: Exception){
            Log.d(TAG, "sendSMS: ${e.message}")
        }

    }

    private fun sendSmss(){
        val sentPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_SENT_ACTION"), 0)
        val deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_DELIVERED_ACTION"), 0)
        
        try {
            for(i in 0..5){
                Thread.sleep(1000)
                val sms = SmsManager.getDefault()
                sms.sendTextMessage(
                    "+254704108976",
                    null,
                    "text",
                    sentPI,
                    deliveredPI
                )
            } 
        }catch (e: Exception){
            Log.d(TAG, "sendSmss: ${e.message}")
        }
    }


    private class SmsScheduler {
        fun sendSmsMessages(
            phoneNumber: String?,
            message: String?,
            sentIntent: PendingIntent?,
            deliveredIntent: PendingIntent?,
            count: Int,
            delaySeconds: Int
        ): ScheduledExecutorService { val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
            val sms = SmsManager.getDefault()

            // Create the task that will send a SMS message
            val sender = Runnable {
                sms.sendTextMessage(
                    phoneNumber,
                    null,
                    "text",
                    sentIntent,
                    deliveredIntent
                )
            }

            // Schedule the messages to be sent at intervals of delaySeconds.for (i in 0 until count) {
                scheduler.schedule(sender, (delaySeconds * 1).toLong(), TimeUnit.SECONDS)
           // }
            return scheduler
        }
    }
}