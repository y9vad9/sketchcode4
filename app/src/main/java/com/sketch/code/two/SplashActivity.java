package com.sketch.code.two;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SplashActivity extends AppCompatActivity {
    ProgressBar loadingBar;
    TextView phase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadingBar = findViewById(R.id.loadingbar);
        phase = findViewById(R.id.phase);
        loadPhase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        RequestNetwork requestNetwork = new RequestNetwork(this);
        final SharedPreferences preferences = getSharedPreferences("codes", MODE_PRIVATE);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/get-list-codes/", "Request", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                try {
                    loadingBar.setProgress(25);
                    ArrayList<HashMap<String, Object>> map = new Gson().fromJson(response, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                    Collections.reverse(map);
                    preferences.edit().putString("all", response).apply();
                } catch (Exception e) {
                    loadingBar.setProgress(25);
                    ArrayList<HashMap<String, Object>> map = new Gson().fromJson(preferences.getString("all", "null"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                    }.getType());
                }
                new RequestNetwork(SplashActivity.this).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/get-list-moreblocks/", "", new RequestNetwork.RequestListener() {
                    @Override
                    public void onResponse(String tag, String response) {
                        loadingBar.setProgress(50);
                        SharedPreferences data = getSharedPreferences("moreblocks", Activity.MODE_PRIVATE);
                        data.edit().putString("all", response).commit();
                        PostManager postManager = new PostManager(SplashActivity.this);
                        postManager.loadFeed(new PostManager.Posts() {
                            @Override
                            public void onResponse(String response) {
                                loadingBar.setProgress(75);
                                loadingBar.setProgress(100);
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            }

                            @Override
                            public void onError(String message) {
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(String tag, String message) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                ArrayList<HashMap<String, Object>> map = new Gson().fromJson(preferences.getString("all", "null"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
    void loadPhase() {
        Random rand = new Random();
        int x = rand.nextInt(5);
        if(x==1) {
            phase.setText("Developer sleeping... Please wait.");
        } else if(x==2){
            phase.setText("Loading is loading...");
        } else if(x==3){
            phase.setText("Wait a bit..");
        } else if(x==4) {
            phase.setText("Loading want load, pls wait..");
        } else {
            phase.setText("Its not will be so long..");
        }
    }
}
