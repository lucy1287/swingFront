package com.example.roadkill

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.roadkill.databinding.ItemUserImageBinding
import kotlin.collections.ArrayList

class ReportImageRVAdapter(private var imageList: ArrayList<*>, private val activity: UserReportActivity) :
    RecyclerView.Adapter<ReportImageRVAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemUserImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var thumbnail: ImageView
        fun onBind(uri: Uri) {
            thumbnail.setImageURI(uri)
        }

        init {
            thumbnail = binding.ivThumbnail
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
        val binding: ItemUserImageBinding =
            ItemUserImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(imageList[position] as Uri)
    }

    fun setImageList(list: ArrayList<*>) {
        imageList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }
}
