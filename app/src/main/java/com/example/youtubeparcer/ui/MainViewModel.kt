package com.example.youtubeparcer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.repository.MainRepository
import com.example.youtubeparcer.utils.App
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {


    val db = App().getInstance().getDatabase()
    fun getPlaylistData() : LiveData<PlaylistModel> {
        return MainRepository.fetchYoutubePlaylistData()
    }

    fun insertPlaylistData(model: PlaylistModel) {
        viewModelScope.launch {
            db.ytVideoDao()?.insertAllPlaylist(model)
        }
    }

    suspend fun getDataFromDB() : PlaylistModel{
        return db.ytVideoDao().getAllPlayList()
    }

}