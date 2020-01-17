package com.example.youtubeparcer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.model.ItemsItem
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.ui.detail_playlist.DetailPlaylistActivity
import com.example.youtubeparcer.utils.InternetHelper
import com.example.youtubeparcer.utils.UiHelper
import com.example.youtubeparcer.utils.gone
import com.example.youtubeparcer.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    private var viewModel: MainViewModel? = null
    private var adapter: PlaylistAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initAdapter()
//        fetchPlaylist()
        getDataFromDatabase()
        viewsActions()
    }


    private fun viewsActions() {
        btn_retry.setOnClickListener {
            fetchPlaylist()
        }
    }

    private fun initAdapter() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = PlaylistAdapter() {item: ItemsItem -> clickItem(item)}
        recycler_view.adapter = adapter

    }

    private fun clickItem(item: ItemsItem) {
        val intent = Intent(this, DetailPlaylistActivity::class.java)
        intent.putExtra("id", item.id)
        intent.putExtra("title", item.snippet.title)
        intent.putExtra("channelTitle", item.snippet.channelId)
        intent.putExtra("etag", item.etag)
        intent.putExtra("item_count", item.contentDetails.itemCount)
        startActivity(intent)
    }

    private fun fetchPlaylist() {
        if (InternetHelper().checkInternetConnection(this)) {
            network_container.gone()
            recycler_view.visible()
            val data = viewModel?.getPlaylistData()
            data?.observe(this, Observer<PlaylistModel> {
                val model: PlaylistModel? = data.value
                when {
                    model != null -> {
                        updateDatabasePlaylist(model)
                        updateAdapterData(model)
                    }
                }

            })
        } else {
            showEmptyState()
            UiHelper().showToast(this, getString(R.string.no_internet_connection))
        }
    }

    private fun showEmptyState() {
        network_container.visible()
        recycler_view.gone()
    }

    private fun updateAdapterData(model: PlaylistModel?) {
        val data = model?.items
        adapter?.updateData(data)
    }

    private fun updateDatabasePlaylist(model: PlaylistModel?) {
        model?.let { viewModel?.insertPlaylistData(it) }
    }

    private fun getDataFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val model = viewModel?.getDataFromDB()
            if (model != null && !model.items.isNullOrEmpty()) {
                updateAdapterData(model)
                fetchNewPlaylistData()
            } else {
                fetchPlaylist()
            }
        }
    }

    private fun fetchNewPlaylistData() {
        val data = viewModel?.getPlaylistData()
        data?.observe(this, Observer<PlaylistModel> {
            val model: PlaylistModel? = data.value
            when {
                model != null -> {
                    updateDatabasePlaylist(model)
                    updateAdapterData(model)
                }
            }
        })
    }

}