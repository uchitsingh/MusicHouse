package com.codepath.musichouse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.codepath.musichouse.classicmusic.ClassicFragment;
import com.codepath.musichouse.popmusic.PopFragment;
import com.codepath.musichouse.rockmusic.RockFragment;

public class MainActivity extends AppCompatActivity {


   public static FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_rock:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, new RockFragment())
                            .addToBackStack(null)
                            .commit();


                    return true;
                case R.id.navigation_classic:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, new ClassicFragment())
                            .addToBackStack(null)
                            .commit();

                    return true;
                case R.id.navigation_pop:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, new PopFragment())
                            .addToBackStack(null)
                            .commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null){
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, new RockFragment())
                    .addToBackStack(null)
                    .commit();
        }else{

        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



}
