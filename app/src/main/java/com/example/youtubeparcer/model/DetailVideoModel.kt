package com.example.youtubeparcer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.youtubeparcer.type_converters.PlaylistModelTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "detail_video_playlist")
@TypeConverters(PlaylistModelTypeConverter::class)
data class DetailVideoModel(
    @SerializedName("kind")
    val kind: String = "",
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @PrimaryKey
    @SerializedName("etag")
    val etag: String = "",
    @SerializedName("items")
    val items: List<ItemsItem>?
)