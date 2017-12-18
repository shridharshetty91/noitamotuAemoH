package com.magnaton.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.magnaton.homeautomation.AppComponents.Model.Constants.Log_TAG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Log_TAG, "MainActivity onCreate");

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main, new MainActivityFragment())
                .commit();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainActivityFragment fragment = new MainActivityFragment();
                fragment.test = true;
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_main, new MainActivityFragment())
                        .addToBackStack("")
                        .commit();
            }
        }, 7000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
