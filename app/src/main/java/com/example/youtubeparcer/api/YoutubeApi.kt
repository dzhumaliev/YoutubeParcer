package com.example.youtubeparcer.api

import com.example.youtubeparcer.model.DetailPlaylistModel
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.PlaylistModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {

    @GET("youtube/v3/playlists")
    fun getPlayLists(
        @Query("part") part: String,
        @Query("key") apiKey: String,
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: String
    ): Call<PlaylistModel>

    @GET("youtube/v3/playlistItems")
    fun getDetailPlaylist(
        @Query("part") part: String,
        @Query("key") apiKey: String,
        @Query("playlistId") playlistId: String,
        @Query("maxResults") maxResults: String
    ): Call<DetailPlaylistModel>

    @GET("youtube/v3/videos")
    fun getDetailVideo(
        @Query("key") apiKey: String,
        @Query("part") part: String,
        @Query("id") id: String?
    ): Call<DetailVideoModel>
}