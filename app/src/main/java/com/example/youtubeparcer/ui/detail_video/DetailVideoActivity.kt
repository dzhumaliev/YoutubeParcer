package com.example.youtubeparcer.ui.detail_video

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.ui.DetailVideoViewModel

class DetailVideoActivity : AppCompatActivity() {

    private var viewModel: DetailVideoViewModel? = null
    private var adapter: PlaylistAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)
        viewModel = ViewModelProviders.of(this).get(DetailVideoViewModel::class.java)
        fetchVideoDetailVideo()

    }

    private fun fetchVideoDetailVideo() {
        val data = viewModel?.getPlaylistDataOne()
        data?.observe(this, Observer<PlaylistModel> {
            val model: PlaylistModel? = data.value
            when {
                model != null -> {
                    updateAdapterData(model)
                }
            }
        })
    }

    private fun updateAdapterData(list: PlaylistModel?) {
        val data = list?.items
        adapter?.updateData(data)
    }

}
