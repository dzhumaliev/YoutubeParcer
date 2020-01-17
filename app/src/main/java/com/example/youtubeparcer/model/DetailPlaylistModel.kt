package com.example.youtubeparcer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.youtubeparcer.type_converters.PlaylistModelTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "detail_playlist")
//Во первых добавил Энтити и Type cpnverter + указали Primary Key



@TypeConverters(PlaylistModelTypeConverter::class)
data class DetailPlaylistModel(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo,
    @SerializedName("etag")
    @PrimaryKey
    val etag: String,
    @SerializedName("items")
    val items: List<ItemsItem>

)