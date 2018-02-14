package com.codepath.musichouse.data.network;

import com.codepath.musichouse.data.network.model.GenreModel;

import io.reactivex.Observable;

/**
 * Created by uchit on 13/02/2018.
 */

public interface IApiHelper {

    Observable<GenreModel> getRockMusic();
}
