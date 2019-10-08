package com.sketch.code.two;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreblockActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton add;
    TextView title;
    TextView about;
    MoreblockManager moreblockManager;
    ImageButton save;
    MenuItem item;
    LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreblock);
        AdManager adManager = new AdManager(this);
        adManager.show();
        moreblockManager = new MoreblockManager(this);
        findViews();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        init();


    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if(moreblockManager.isSaved(getIntent().getStringExtra("name"))) {
                    moreblockManager.remove(getIntent().getStringExtra("name"));
                    item.setIcon(R.drawable.round_turned_in_not_24);
                    save.setImageResource(R.drawable.round_turned_in_not_24);
                } else {
                    moreblockManager.save(getIntent().getStringExtra("name"), getIntent().getStringExtra("subtitle"), getIntent().getStringExtra("data"));
                    item.setIcon(R.drawable.saved);
                    save.setImageResource(R.drawable.saved);
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    void findViews() {
        toolbar = findViewById(R.id.toolbar);
        add = (ImageButton) findViewById(R.id.add);
        about = findViewById(R.id.about);
        title = findViewById(R.id.title);
        root = findViewById(R.id.root);
        save = findViewById(R.id.save);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
    }
    void init(){
        title.setText(getIntent().getStringExtra("name"));
        about.setText(getIntent().getStringExtra("subtitle"));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreblockManager.add(getIntent().getStringExtra("data"));
                Toast.makeText(getBaseContext(), "Added!", Toast.LENGTH_LONG).show();
            }
        });
        if(moreblockManager.isSaved(getIntent().getStringExtra("name"))) {
            save.setImageResource(R.drawable.saved);
        } else {
            moreblockManager.save(getIntent().getStringExtra("name"), getIntent().getStringExtra("subtitle"), getIntent().getStringExtra("data"));
            save.setImageResource(R.drawable.round_turned_in_not_24);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moreblockManager.isSaved(getIntent().getStringExtra("name"))) {
                    moreblockManager.remove(getIntent().getStringExtra("name"));
                    save.setImageResource(R.drawable.round_turned_in_not_24);
                } else {
                    moreblockManager.save(getIntent().getStringExtra("name"), getIntent().getStringExtra("subtitle"), getIntent().getStringExtra("data"));
                   save.setImageResource(R.drawable.saved);
                }
            }
        });
        Design.borderWithRound(root, 16, 0, 255,255, 255, "#777777");
        root.setElevation(5f);
    }
}
