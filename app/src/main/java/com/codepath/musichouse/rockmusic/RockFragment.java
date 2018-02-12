package com.codepath.musichouse.rockmusic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.codepath.musichouse.R;
import com.codepath.musichouse.adapter.MusicAdapter;
import com.codepath.musichouse.adapter.RockAdapter;
import com.codepath.musichouse.controller.RealmBackupRestore;
import com.codepath.musichouse.controller.RealmHelper;
import com.codepath.musichouse.model.GenreModel;
import com.codepath.musichouse.model.Result;
import com.codepath.musichouse.service.IRequestInterface;
import com.codepath.musichouse.service.ServiceConnection;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * It displays list of music by Rock Genre.
 */
public class RockFragment extends Fragment {

    private IRequestInterface requestInterface;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Realm realm;
    private RealmHelper realmHelper;
    private RealmBackupRestore realmBackupRestore;
    private ArrayList<Result> genreModelArrayList;
    private CompositeDisposable compositeDisposable;

    /*
     Required empty public constructor
     */
    public RockFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       requestInterface = ServiceConnection.getConnection();
        compositeDisposable = new CompositeDisposable();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rock, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(compositeDisposable !=null & !compositeDisposable.isDisposed()){
            compositeDisposable.clear();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        initRealm();
        initRecycleView();
        //intefaceData =(IntefaceData) getActivity();

        refreshLayout = view.findViewById(R.id.swipeRefreshRock);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callService();
              //  displayRockMusic();
            }
        });
        callService();
      //  displayRockMusic();
        realmBackupRestore = new RealmBackupRestore(getActivity());
    }


    /*
           Network Check: This method uses Android library Reactivenetwork for listening network connection state and Internet connectivity with RxJava Observables
           When Connectivity changes, subscriber will be notified. It constantly check if there is internet connection or not on a separate thread.
   */
    public void callService(){

    ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean internetConnection) throws Exception {
                    if(internetConnection){
                        Toast.makeText(getContext(), "Conection", Toast.LENGTH_SHORT).show();
                        displayRockMusic();


                    }else{

                        //intefaceData.saveData(String artistName, String collectionName, String artWorkUrl, Double price);

                        genreModelArrayList = realmHelper.getGenres();
                        displayRockMusic();

                        Toast.makeText(getContext(), "No conection", Toast.LENGTH_SHORT).show();

                    }
                }
            });


    }
    /*
        Function to initialize Realm
     */
    private void initRealm() {
        realm = realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);

    }
    /*
    This method initializes recycler view
     */
    private void initRecycleView() {
        recyclerView =(RecyclerView) getActivity().findViewById(R.id.rvRockMmusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    /*
       Implements IrequestInterface getRockMusic method which returns Observable<model> objects which gets items pinged from the web
     */
    private void displayRockMusic() {
        //genreModelArrayList = new ArrayList<Result>();
        compositeDisposable.add(requestInterface.getRockMusic()
                .subscribeOn(Schedulers.newThread()) //which thread the observable will begin operating on
                .observeOn(AndroidSchedulers.mainThread()) //
                .subscribe(new Consumer<GenreModel>() { //connect an observer to an observable
                    @Override
                    public void accept(GenreModel genreModel) throws Exception {

                    genreModelArrayList = (ArrayList) genreModel.getResults();

                    recyclerView.setAdapter(new RockAdapter( getActivity(), this, genreModelArrayList,R.layout.row_music_details));
                    refreshLayout.setRefreshing(false);

                        //      Toast.makeText(getActivity(),genreModel.getResults().get(0).getArtistName(), Toast.LENGTH_SHORT).show();
                    //    Log.i("Artist", genreModel.getResults().get(0).getArtistName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                }));

    }
    /*
       Helper method to saveDataToRealm
     */
    public void saveDataToRealm(String artistName, String collectionName, String artWorkUrl, Double price) {
        Result result = new Result(artistName, collectionName,artWorkUrl
                , price);
        realmHelper.saveGenres(result);
        realmBackupRestore.backup();
    }

}
