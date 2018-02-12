package com.codepath.musichouse.controller;

import com.codepath.musichouse.model.GenreModel;
import com.codepath.musichouse.model.Result;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by uchit on 11/02/2018.
 * 
 */

public class RealmHelper {
    Realm realm;
    
    public RealmHelper(Realm realm ){
        this.realm = realm;
    }
    
    /*
    Make sure the function runs on seperate Thread when saving the data to Realm
    uses Executor Framework.
     */
    public void saveGenres(final Result result){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(result);
            }
        });
    }
    
    /*
    Read Genre Data from Realm Database
     */
   public ArrayList<Result> getGenres(){
       ArrayList<Result> genreResults = new ArrayList<>();
       RealmResults<Result> realmResults = realm.where(Result.class).findAll();
       for(Result result :realmResults){
           genreResults.add(result);
       }
       return genreResults;
   }
    
    
}
