package com.sketch.code.two;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        Intent i = new Intent();
        i.setClass(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
