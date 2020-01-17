package com.example.youtubeparcer.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.youtubeparcer.api.RetrofitClient
import com.example.youtubeparcer.api.YoutubeApi
import com.example.youtubeparcer.model.DetailPlaylistModel
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.PlaylistModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainRepository {

    companion object {

        val channel = "UCCmFDKAKbiTJg9NWsGk5nEA"
        val apiKey = "AIzaSyCWK-EoCHecYMMFAvl-DI5iegR9s1WW20Y"
        val part = "snippet,contentDetails"
        val maxResult = "50"

        private lateinit var apiService: YoutubeApi
        fun fetchYoutubePlaylistData(): LiveData<PlaylistModel> {
            apiService = RetrofitClient.create()
            val data = MutableLiveData<PlaylistModel>()

            apiService.getPlaylists(part, apiKey, channel, maxResult).enqueue(object :
                Callback<PlaylistModel> {
                override fun onFailure(call: Call<PlaylistModel>, t: Throwable) {
                    data.value = null
                    Log.v("TAG", t.message)
                }

                override fun onResponse(
                    call: Call<PlaylistModel>,
                    response: Response<PlaylistModel>
                ) {
                    data.value = response.body()
                }

            })

            return data
        }


        fun fetchYoutubeDetailPlaylistData(playlistId: String): LiveData<DetailPlaylistModel>? {
            apiService = RetrofitClient.create()
            val data = MutableLiveData<DetailPlaylistModel>()
            apiService.getDetailPlaylist(part, apiKey, playlistId, maxResult).enqueue(object : Callback<DetailPlaylistModel> {

                override fun onResponse(call: Call<DetailPlaylistModel>, response: Response<DetailPlaylistModel>) {
                    data.value = response.body()
                }

                override fun onFailure(call: Call<DetailPlaylistModel>, t: Throwable) {
                    Log.v("response_fail", t.message)
                    data.value = null
                }

            })

            return data
        }

        fun fetchVideoData(videoId: String): LiveData<DetailVideoModel>? {
            apiService = RetrofitClient.create()
            val data = MutableLiveData<DetailVideoModel>()
            apiService.getDetailVideo(apiKey, part, videoId).enqueue(object : Callback<DetailVideoModel> {
                override fun onFailure(call: Call<DetailVideoModel>, t: Throwable) {
                    data.value = null
                }

                override fun onResponse(
                    call: Call<DetailVideoModel>,
                    response: Response<DetailVideoModel>
                ) {
                    data.value = response.body()
                }

            })

            return data
        }

    }
}