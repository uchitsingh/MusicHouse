package com.codepath.musichouse;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by uchit on 11/02/2018.
 * This class initializes Default configuration For the whole of the MusicHouse application
 */

public class MyApp extends Application {
    private static MyApp sInstance;
    private static Context context;

    public static MyApp getInstance() {
        if (sInstance == null) {
            sInstance = new MyApp();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        configRealm();
    }



    public Context getAppContext(){
        return context;
    }

    /*
     Function to allow configuration
    */
    public void configRealm(){

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(1) //Droping is different than Deleting, If i have only one column, drop existing structure of table, add new one but not erasing the  value of the existing table
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

    }

}
