package com.codepath.musichouse.service;

import android.util.Log;


import com.codepath.musichouse.MyApp;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by uchit on 11/02/2018.
 */


public class ServiceConnection {

   static OkHttpClient okHttpClient;
   static Retrofit retrofit;
    private static final int CACHE_SIZE = 10*1024*1024;
    private static final int MAX_STALE = 60*60*1;
    private static final int MAX_AGE = 60; //1min
    private static final String HEADER_NAME = "Cache-Control";

    /*
        This static method returns IrequestInterface, and  initialzes cache, and OkHttpClient for logging, error checking assistance.
        We set the Retrofit class to generate an implementation of IrequestInterface, for Background Http processing of APIs into Java interface.
     */
   public static IRequestInterface getConnection(){

       // Location of the cache.
       //File httpCacheDirectory = new File(MyApp.getInstance().getAppContext().getCacheDir(),  "responses");

       // Initialise the cache.
       Cache cache = new Cache(MyApp.getInstance().getAppContext().getCacheDir(), CACHE_SIZE); //cacheDirectory , in emulator , it will have a cache location, path

       HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
       interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //logging, error checking ,Assistant
       OkHttpClient client = new OkHttpClient.Builder()
               .addInterceptor(x ->{
                   Response response = x.proceed(x.request());


                   if(response.networkResponse()!=null){
                       Log.i("cache", "Network response");
                   }

                   if(response.cacheControl()!=null){
                       Log.i("cache", "Cached response");
                   }
                   return response;
               })
               .addInterceptor(interceptor)
               .addNetworkInterceptor(new getRewriteResponseInterceptor())
               .cache(cache)
               .build();


       retrofit= new Retrofit.Builder()
               .baseUrl(ApiList.BASE_URL)
               .client(client)
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Allows to inflate the recyclerview adapter
               .addConverterFactory(GsonConverterFactory.create())// adds gson converter
               .build();


       return  retrofit.create(IRequestInterface.class);
   }



    //Caching Mechanism
    public static class getRewriteResponseInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header(HEADER_NAME);

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                Log.i("Values", "REWRITE_RESPONSE_CACHE");

                return originalResponse.newBuilder()
                        .header(HEADER_NAME, "public, max-age=" + MAX_AGE)
                        .build();
            }

            else {
                Log.i("Values", "REWRITE_RESPONSE_INTERCEPTOR");
                return originalResponse;
            }
        };
    }
}
