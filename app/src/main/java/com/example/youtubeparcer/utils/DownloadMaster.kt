package com.example.youtubeparcer.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment

class DownloadMaster  {

    fun downloadFile(context: Context, url: String, ext: String?, fileName: String?
    ) {
        var request: DownloadManager.Request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle("$fileName.$ext") // Title of the Download Notification
                .setDescription("Downloading") // Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE) // Visibility of the download Notification
                .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true)

        val downloadManager: DownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }


}