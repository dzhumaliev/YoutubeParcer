package com.example.youtubeparcer.ui.detail_video
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.repository.MainRepository
import com.example.youtubeparcer.utils.App
import kotlinx.coroutines.launch

class DetailVideoViewModel : ViewModel() {
    val db = App.database.ytVideoDao()

    fun getVideoData(id: String) : LiveData<DetailVideoModel>? {
        return MainRepository.fetchVideoData(id)
    }

    suspend fun getDetailVideoList(): List<DetailVideoModel> {
        return db.getDetailVideo()
    }

    fun insertDetailVideoList(model: DetailVideoModel){
        viewModelScope.launch { db.insertVideoPlaylist(model) }
    }


}