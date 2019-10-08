package com.sketch.code.two;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PubProfActivity extends AppCompatActivity {
    TextView name;
    CoordinatorLayout coord;
    ImageView icon_profile;
    Toolbar toolbar;
    TabLayout tab;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_prof);
        init();
    }
    public void init() {
        name = findViewById(R.id.name);
        coord = findViewById(R.id.coord);
        toolbar = findViewById(R.id.toolbar);
        tab = findViewById(R.id.tabs);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        icon_profile = findViewById(R.id.icon_profile);
        name.setText(getIntent().getStringExtra("name"));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.prof_def)
                .error(R.drawable.prof_def);



        Glide.with(PubProfActivity.this).load(Uri.parse(getIntent().getStringExtra("avatar"))).apply(options).into(icon_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        inittab();
    }
    public void inittab() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new PubProfActivity.ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tab.setupWithViewPager(viewPager);
    }
    public void showMessage(String s){
        Snackbar.make(coord, s, Snackbar.LENGTH_SHORT).show();
    }
private class ViewPagerAdapter extends FragmentPagerAdapter
{

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return new UserInfoFragment();
            case 1: return new MyPostsFragment();
            case 2: return new MyCodesFragment();
            case 3: return new Fragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "Info".toUpperCase();
            case 1: return "Posts".toUpperCase();
            case 2: return "Codes".toUpperCase();
            case 3: return "Moreblocks".toUpperCase();
            default: return null;
        }
    }
}
}
