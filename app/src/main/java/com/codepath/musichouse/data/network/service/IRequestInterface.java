package com.codepath.musichouse.data.network.service;

import com.codepath.musichouse.data.network.model.GenreModel;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by uchit on 11/02/2018.
 * Uses Retrofit to turn your REST API into a JAVA Interface
 * HTTP request definition

 */

public interface IRequestInterface {

    @GET(ApiList.ROCK_API)
    Observable<GenreModel> getRockMusic();

    @GET(ApiList.CLASSIC_API)
    Observable<GenreModel> getClassicMusic();

    @GET(ApiList.POP_API )
    Observable<GenreModel> getPopMusic();
}
