package com.example.youtubeparcer.utils

import android.content.Context
import android.widget.Toast

class UiHelper {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}