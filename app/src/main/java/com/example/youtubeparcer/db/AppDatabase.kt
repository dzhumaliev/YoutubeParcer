package com.example.youtubeparcer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.youtubeparcer.model.DetailPlaylistModel
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.PlaylistModel

@Database(
    entities = [
        PlaylistModel::class,
        DetailPlaylistModel::class,
        DetailVideoModel::class], /// во вторых добавил Этити и указал класс

    version = 3, exportSchema = false
)  /// version увеличивается при добаввлении энтити
abstract class AppDatabase : RoomDatabase() {
    abstract fun ytVideoDao(): YoutubeDao
}