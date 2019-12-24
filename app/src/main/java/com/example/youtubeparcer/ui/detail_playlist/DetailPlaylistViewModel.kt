package com.example.youtubeparcer.ui.detail_playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.model.DetailPlaylistModel
import com.example.youtubeparcer.repository.MainRepository


class DetailPlaylistViewModel : ViewModel(){

    fun fetchDetailPlaylistData(id: String): LiveData<DetailPlaylistModel>? {
        return MainRepository.fetchYoutubeDetailPlaylistData(id)
    }





}

