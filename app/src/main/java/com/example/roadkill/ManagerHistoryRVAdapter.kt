package com.example.roadkill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roadkill.databinding.ItemManagerHistoryBinding

class ManagerHistoryRVAdapter(private var accidentList: ArrayList<*>) :
    RecyclerView.Adapter<ManagerHistoryRVAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemManagerHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var date: TextView
        var name: TextView

        fun onBind(history: History) {
            date.text = history.date
            name.text = history.name
        }

        init {
            date = binding.tvAccidentHistoryDate
            name = binding.tvAccidentHistoryRoad

            binding.root.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (onItemClickListener != null) {
                        onItemClickListener!!.onItemClick(position)
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemManagerHistoryBinding =
            ItemManagerHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.onBind(videoList.get(position));
    }

    fun setVideoList(list: ArrayList<*>) {
        accidentList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return accidentList.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }
}
