package com.codepath.musichouse.rockmusic;


import com.codepath.musichouse.ui.base.MvpPresenter;

/**
 * Created by uchit on 13/02/2018.
 */
/*
    Methods called by the View.
 */
public interface IRockMusicPresenter <V extends IRockMusicMvpView> extends MvpPresenter<V> {
        void loadMusicList();

}
