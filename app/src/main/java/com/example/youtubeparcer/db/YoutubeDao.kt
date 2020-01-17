package com.example.youtubeparcer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.youtubeparcer.model.DetailPlaylistModel
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.PlaylistModel

@Dao
interface YoutubeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPlaylist(items: PlaylistModel)

    @Query("SELECT * FROM play_list")
    suspend fun getAllPlayList(): PlaylistModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailPlaylistData(items: DetailPlaylistModel)

    @Query("SELECT * FROM detail_playlist")
    suspend fun getDetailPlaylist(): List<DetailPlaylistModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoPlaylist(items: DetailVideoModel)


    @Query("SELECT * FROM detail_video_playlist")
    suspend fun getDetailVideo(): List<DetailVideoModel>
    //добавили insert и Query

}