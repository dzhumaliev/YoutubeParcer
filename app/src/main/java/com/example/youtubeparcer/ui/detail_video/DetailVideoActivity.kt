package com.example.youtubeparcer.ui.detail_video

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.youtubeparcer.R
import com.example.youtubeparcer.adapter.DownloadDialogAdapter
import com.example.youtubeparcer.adapter.PlaylistAdapter
import com.example.youtubeparcer.model.DetailVideoModel
import com.example.youtubeparcer.model.YtVideo
import com.example.youtubeparcer.utils.CallBacks
import com.example.youtubeparcer.utils.DownloadMaster
import com.example.youtubeparcer.utils.PlayerManager
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.activity_detail_video.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailVideoActivity : AppCompatActivity(), CallBacks.playerCallBack {


    private var viewModel: DetailVideoViewModel? = null
    private var adapter: PlaylistAdapter? = null

    private lateinit var mediaPlayer: MediaPlayer

    private var videoId: String? = null
    private var playlistId: String? = null

    private val ITAG_FOR_AUDIO = 140

    private var writePermission = false
    private var selectedVideoQuality: String? = null
    private var selectedVideoExt: String? = null
    private var fileVideo: YtVideo? = null
    private var fileName: String? = null

    private lateinit var player: Player
    private lateinit var playerManager: PlayerManager

    private lateinit var dialogDownloadButton: Button
    private lateinit var dialogRecyclerView: RecyclerView

    private lateinit var dialogAdapter: DownloadDialogAdapter
    private var formatsToShowList: MutableList<YtVideo?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)
        viewModel = ViewModelProviders.of(this).get(DetailVideoViewModel::class.java)
        formatsToShowList = ArrayList()
        playerManager = PlayerManager.getSharedInstance(this)
        player = playerManager.playerView.player
        getExtra()
        setupViews()
        getDetailVideoPlaylistData()
        fetchDetailVideo()

    }

    private fun getExtra() {
        videoId = intent?.getStringExtra("videoId")
        playlistId = intent?.getStringExtra("playlistId")
    }

    private fun setupViews() {
        btn_download.setOnClickListener {
            checkRequestPermission()
            showDownloadDialog()
        }
    }

    private fun showDownloadDialog() {
        val builder = AlertDialog.Builder(this, R.style.DownloadDialog)

        val view = layoutInflater.inflate(R.layout.alert_download_dialog, null)
        builder.setView(view)
        dialogDownloadButton = view.findViewById(R.id.btn_alert_download)
        dialogRecyclerView = view.findViewById(R.id.alert_recycler_view)

        initDialogAdapter()
        dialogAdapter.updateData(formatsToShowList)
        val alert = builder.create()
        alert.show()
        downloadAction(alert)

    }

    private fun downloadAction(builder: AlertDialog) {
        dialogDownloadButton.setOnClickListener {
            var downloadName = fileName!!
            downloadName = downloadName.replace("[\\\\><\"|*?%:#/]".toRegex(), "")
            var downloadIds = ""

            try {
                if (fileVideo?.videoFile != null) {
                    downloadIds += DownloadMaster().downloadFile(
                        this,
                        fileVideo?.videoFile!!.url,
                        downloadName + "." + fileVideo?.videoFile!!.format.ext,
                        downloadName + "." + fileVideo?.videoFile!!.format.ext

                    )
                    downloadIds += "-"
                }
                if (fileVideo?.audioFile != null) {
                    downloadIds += DownloadMaster().downloadFile(
                        this,
                        fileVideo?.videoFile!!.url,
                        downloadName + "." + fileVideo?.videoFile!!.format.ext,
                        downloadName + "." + fileVideo?.videoFile!!.format.ext
                    )
                }

            } catch (e: Exception) {

            }
            builder.dismiss()
        }
    }


    private fun initDialogAdapter() {
        dialogAdapter = DownloadDialogAdapter { item: YtVideo -> downloadClickItem(item) }
        dialogRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        dialogRecyclerView.adapter = dialogAdapter
    }

    private fun downloadClickItem(item: YtVideo) {
        selectedVideoQuality = item.videoFile?.url
        selectedVideoExt = item.videoFile?.format?.ext
        fileVideo = item

    }

    private fun checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1024
            )
        } else {
            writePermission = true
        }
    }

    private fun fetchDetailVideo() {
        val data = videoId?.let { viewModel?.getVideoData(it) }
        data?.observe(this, Observer<DetailVideoModel> {
            val model: DetailVideoModel? = data.value
            when {
                model != null -> {
                    setData(model)
                }
            }
        })
    }

    private fun fetchDetailVideoListForDb() {
        val data = videoId?.let { viewModel?.getVideoData(it) }
        data?.observe(this, Observer<DetailVideoModel> {
            val model: DetailVideoModel? = data.value
            when {
                model != null -> {
                    setData(model)
                    insertInDbVideo(model)
                }
            }
        })
    }


    private fun insertInDbVideo(model: DetailVideoModel) {
        viewModel!!.insertDetailVideoList(model)
    }

    private fun setData(model: DetailVideoModel) {
        vd_title.text = model.items?.get(0)?.snippet?.title
        fileName = model.items?.get(0)?.snippet?.title
        vd_description.text = model.items?.get(0)?.snippet?.description
        val link = model.items?.get(0)?.id.toString()
        actualLink(link)

    }

    private fun setDataForDb(model: DetailVideoModel) {
        vd_title.text = model.items?.get(0)?.snippet?.title
        fileName = model.items?.get(0)?.snippet?.title
        vd_description.text = model.items?.get(0)?.snippet?.description
    }

    @SuppressLint("StaticFieldLeak")
    private fun actualLink(link: String) {
        object : YouTubeExtractor(this) {
            public override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                vMeta: VideoMeta
            ) {

                formatsToShowList = mutableListOf()
                var i = 0
                var itag: Int
                if (ytFiles != null) {
                    while (i < ytFiles.size()) {
                        itag = ytFiles.keyAt(i)
                        val ytFile = ytFiles.get(itag)

                        if (ytFile.format.height == -1 || ytFile.format.height >= 360) {
                            addFormatToList(ytFile, ytFiles)
                        }
                        i++
                    }
                }
                (formatsToShowList)?.sortWith(Comparator { lhs, rhs ->
                    lhs!!.height - rhs!!.height
                })
                try {
                    val yotutubeUrl: YtVideo? =
                        formatsToShowList?.get(formatsToShowList!!.lastIndex - 1)
                    playVideo(yotutubeUrl!!.videoFile?.url!!)
                } catch (e: Exception) {
                    Toast.makeText(this@DetailVideoActivity, "Error", Toast.LENGTH_SHORT).show()
                    makeVibrate()
                    customAudio()
                    finish()
                }

            }
        }.extract(link, true, true)
    }

    fun customAudio() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.ring1)
        mediaPlayer.start()

    }

    fun makeVibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    700,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(900)
        }
    }

    private fun addFormatToList(ytFile: YtFile, ytFiles: SparseArray<YtFile>) {
        val height = ytFile.format.height
        if (height != -1) {
            for (frVideo in this.formatsToShowList!!) {
                if (frVideo?.height == height && (frVideo?.videoFile == null || frVideo.videoFile!!.format.fps == ytFile.format.fps)) {
                    return
                }
            }
        }
        val frVideo = YtVideo()
        frVideo.height = height
        if (ytFile.format.isDashContainer) {
            if (height > 0) {
                frVideo.videoFile = ytFile
                frVideo.audioFile = ytFiles.get(ITAG_FOR_AUDIO)
            } else {
                frVideo.audioFile = ytFile
            }
        } else {
            frVideo.videoFile = ytFile
        }
        formatsToShowList!!.add(frVideo)
    }

    private fun playVideo(url: String) {
        player_view?.player = player
        PlayerManager.getSharedInstance(this).playStream(url)
        PlayerManager.getSharedInstance(this).setPlayerListener(this)
    }

    override fun onPlayingEnd() {
    }

    override fun onItemClickOnItem(albumId: Int) {

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player_view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            player_view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            player_view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            player_view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        }
    }

    private fun getDetailVideoPlaylistData() {   //в четвертых добавили это
        CoroutineScope(Dispatchers.Main).launch {
            val model = viewModel?.getDetailVideoList()
            if (model != null && !model.isNullOrEmpty()) {
                getExtraDetailVideoPlaylistData(model)
            } else {
                fetchDetailVideoListForDb()
            }
        }
    }

    private fun getExtraDetailVideoPlaylistData(model: List<DetailVideoModel>) {
        var detailVideo: DetailVideoModel? = null
        for (i in 0 until model.size) {
            for (z in 0 until model[i].items!!.size) {
                detailVideo = model[i]
            }
        }

        if (detailVideo != null) setDataForDb(detailVideo)
        else fetchDetailVideoListForDb()
    }
}