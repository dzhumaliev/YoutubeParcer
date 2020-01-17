package com.example.youtubeparcer.type_converters

import androidx.room.TypeConverter
import com.example.youtubeparcer.model.ContentDetails
import com.example.youtubeparcer.model.Snippet
import com.google.gson.Gson

class ItemsItemTypeConverter {
    var gson = Gson()
    @TypeConverter
    public fun toSnippet(data: Snippet): String = gson.toJson(data)

    @TypeConverter
    public fun fromSnippet(value: String): Snippet = gson.fromJson(value, Snippet::class.java)

    @TypeConverter
    public fun toContentDetails(data: ContentDetails): String = gson.toJson(data)

    @TypeConverter
    public fun fromContentDetails(value: String): ContentDetails =
        gson.fromJson(value, ContentDetails::class.java)

}