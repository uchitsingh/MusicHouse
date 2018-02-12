package com.codepath.musichouse.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.musichouse.ItemClickListener;
import com.codepath.musichouse.MainActivity;
import com.codepath.musichouse.R;
import com.codepath.musichouse.controller.RealmBackupRestore;
import com.codepath.musichouse.controller.RealmHelper;
import com.codepath.musichouse.model.GenreModel;
import com.codepath.musichouse.model.Result;
import com.codepath.musichouse.rockmusic.RockFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.realm.Realm;

/**
 * Created by uchit on 11/02/2018.
 * This is the adapter class in which we initialize our recyclerView , and update our views.
 * It also uses RockFragment saveDataToRealm() method to save the data pinged from web api to Realm.
 */

public class RockAdapter extends RecyclerView.Adapter<RockAdapter.MusicHolder> {
    Context applicationContext;
    Consumer<GenreModel> consumer ;
    List<Result> results;
    private int row_music_details;
    private Realm realm;
    private RealmHelper realmHelper;
    private RealmBackupRestore realmBackupRestore;
    private Activity activity;


    public RockAdapter( Activity activity,   Consumer<GenreModel> consumer, List<Result> results, int row_music_details) {
        this.applicationContext = activity.getApplicationContext();
        this.consumer = consumer;
        this.row_music_details = row_music_details;
        this.results = results;
        this.activity = activity;

    }

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        initRealm();
        realmBackupRestore = new RealmBackupRestore(activity);
        return new MusicHolder(LayoutInflater.from(parent.getContext()).inflate(row_music_details,parent,false));
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, int position) {

        if(results.get(position).getTrackPrice() != null){

            holder.mTrackPrice.setText(String.valueOf(results.get(position).getTrackPrice()) +" USD");
        }else{
            holder.mTrackPrice.setText("Not availiable");
        }

        holder.mArtistName.setText(results.get(position).getArtistName());
        holder.mCollectionName.setText(results.get(position).getCollectionName());
        //Picasso.with(applicationContext).load("http://i.imgur.com/DvpvklR.png").into(holder.mArtwork);

        Picasso.with(applicationContext).load(results.get(position).getArtworkUrl60())
                .resize(350, 350)
                .centerCrop()
                .into(holder.mArtwork);
        //   Log.i("image2222", results.get(position).getArtworkUrl60());

        holder.callItemClick(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                playMedia(Uri.parse(results.get(position).getPreviewUrl()));
            }
        });



        //Save Data To Realm
        RockFragment rockFragment =(RockFragment) MainActivity.fragmentManager.findFragmentById(R.id.fragmentContainer);
        if(rockFragment instanceof RockFragment && !rockFragment.getRetainInstance() ){
            rockFragment.saveDataToRealm(holder.mArtistName.getText().toString(), holder.mCollectionName.getText().toString(), holder.mCollectionName.getText().toString()
                    , Double.parseDouble(holder.mTrackPrice.getText().toString().replaceAll(" USD","")));
            Log.i("fragment happend","sss");
        }


    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public class MusicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mArtistName, mCollectionName, mTrackPrice;
        private ImageView mArtwork;
        private ItemClickListener itemClickListener; //create an object of the interface


        public MusicHolder(View itemView) {
            super(itemView);

            mArtistName = (TextView) itemView.findViewById(R.id.tvArtistName);
            mCollectionName =(TextView) itemView.findViewById(R.id.tvCollectionName);
            mTrackPrice = (TextView) itemView.findViewById(R.id.tvTrackPrice);
            mArtwork = (ImageView) itemView.findViewById(R.id.iv_artWork);
            itemView.setOnClickListener(this);
        }

        public void callItemClick(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View view) {itemClickListener.onClick(view, getPosition());
        }
    }

    /*
        Method to playMedia file from a Uri File, using Implict Intent.
     */
    public void playMedia(Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "audio/*");
//        intent.setData(file);
        if (intent.resolveActivity(applicationContext.getPackageManager()) != null) {
            applicationContext.startActivity(intent);
        }
    }
}
