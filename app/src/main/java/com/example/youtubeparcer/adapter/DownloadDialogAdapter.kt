package com.example.youtubeparcer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeparcer.R
import com.example.youtubeparcer.model.YtVideo

class DownloadDialogAdapter(val function: (YtVideo) -> Unit) :
    RecyclerView.Adapter<DownloadDialogAdapter.YouTubeViewHolder>() {


    private var list = mutableListOf<YtVideo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouTubeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_download_dialog, parent, false)
        return YouTubeViewHolder(view, function)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: YouTubeViewHolder, position: Int) {
        val model = list[position]
        holder.radioButton?.isChecked = model.isSelected
        if (list[position].height == -1) {
            holder.radioButton?.text = "Audio = ${list[position].audioFile!!.format.audioBitrate} kbit/s"
        } else {
            holder.radioButton?.text = if (list[position].videoFile!!.format.fps == 60)
                "Video ${list[position].height} p60"
            else
                "Video " + list[position].height.toString() + "p"
        }

        holder.radioButton?.setOnClickListener {
            clearSelectedItem()
            list[position].isSelected = true
            function(list[position])
            notifyDataSetChanged()
        }


    }

    fun updateData(newList: MutableList<YtVideo?>?) {
        list = newList as MutableList<YtVideo>
        notifyDataSetChanged()
    }


    class YouTubeViewHolder(itemView: View, val function: (YtVideo) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        var radioButton: RadioButton? = itemView.findViewById(R.id.radio_button)
    }

    private fun clearSelectedItem() {
        for (i in 0 until list.size) {
            if (list[i].isSelected) {
                list[i].isSelected = false
            }
        }
    }


}