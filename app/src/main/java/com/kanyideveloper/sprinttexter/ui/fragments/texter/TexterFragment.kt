package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

    private val viewModelFactory by lazy {
        TexterViewModelFactory(
            application,
            smsSentReceiver,
            smsDeliveredBroadcastReceiver
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(TexterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTexterBinding.inflate(inflater, container, false)
        val view = binding.root

        val sentPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_SENT_ACTION"), 0)
        val deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_DELIVERED_ACTION"), 0)

        binding.buttonSend.setOnClickListener {

            if (binding.smsCount.editText!!.text.toString().trim().isEmpty()) {
                binding.smsCount.editText?.error = "Require an SMS count"
            }
            if (binding.message.editText!!.text.toString().trim().isEmpty()) {
                binding.message.editText?.error = "Require a message"
            }
            if (binding.smsToWho.editText!!.text.toString().trim().isEmpty()) {
                binding.smsToWho.editText?.error = "Require destination"
            }else{

                viewModel.doneCounting()
                binding.count.text = "0"

                if (checkIfPhoneNumberOrCompanyNumber(binding.smsToWho.editText!!.text.toString()) == null){
                    Toast.makeText(requireContext(), "Please input a correct number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else{
                    viewModel.sendSms(
                        binding.smsCount.editText!!.text.toString().toInt(),
                        binding.smsToWho.editText!!.text.toString().trim(),
                        binding.message.editText!!.text.toString().trim(),
                        sentPI, deliveredPI)
                }
            }
        }

        viewModel.smsCount.observe(viewLifecycleOwner, Observer {counterValue ->
            binding.count.text = counterValue.toString()
        })

        return view
    }

    private fun checkIfPhoneNumberOrCompanyNumber(number: String): String?{
        // if the length is equal to 10 -> number
            /*
            slice the zero and concatenate with the country code
            */
        //if length is equal 6 -> radio station number
            /*
            Proceeding the sending the message
            */
        //Else wrong input

        //Handle instance when the input already has a +254

        var trimmedNumber: String? = null

        if(number.length == 10){
             trimmedNumber = number.replaceFirst("0", "+254")
        }else if (number.length in 5..9){
            trimmedNumber = number
        }else if(number.contains("+254")){
            trimmedNumber = number
        }else if(number.contains("254")){
            trimmedNumber = "+$number"

        }
        else{
            Timber.d("Wrong Input")
        }

        return trimmedNumber
    }
}
