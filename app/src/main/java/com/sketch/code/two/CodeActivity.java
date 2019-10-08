package com.sketch.code.two;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.sketch.code.two.CodesManager.*;

import com.froyo.codeview.CodeView;
import com.froyo.codeview.Language;
import com.froyo.codeview.Theme;

public class CodeActivity extends AppCompatActivity {
    public static Intent intent;
    Typeface google_sans_bold;
    Toolbar toolbar;
    TabLayout tabs;
    ViewPager viewPager;
    FloatingActionButton fab;
    String name;
    String code;
    String help;
    CodesManager codesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        AdManager adManager = new AdManager(this);
        adManager.show();
        intent = getIntent();
        codesManager = new CodesManager(this);
        findViewsById();
        initActivityBase();
        name = getIntent().getStringExtra("name");
        code = getIntent().getStringExtra("code");
        help = getIntent().getStringExtra("help");
    }

    void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.list);
        fab = findViewById(R.id.copy);
    }

    void initActivityBase() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch(i) {
                    case 0:
                        fab.show();
                        break;
                    case 1:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabs.setupWithViewPager(viewPager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) CodeActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("sketchcode", intent.getStringExtra("code"));
                clipboard.setPrimaryClip(clip);
                showMessage(getString(R.string.copied));
            }
        });
    }
    public void showMessage(String s){
        CoordinatorLayout coord = findViewById(R.id.coord);
        Snackbar.make(coord, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_code, menu);
        if(codesManager.isSaved(name)) {
            menu.getItem(1).setIcon(R.drawable.saved);
        } else {
            menu.getItem(1).setIcon(R.drawable.round_turned_in_not_24);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if(codesManager.isSaved(name)) {
                    showMessage("Removed");
                    codesManager.removeSavedCode(name);
                    item.setIcon(R.drawable.round_turned_in_not_24);
                } else {
                    codesManager.save(name, code, help);
                    item.setIcon(R.drawable.saved);
                    showMessage("Saved");
                }
                break;
            case R.id.share:
                Intent i = new Intent();
                i.setType("text/plain");
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("code").concat("\nSended from - https://play.google.com/store/apps/details?id=com.sketch.code.two"));
                startActivity(Intent.createChooser(i, "Share using"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new CodeViewFragment();
                case 1:
                    return new CodeHelpFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.code).toUpperCase();
                case 1:
                    return getString(R.string.help).toUpperCase();
                default:
                    return null;
            }
        }
    }

    public static class CodeViewFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            container.setBackgroundColor(Color.parseColor("#FAFAFA"));
            container.setPadding(8, 8, 8, 8);
            CodeView codeView = new CodeView(getActivity());
            codeView.setBackgroundColor(Color.parseColor("#FAFAFA"));
            codeView.setTheme(Theme.ATOM_ONE_LIGHT);
            codeView.setLanguage(Language.JAVA);
            codeView.setWrapLine(true);
            //TODO bad solution
            codeView.setCode("\n" + intent.getStringExtra("code") + "\n");
            codeView.setShowLineNumber(false);
            codeView.setZoomEnabled(false);
            codeView.setFontSize(12);
            codeView.setOnHighlightListener(new CodeView.OnHighlightListener() {
                @Override
                public void onStartCodeHighlight() {

                }

                @Override
                public void onFinishCodeHighlight() {

                }

                @Override
                public void onLanguageDetected(Language language, int i) {

                }

                @Override
                public void onFontSizeChanged(int i) {

                }

                @Override
                public void onLineClicked(int i, String s) {

                }
            });
            codeView.apply();
            return codeView;
        }
    }

    public static class CodeHelpFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_code_help, container, false);
            TextView textView = view.findViewById(R.id.helptv);
            textView.setText(intent.getStringExtra("help"));
            return view;
        }
    }
}
