package com.kanyideveloper.sprinttexter.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kanyideveloper.sprinttexter.data.database.TextsHistory
import com.kanyideveloper.sprinttexter.databinding.HistoryRowBinding

class HistoryAdapter : ListAdapter<TextsHistory, HistoryAdapter.MyViewHolder>(DiffUtilCallback) {

    object DiffUtilCallback : DiffUtil.ItemCallback<TextsHistory>() {
        override fun areItemsTheSame(oldItem: TextsHistory, newItem: TextsHistory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TextsHistory, newItem: TextsHistory): Boolean {
            return oldItem.id == newItem.id
        }

    }

    inner class MyViewHolder(private val binding: HistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: TextsHistory?) {
            binding.historyDate.text = history?.date
            binding.historyContent.text =
                "Texted ${history?.smsCount} SMS to ${history?.phoneNumber} from SIM ${history?.simCard}"
            binding.textMessage.text = "Message: ${history?.sentText}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            HistoryRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }
}