package com.kanyideveloper.sprinttexter.ui.fragments.texter

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.kanyideveloper.sprinttexter.R
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDatabase
import com.kanyideveloper.sprinttexter.databinding.FragmentTexterBinding
import com.kanyideveloper.sprinttexter.utils.SmsDeliveredBroadcastReceiver
import com.kanyideveloper.sprinttexter.utils.SmsSentBroadcastReciever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class TexterFragment : Fragment() {

    private val smsSentReceiver by lazy { SmsSentBroadcastReciever() }
    private val smsDeliveredBroadcastReceiver by lazy { SmsDeliveredBroadcastReceiver() }
    private lateinit var binding: FragmentTexterBinding
    private lateinit var viewModelFactory: TexterViewModelFactory
    private lateinit var viewModel: TexterViewModel
    private lateinit var textsHistoryDao: TextsHistoryDao

    private lateinit var textMessage: String
    private lateinit var phoneNum: String
    private val simCard: String = "1"
    private var totalSms: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.d("onCreateView")

        setHasOptionsMenu(true)

        binding = FragmentTexterBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application

        textsHistoryDao = TextsHistoryDatabase.getInstance(application).textsHistoryDao

        viewModelFactory =
            TexterViewModelFactory(
                application,
                smsSentReceiver,
                smsDeliveredBroadcastReceiver,
                textsHistoryDao
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(TexterViewModel::class.java)

        val sentPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_SENT_ACTION"), 0)
        val deliveredPI = PendingIntent.getBroadcast(activity, 0, Intent("SMS_DELIVERED_ACTION"), 0)

        binding.buttonSend.setOnClickListener {

            viewModel.doneCounting()

            if (binding.smsCount.text.toString().trim().isEmpty()) {
                binding.smsCount.error = "Require an SMS count"
            }
            if (binding.message.text.toString().trim().isEmpty()) {
                binding.message.error = "Require a message"
            }
            if (binding.smsToWho.text.toString().trim().isEmpty()) {
                binding.smsToWho.error = "Require destination"
            } else {

                if (checkIfPhoneNumberOrCompanyNumber(binding.smsToWho.text.toString()) == null) {
                    Toast.makeText(
                        requireContext(),
                        "Please input a correct number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener

                } else {

                    textMessage = binding.message.text.toString().trim()
                    phoneNum = binding.smsToWho.text.toString().trim()
                    totalSms = binding.smsCount.text.toString().toInt()

                    CoroutineScope(Dispatchers.Main).launch {
                        Timber.d("TexterFragment: Call sendSms in ViewModel")
                        viewModel.sendSms(
                            binding.smsCount.text.toString().toInt(),
                            binding.smsToWho.text.toString().trim(),
                            binding.message.text.toString().trim(),
                            sentPI, deliveredPI
                        )
                    }
                }
            }
        }

        viewModel.smsCount.observe(viewLifecycleOwner, Observer { counterValue ->
            if (binding.smsCount.text.toString() == "") {
                return@Observer
            }else if(totalSms == (counterValue + 1)){
                viewModel.youCanNowSave()
            }

            else {
                binding.progressCircular.isVisible = true
                binding.textView254.text = counterValue.toString()
                if (binding.smsCount.toString() == counterValue.toString()){
                    binding.progressCircular.isVisible = false
                }
            }
        })

        viewModel.save.observe(viewLifecycleOwner, Observer {
            if (it){
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.saveToDb(textMessage, totalSms, phoneNum, simCard)
                    viewModel.doneSaving()
                }
            }
        })

        return view
    }

    private fun disableProgressbar(){

    }

    private fun checkIfPhoneNumberOrCompanyNumber(number: String): String? {
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

        if (number.length == 10) {
            trimmedNumber = number.replaceFirst("0", "+254")
        } else if (number.length in 3..9) {
            trimmedNumber = number
        } else if (number.contains("+254")) {
            trimmedNumber = number
        } else if (number.contains("254")) {
            trimmedNumber = "+$number"

        } else {
            Timber.d("Wrong Input")
        }

        return trimmedNumber
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.my_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.about -> {
                Toast.makeText(requireContext(), "About", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(R.id.action_texterFragment_to_aboutFragment)
            }
            R.id.history -> {
                Toast.makeText(requireContext(), "History", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(R.id.action_texterFragment_to_historyFragment)
            }
            R.id.feedback -> {
                Toast.makeText(requireContext(), "Feedback", Toast.LENGTH_SHORT).show()
            }
            R.id.help -> {
                Toast.makeText(requireContext(), "Help", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
