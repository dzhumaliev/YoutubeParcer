package com.example.youtubeparcer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.model.PlaylistModel
import com.example.youtubeparcer.repository.MainRepository

class MainViewModel : ViewModel() {


    fun getPlaylistData() : LiveData<PlaylistModel> {
        return MainRepository.fetchYoutubePlaylistData()
    }
}