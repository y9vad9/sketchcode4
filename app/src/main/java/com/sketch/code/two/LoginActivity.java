package com.sketch.code.two;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button loginbutton;
    TextView registerbutton;
    AppCompatEditText login, password;
    AccountManager accountManager;
    CoordinatorLayout coord;
    LoadingDialog loadingDialog;
    AppCompatEditText username;
    TextView textview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountManager = new AccountManager(this);
        findViewsById();
        initActivityBase();
    }

    void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        loginbutton = findViewById(R.id.loginbutton);
        registerbutton = findViewById(R.id.registerbutton);
        login = findViewById(R.id.login);
        coord = findViewById(R.id.coord);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        textview2 = findViewById(R.id.textView2);
    }

    void initActivityBase() {
        setSupportActionBar(toolbar);
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
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog = new LoadingDialog(LoginActivity.this);
                loadingDialog.show();
                if(loginbutton.getText().toString().equals("SIGN IN")) {
                    accountManager.login(login.getText().toString(), password.getText().toString(), new AccountManager.Account() {
                        @Override
                        public void onSuccess(int resultCode, String response) {
                            Intent i = new Intent();
                            i.setClass(LoginActivity.this, ProfileActivity.class);
                            startActivity(i);
                            finish();
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(int resultCode, String response) {
                            loadingDialog.dismiss();
                            showMessage(response);
                        }

                        @Override
                        public void onConnectionError(String message) {
                            showMessage(message);
                            loadingDialog.dismiss();
                        }
                    });
                } else if(loginbutton.getText().toString().equals("SIGN UP")){
                    accountManager.register(login.getText().toString(), password.getText().toString(), username.getText().toString(), new AccountManager.Account() {
                        @Override
                        public void onSuccess(int resultCode, String response) {
                            showMessage("Success");
                            Intent i = new Intent();
                            i.setClass(LoginActivity.this, ProfileActivity.class);
                            startActivity(i);
                            finish();
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onError(int resultCode, String response) {
                            showMessage(response);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onConnectionError(String message) {
                            showMessage(message);
                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        });
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(registerbutton.getText().toString().equals("Create one!")) {
                    registerbutton.setText("Sign in!");
                    textview2.setText("Already has account? ");
                    username.setVisibility(View.VISIBLE);
                    loginbutton.setText("SIGN UP");
                } else {
                    registerbutton.setText("Create one!");
                    textview2.setText("Don't have an account? ");
                    username.setVisibility(View.GONE);
                    loginbutton.setText("SIGN IN");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void showMessage(String s){
        Snackbar.make(coord, s, Snackbar.LENGTH_SHORT).show();
    }
}
