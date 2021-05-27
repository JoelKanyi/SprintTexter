package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kanyideveloper.sprinttexter.databinding.FragmentTexterBinding
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import timber.log.Timber


class TexterFragment : Fragment() {

    private val smsSentReceiver by lazy { SmsSentBroadcastReciever() }
    private val smsDeliveredBroadcastReceiver by lazy { SmsDeliveredBroadcastReceiver() }
    private lateinit var binding: FragmentTexterBinding
    private val application by lazy { requireNotNull(this.activity).application }
    private val viewModelFactory by lazy { TexterViewModelFactory(application, smsSentReceiver, smsDeliveredBroadcastReceiver) }
    private val viewModel by lazy { ViewModelProvider(this, viewModelFactory).get(TexterViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTexterBinding.inflate(inflater, container, false)
        val view = binding.root

        val sentPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_SENT_ACTION"), 0)
        val deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_DELIVERED_ACTION"), 0)


        binding.buttonSend.setOnClickListener {

            if (binding.smsCount.editText.toString().trim().isEmpty()){
                binding.smsCount.editText?.error = "Require an SMS count"
            }
            if (binding.message.editText.toString().trim().isEmpty()){
                binding.message.editText?.error = "Require a message"
            }
            if (binding.smsToWho.editText.toString().trim().isEmpty()){
                binding.smsToWho.editText?.error = "Require destination"
            }

            viewModel.sendSms(2,
            binding.smsToWho.editText.toString().trim(),
            binding.message.editText.toString().trim(),
            sentPI,
            deliveredPI)
        }

        return view
    }
}
