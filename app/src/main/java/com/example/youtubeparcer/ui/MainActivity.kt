package com.example.youtubeparcer.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.model.ItemsItem
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.ui.detail_playlist.DetailPlayListActivity
import com.example.youtubeparcer.utils.UiHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.splashscreen.*

class MainActivity : AppCompatActivity() {

    private val data = mutableListOf<String>()
    private var viewModel: MainViewModel? = null
    private var adapter: PlaylistAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initAdapter()
        fetchPlaylist()


    }

    private fun initAdapter() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = PlaylistAdapter { item: ItemsItem -> clickItem(item) }
        recycler_view.adapter = adapter

    }

    private fun clickItem(item: ItemsItem) {
        UiHelper().showToast(this, "${item.id} clicked")
        val intent = Intent(this, DetailPlayListActivity::class.java)
        intent.putExtra("id", item.id)
        intent.putExtra("title", item.snippet.title)
        intent.putExtra("channelId", item.snippet.channelId)
        intent.putExtra("title", item.snippet.title)
        startActivity(intent)
    }


    private fun fetchPlaylist() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        if (isConnected) {
            val data = viewModel?.getPlaylistData()
            data?.observe(this, Observer<PlaylistModel>
            {
                val model: PlaylistModel? = data.value
                when {
                    model != null -> {
                        updateAdapterData(model)
                    }
                }
            })
        } else {
            setContentView(R.layout.splashscreen)
            buttonInSplash()
            
        }

    }

    private fun buttonInSplash() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        spl_button.setOnClickListener {
            if (isConnected) {
                setContentView(R.layout.activity_main)
                fetchPlaylist()

            } else {
                setContentView(R.layout.splashscreen)
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    private fun updateAdapterData(list: PlaylistModel?) {
        val data = list?.items
        adapter?.updateData(data)
    }
}
