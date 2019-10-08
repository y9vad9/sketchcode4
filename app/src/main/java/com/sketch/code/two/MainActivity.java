package com.sketch.code.two;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import static com.sketch.code.two.ContextContainer.*;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MainActivity extends AppCompatActivity {
    Typeface google_sans_bold;
    Toolbar toolbar;
    TextView logo;
    TabLayout tabs;
    FrameLayout fragment;
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    Fragment home_fragment;
    MenuItem search;
    MaterialSearchView searchView;
    static int tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = this;
        /*if(SecurityUtil.verifyInstallerId(activity, "com.sketch.code.two")) {
            SecurityUtil.verify = true;
        } else {
            SecurityUtil.verify = false;
            SecurityUtil.notVerifiedApp(activity);
        }*/
        try
        {
            Class.forName("com.sketch.code.two.SecurityUtil");
            Class.forName("com.sketch.code.two.AdManager");
        }
        catch( ClassNotFoundException e )
        {
            Toast.makeText(getApplicationContext(), "Do not patch this app :)", Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_main);
        findViewsById();
        initActivityBase();
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        initBottomNavigationBar();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
        }
    }
    void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        logo = findViewById(R.id.logo);
        searchView = findViewById(R.id.search_view);
        tabs = findViewById(R.id.tabs);
        fragment = findViewById(R.id.fragment);
        bottomNavigationView = findViewById(R.id.bottomnav);
    }
    void initActivityBase() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        google_sans_bold = ResourcesCompat.getFont(this, R.font.google_sans_bold);
        logo.setTypeface(google_sans_bold);

        home_fragment = new HomeFragment();
    }

    void initBottomNavigationBar() {
        bottomNavigationView.setSelectedItemId(R.id.bottomnav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragment.setVisibility(View.GONE);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch(menuItem.getItemId()) {
                    case R.id.bottomnav_home:
                            tabs.setVisibility(View.GONE);
                            tab = bottomNavigationView.getSelectedItemId();
                            fragmentTransaction.replace(R.id.fragment, home_fragment);
                        break;
                    case R.id.bottomnav_codes:
                        tab = bottomNavigationView.getSelectedItemId();
                        tabs.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.fragment, new CodesFragment());
                        break;
                    case R.id.bottomnav_moreblocks:
                        tab = bottomNavigationView.getSelectedItemId();
                        tabs.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.fragment, new MoreblocksFragment());
                        break;
                    case R.id.bottomnav_usercontent:
                        tab = bottomNavigationView.getSelectedItemId();
                        tabs.setVisibility(View.VISIBLE);
                        fragmentTransaction.replace(R.id.fragment, new UsersContentFragment());
                        break;
                    case R.id.bottomnav_more:
                        tab = bottomNavigationView.getSelectedItemId();
                        tabs.setVisibility(View.GONE);
                        fragmentTransaction.replace(R.id.fragment, new MoreFragment());
                        break;

                }
                fragmentTransaction.commit();
                fragment.setVisibility(View.VISIBLE);
                return true;
            }
        });
        bottomNavigationView.getMenu().performIdentifierAction(R.id.bottomnav_home, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                logo.setVisibility(logo.GONE);
                getSupportActionBar().setDisplayUseLogoEnabled(false);
            }

            @Override
            public void onSearchViewClosed() {
                logo.setVisibility(logo.VISIBLE);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
            }
        });
        return true;
    }

    public static MainActivity activity;

    public void setupTabs(ViewPager viewPager) {
        tabs.setupWithViewPager(viewPager);
    }
}
