package com.codepath.musichouse.data.network;

import com.codepath.musichouse.data.network.model.GenreModel;
import com.codepath.musichouse.data.network.service.IRequestInterface;
import com.codepath.musichouse.data.network.service.ServiceConnection;

import io.reactivex.Observable;

/**
 * Created by uchit on 13/02/2018.
 */

public class AppApiHelper implements IApiHelper {
    private IRequestInterface iRequestInterface;

    public AppApiHelper() {
        iRequestInterface = ServiceConnection.getConnection();
    }

    @Override
    public Observable<GenreModel> getRockMusic() {
        return iRequestInterface.getRockMusic();


    }
}
