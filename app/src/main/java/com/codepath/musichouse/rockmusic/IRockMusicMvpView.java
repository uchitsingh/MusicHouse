package com.codepath.musichouse.rockmusic;


import com.codepath.musichouse.data.network.model.GenreModel;

import com.codepath.musichouse.ui.base.MvpView;

/**
 * Created by uchit on 13/02/2018.
 */

public interface IRockMusicMvpView extends MvpView {

    void onFetchDataProgress();
    void onFetchDataSucess(GenreModel genreModel);
    void onFetchDataError(String error);
}
