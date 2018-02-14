package com.codepath.musichouse.rockmusic;

import com.codepath.musichouse.data.network.IDataManager;
import com.codepath.musichouse.data.network.model.GenreModel;
import com.codepath.musichouse.data.network.service.ApiList;
import com.codepath.musichouse.ui.base.BasePresenter;
import com.codepath.musichouse.ui.base.MvpView;
import com.codepath.musichouse.ui.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by uchit on 13/02/2018.
 */

public class RockMusicPresenterIml <V extends IRockMusicMvpView>
        extends BasePresenter<V> implements IRockMusicPresenter<V>{


    public RockMusicPresenterIml(IDataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadMusicList() {
        getDataManager().getRockMusic()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<GenreModel>() {
                    @Override
                    public void accept(GenreModel genreModel) throws Exception {
                        getMvpView().onFetchDataSucess(genreModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().onFetchDataError(throwable.getMessage());
                    }
                });


    }
}
