package com.example.youtubeparcer.ui
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.repository.MainRepository

class DetailVideoViewModel : ViewModel() {


    fun getVideoData(id: String) : LiveData<DetailVideoModel>?{
        return MainRepository.fetchVideoData(id)

    }
}