package com.example.youtubeparcer.api

import com.example.youtubeparcer.model.PlaylistModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("youtube/v3/playlists")
    fun getPlayLists(
        @Query("part") part: String,
        @Query("key") key: String,
        @Query("channelId") channelId: String,
        @Query("maxResult") maxResult: String
    ): Call<PlaylistModel>
}