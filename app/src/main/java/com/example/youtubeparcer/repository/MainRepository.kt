package com.example.youtubeparcer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.youtubeparcer.api.RetrofitClient
import com.example.youtubeparcer.api.YoutubeApi
import com.example.youtubeparcer.model.PlaylistModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainRepository {

    companion object {

        val channel = "UC0C-w0YjGpqDXGB8IHb662A"
        val apiKey = "AIzaSyCWK-EoCHecYMMFAvl-DI5iegR9s1WW20Y"
        val part = "snippet,contentDetails"
        val maxResult = "50"



        private lateinit var apiService: YoutubeApi
        fun fetchYoutubePlaylistData(): LiveData<PlaylistModel> {
            apiService = RetrofitClient.create()
            val data = MutableLiveData<PlaylistModel>()

            apiService.getPlayLists(part, apiKey, channel, maxResult).enqueue(object :
                Callback<PlaylistModel> {
                override fun onFailure(call: Call<PlaylistModel>, t: Throwable) {
                    data.value = null
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
    }
}