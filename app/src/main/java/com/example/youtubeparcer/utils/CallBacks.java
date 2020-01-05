package com.example.youtubeparcer.utils;

public interface CallBacks {
    void callbackObserver(Object object);

    interface playerCallBack {
        void onItemClickOnItem(int albumId);
        void onPlayingEnd();
    }
}
