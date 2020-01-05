package com.example.youtubeparcer.model

import at.huber.youtubeExtractor.YtFile

data class YtVideo(
    var height: Int = 0,
    var audioFile: YtFile? = null,
    var videoFile: YtFile? = null,
    var isSelected: Boolean = false)