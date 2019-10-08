package com.sketch.code.two;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;


public class UsersContentActivity extends AppCompatActivity {

    static TabLayout tabs;
    FrameLayout frame;
    FragmentTransaction fragmentTransaction;
    static Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_content);
        tabs = findViewById(R.id.tabs);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        i = new Intent();
        frame = findViewById(R.id.frame);
        frame.setVisibility(View.GONE);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(getIntent().getStringExtra("type").equals("code"))
        {
            fragmentTransaction.replace(R.id.frame, new UsersCodeFragment());
            tabs.setVisibility(View.GONE);
        } else if(getIntent().getStringExtra("type").equals("moreblock")) {
            tabs.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.frame, new Fragment());
        } else if(getIntent().getStringExtra("type").equals("saved")) {
            fragmentTransaction.replace(R.id.frame, new GlobalSavedFragment());
            tabs.setVisibility(View.VISIBLE);
        } else if(getIntent().getStringExtra("type").equals("mycodes")) {
            fragmentTransaction.replace(R.id.frame, new MyCodesFragment());
            tabs.setVisibility(View.GONE);
        } else if(getIntent().getStringExtra("type").equals("account")) {
            fragmentTransaction.replace(R.id.frame, new MyAccountFragment());
            tabs.setVisibility(View.GONE);
        } else if(getIntent().getStringExtra("type").equals("myposts")) {
            fragmentTransaction.replace(R.id.frame, new MyPostsFragment());
            tabs.setVisibility(View.GONE);
        } else if(getIntent().getStringExtra("type").equals("newmoreblock")) {
            fragmentTransaction.replace(R.id.frame, new MoreblockPublishFragment());
            tabs.setVisibility(View.GONE);
        }
        fragmentTransaction.commit();
        frame.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
    }
    public static void setupTabs(ViewPager viewPager) {
        tabs.setupWithViewPager(viewPager);
    }
}
