package com.codepath.musichouse.classicmusic;


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
import com.codepath.musichouse.model.GenreModel;
import com.codepath.musichouse.service.IRequestInterface;
import com.codepath.musichouse.service.ServiceConnection;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassicFragment extends Fragment {
    private RecyclerView recyclerView;
    private IRequestInterface iRequestInterface;
    private SwipeRefreshLayout refreshLayout;
    private CompositeDisposable compositeDisposable;
    public ClassicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        iRequestInterface = ServiceConnection.getConnection();
        compositeDisposable = new CompositeDisposable();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classic, container, false);
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
       //  setRetainInstance(true);
        initRecycleView();
        refreshLayout = view.findViewById(R.id.swipeRefreshClassic);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              //  displayPopMusic();
                callService();
            }
        });
     //   displayPopMusic();
        callService();

    }
    /*
         Network Check: This method uses Android library Reactivenetwork for listening network connection state and Internet connectivity with RxJava Observables
         When Connectivity changes, subscriber will be notified. It constantly check if there is internet connection or not on a separate thread.
 */
    private void callService() {

        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean internetConnection) throws Exception {

                     if(internetConnection){
                         Toast.makeText(getContext(), "Connection", Toast.LENGTH_SHORT).show();
                         displayClassicMusic();
                     }else{
                         Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();

                     }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /*
       Implements IrequestInterface getClassicMusic method which returns Observable<model> objects which emits values pinged from the web
     */
    private void displayClassicMusic() {
        compositeDisposable.add(iRequestInterface.getClassicMusic()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GenreModel>() {
                    @Override
                    public void accept(GenreModel genreModel) throws Exception {
                        //Toast.makeText(getActivity(), genreModel.getResults().get(0).getArtistName(), Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(new MusicAdapter(getActivity(),this ,genreModel.getResults(),R.layout.row_music_details));
                        refreshLayout.setRefreshing(false);
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
        Function to initialize RecycleView
     */
    private void initRecycleView() {

        recyclerView =(RecyclerView) getActivity().findViewById(R.id.rvClassicMusic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
