package com.example.youtubeparcer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.youtubeparcer.type_converters.ItemsItemTypeConverter
import com.example.youtubeparcer.type_converters.PlaylistModelTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "play_list")
@TypeConverters(PlaylistModelTypeConverter::class)
data class PlaylistModel(
    @SerializedName("kind")
    var kind: String,
    @SerializedName("pageInfo")
    var pageInfo: PageInfo,
    @SerializedName("etag")
    @PrimaryKey
    var etag: String,
    @SerializedName("items")
    var items: List<ItemsItem>
)

data class High(
    @SerializedName("width")
    val width: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int
)

data class PageInfo(
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int
)

data class ContentDetails(
    @SerializedName("itemCount")
    val itemCount: String
)

data class Medium(
    @SerializedName("width")
    val width: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int
)


data class Maxres(
    @SerializedName("width")
    val width: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int
)

data class Standard(
    @SerializedName("width")
    val width: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int
)

data class Thumbnails(
    @SerializedName("standard")
    val standard: Standard,
    @SerializedName("default")
    val default: Default,
    @SerializedName("high")
    val high: High,
    @SerializedName("maxres")
    val maxres: Maxres,
    @SerializedName("medium")
    val medium: Medium
)

@Entity(tableName = "items_item")
@TypeConverters(ItemsItemTypeConverter::class)
data class ItemsItem(
    @SerializedName("snippet")
    val snippet: Snippet,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    @PrimaryKey
    val id: String,
    @SerializedName("contentDetails")
    val contentDetails: ContentDetails
)

data class Snippet(
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("localized")
    val localized: Localized,
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnails")
    val thumbnails: Thumbnails,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("channelTitle")
    val channelTitle: String,
    @SerializedName("resourceId")
    val resourceId: ResourceId,
    @SerializedName("playlistId")
    val playlistId: String
)

data class Default(
    @SerializedName("width")
    val width: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int
)

data class Localized(
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String
)

data class ResourceId(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("videoId")
    val videoId: String
)