package com.example.youtubeparcer.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class PlayerManager {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ExoPlayerManager";
    private static PlayerManager mInstance = null;
    PlayerView mPlayerView;
    DefaultDataSourceFactory dataSourceFactory;
    String uriString = "";
    ArrayList<String> mPlayList = null;
    Integer playlistIndex = 0;
    CallBacks.playerCallBack listner;
    private SimpleExoPlayer mPlayer;

    private PlayerManager(Context mContext) {

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        mPlayerView = new PlayerView(mContext);
        mPlayerView.setUseController(true);
        mPlayerView.requestFocus();
        mPlayerView.setPlayer(mPlayer);

        Uri mp4VideoUri = Uri.parse(uriString);

        dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "androidwave"), BANDWIDTH_METER);

        final MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);
        mPlayer.prepare(videoSource);
        mPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                Log.i(TAG, "onTimelineChanged: ");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.i(TAG, "onTracksChanged: ");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.i(TAG, "onLoadingChanged: ");

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.i(TAG, "onPlayerStateChanged: ");
                if (playbackState == 4 && mPlayList != null && playlistIndex + 1 < mPlayList.size()) {
                    Log.e(TAG, "Song Changed...");

                    playlistIndex++;
                    listner.onItemClickOnItem(playlistIndex);
                    playStream(mPlayList.get(playlistIndex));
                } else if (playbackState == 4 && mPlayList != null && playlistIndex + 1 == mPlayList.size()) {
                    mPlayer.setPlayWhenReady(false);
                }
                if (playbackState == 4 && listner != null) {
                    listner.onPlayingEnd();
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.i(TAG, "onRepeatModeChanged: ");
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                Log.i(TAG, "onShuffleModeEnabledChanged: ");

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.i(TAG, "onPlayerError: ");
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Log.i(TAG, "onPositionDiscontinuity: ");

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.i(TAG, "onPlaybackParametersChanged: ");

            }

            @Override
            public void onSeekProcessed() {
                Log.i(TAG, "onSeekProcessed: ");

            }
        });


    }

    public static PlayerManager getSharedInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new PlayerManager(mContext);
        }
        return mInstance;

    }

    public void setPlayerListener(CallBacks.playerCallBack mPlayerCallBack) {
        listner = mPlayerCallBack;

    }

    public PlayerView getPlayerView() {
        return mPlayerView;
    }

    public void playStream(String urlToPlay) {
        uriString = urlToPlay;
        Uri mp4VideoUri = Uri.parse(uriString);
        MediaSource videoSource;

        if (uriString.toUpperCase().contains("M3U8")) {
            videoSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .setAllowChunklessPreparation(true)
                    .createMediaSource(mp4VideoUri, null, null);
        } else {
            mp4VideoUri = Uri.parse(urlToPlay);
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .setExtractorsFactory(new DefaultExtractorsFactory()).createMediaSource(mp4VideoUri);

        }


        if (mPlayer != null && videoSource != null) {
            mPlayer.prepare(videoSource);
            mPlayer.setPlayWhenReady(true);
        }


    }

    public void pausePlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.getPlaybackState();
        }
    }

    public void resumePlayer() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(true);
            mPlayer.getPlaybackState();
        }
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mInstance = null;
            mPlayer = null;
        }
    }

    public Boolean isPlayedPlaying() {
        return mPlayer.getPlayWhenReady();
    }

}
