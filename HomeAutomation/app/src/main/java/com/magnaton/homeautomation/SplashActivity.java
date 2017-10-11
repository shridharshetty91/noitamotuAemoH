package com.magnaton.homeautomation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.magnaton.homeautomation.Dashboard.DashboardActivity;

import static com.magnaton.homeautomation.Constants.SharedPreferencesTag;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent;
                if (BuildConfig.DEBUG) {
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent = new Intent(SplashActivity.this, DashboardActivity.class);
                } else {
                    SharedPreferences preferences = SplashActivity.this.getSharedPreferences(SharedPreferencesTag, Context.MODE_PRIVATE);
                    String token = preferences.getString("Token", "");
                    if (TextUtils.isEmpty(token)) {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        }, 700);
    }
}
