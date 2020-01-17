package com.example.youtubeparcer.utils

import android.content.Context
import android.net.ConnectivityManager

class InternetHelper {


    fun checkInternetConnection(context: Context): Boolean {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo != null
    }
}