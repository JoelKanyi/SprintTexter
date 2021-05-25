package com.kanyideveloper.sprinttexter.ui.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kanyideveloper.sprinttexter.adapters.HistoryAdapter
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDao
import com.kanyideveloper.sprinttexter.data.database.TextsHistoryDatabase
import com.kanyideveloper.sprinttexter.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyDao: TextsHistoryDao
    private lateinit var viewModel: HistoryViewModel
    private val adapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        historyDao = TextsHistoryDatabase.getInstance(application).textsHistoryDao
        val historyViewModelFactory = HistoryViewModelFactory(historyDao, application)
        viewModel =
            ViewModelProvider(this, historyViewModelFactory).get(HistoryViewModel::class.java)

        viewModel.history.observe(viewLifecycleOwner, { historyList ->
            adapter.submitList(historyList)
            binding.historyRecyclerview.adapter = adapter
        })

        return view
    }
}