package com.sketch.code.two;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView name;
    ImageView icon_profile;
    AccountManager accountManager;
    FloatingActionButton fab;
    String url;
    LoadingDialog loadingDialog;
    static CoordinatorLayout coord;
    UsersContentManager usersContentManager;
    CardView cardView1;
    CardView cardView2;
    CardView cardView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        usersContentManager = new UsersContentManager(this);
        findViewsById();
        initActivityBase();
        initMenu();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(resultCode==1000) {
            if(requestCode==3000) {

                accountManager.changePassword(data.getStringExtra("value"), new AccountManager.UpdateListener() {
                    @Override
                    public void onDataUpdated() {
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            } else if(requestCode==4000) {
                accountManager.changeName(data.getStringExtra("value"), new AccountManager.UpdateListener() {
                    @Override
                    public void onDataUpdated() {
                        initAccount();
                    }

                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }
                });

            } else if(requestCode==12000) {
                try {
                    accountManager.setStatus(data.getStringExtra("value"), new AccountManager.UpdateListener() {
                        @Override
                        public void onDataUpdated() {
                            showMessage("Success");
                        }

                        @Override
                        public void onError(String message) {
                            showMessage(message);
                        }
                    });
                }catch (Exception e) {

                }
            }
        } else if(requestCode==6000) {
            TransparentProgressDialog.showLoading(ProfileActivity.this);
            try {

                url = accountManager.uploadFile(data.getStringExtra("path"));
                url = "https://neonteam.net/sketchcode/res".concat(url);
                if (url.contains("error") || url.contains("rror")) {
                    showMessage("An error has occurred. Try selecting images from a different location.");
                    loadingDialog.dismiss();
                } else {
                    accountManager.changeProfileIcon(url, new AccountManager.ProfileIconListener() {
                        @Override
                        public void onLoaded(String url, String message) {
                            showMessage("Successful!");
                            TransparentProgressDialog.hideLoading(ProfileActivity.this);
                            initAccount();
                        }

                        @Override
                        public void onError(String message, String url) {
                            showMessage(message);
                            TransparentProgressDialog.hideLoading(ProfileActivity.this);
                        }
                    });
                }
            }catch (Exception e) {
                Debug.reportError(e);
                loadingDialog.dismiss();
                try {
                    Debug.writeResponse(data.getStringExtra("path"));
                }catch (Exception e2) {

                }
            }
        }
    }

    void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        icon_profile = findViewById(R.id.icon_profile);
        fab = findViewById(R.id.floatingActionButton);
        coord = findViewById(R.id.coord);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgsettings = new AlertDialog.Builder(ProfileActivity.this);
                dlgsettings.setTitle("Account settings");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("New post");
                arrayAdapter.add("New code");
                //arrayAdapter.add("New moreblock");
                arrayAdapter.add("Set status");
                arrayAdapter.add("Change profile icon");
                arrayAdapter.add("Log out");


                dlgsettings.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if(strName.equals("Change name")) {
                            Intent i = new Intent();
                            i.setClass(ProfileActivity.this, EditTextActivity.class);
                            i.putExtra("title", "Change name");
                            startActivityForResult(i, 4000);
                        } else if(strName.equals("Change password")) {
                            Intent i = new Intent();
                            i.setClass(ProfileActivity.this, EditTextActivity.class);
                            i.putExtra("title", "Change password");
                            startActivityForResult(i, 3000);
                        } else if(strName.equals("Log out")) {
                            accountManager.logout();
                            finish();
                        } else if(strName.equals("Change profile icon")) changeIcon(); else if(strName.equals("New post"))
                            newPost();
                        else if(strName.equals("New code")) newcode(); else if(strName.equals("New moreblock")) newMoreblock(); else if(strName.equals("Set status")) {
                            Intent i = new Intent();
                            i.setClass(ProfileActivity.this, EditTextActivity.class);
                            i.putExtra("title", "Set new status");
                            startActivityForResult(i, 12000);
                        } else if(strName.equals("New moreblock")) newMoreblock();
                    }
                });
                dlgsettings.show();
            }
        });
        initAccount();
    }
    public void newMoreblock() {
        Intent i = new Intent();
        i.setClass(getApplicationContext(), UsersContentActivity.class);
        i.putExtra("type", "newmoreblock");
        i.putExtra("name", "New Moreblock");
        startActivity(i);
    }
    public void newcode() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.uploadcode_layout, null);
        bottomSheetDialog.setContentView(view);
        final TextInputEditText name = (TextInputEditText)view.findViewById(R.id.name);
        final TextInputEditText subtitle = (TextInputEditText)view.findViewById(R.id.subtitle);
        final TextInputEditText code = (TextInputEditText)view.findViewById(R.id.code);
        final Button upload = (Button) view.findViewById(R.id.upload);
        MaterialButton cancel = (MaterialButton) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setClickable(false);
                bottomSheetDialog.hide();
                UsersContentManager usersContentManager = new UsersContentManager(ProfileActivity.this);
                usersContentManager.uploadCode(new AccountManager(ProfileActivity.this), name.getText().toString(), code.getText().toString(), subtitle.getText().toString(), new UsersContentManager.UploadListener() {
                    @Override
                    public void onSuccess() {
                        showMessage("Uploaded!");
                    }

                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }

                    @Override
                    public void onConnError(String message) {
                        showMessage(message);
                    }
                });

                AdManager adManager = new AdManager(ProfileActivity.this);
                adManager.show();
            }
        });
        bottomSheetDialog.show();
    }
    public void newPost() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.newpost_layout, null);
        bottomSheetDialog.setContentView(view);
        final TextInputEditText message = (TextInputEditText)view.findViewById(R.id.message);
        final Button upload = (Button) view.findViewById(R.id.upload);
        MaterialButton cancel = (MaterialButton) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setClickable(false);
                bottomSheetDialog.hide();
                usersContentManager.uploadPost(accountManager, message.getText().toString(), new UsersContentManager.UploadListener() {
                    @Override
                    public void onSuccess() {
                        showMessage("Success!");
                    }

                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }

                    @Override
                    public void onConnError(String message) {
                        showMessage(message);
                    }
                });

                AdManager adManager = new AdManager(ProfileActivity.this);
                adManager.show();
            }
        });
        bottomSheetDialog.show();
    }
    public void changeIcon() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setClass(ProfileActivity.this, FilePickerActivity.class);
        startActivityForResult(intent, 6000);
    }
    public void initMenu() {
        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        cardView3 = findViewById(R.id.cardview3);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), UsersContentActivity.class);
                i.putExtra("type", "account");
                i.putExtra("name", "Account Settings");
                startActivity(i);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent();
                i.setClass(getApplicationContext(), UsersContentActivity.class);

                accountManager.getAccountData(new AccountManager.AccountData() {
                    @Override
                    public void onDataReceived(HashMap<String, Object> _data) {
                        i.putExtra("type", "mycodes");
                        i.putExtra("name", "My Codes");
                        i.putExtra("id", _data.get("id").toString());
                        startActivity(i);
                    }

                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }
                });

            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent();
                i.setClass(getApplicationContext(), UsersContentActivity.class);

                accountManager.getAccountData(new AccountManager.AccountData() {
                    @Override
                    public void onDataReceived(HashMap<String, Object> _data) {
                        i.putExtra("type", "myposts");
                        i.putExtra("name", "My Posts");
                        i.putExtra("id", _data.get("id").toString());
                        startActivity(i);
                    }

                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }
                });
            }
        });
    }
    public void initAccount() {
        accountManager = new AccountManager(this);
        if(accountManager.isAuthorized()) {
            accountManager.getAccountData(new AccountManager.AccountData() {
                @Override
                public void onDataReceived(final HashMap<String, Object> _data) {
                    name.setText(_data.get("nickname").toString());
                    AccountManager.data.edit().putString("icon_profile", _data.get("avatar").toString()).commit();
                    AccountManager.data.edit().putString("username", _data.get("nickname").toString()).commit();
                    AccountManager.data.edit().putString("prem", _data.get("prem").toString()).commit();
                    if(_data.get("avatar").equals("%default%") || _data.get("avatar").equals("")) {
                    } else {
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.prof_def)
                                .error(R.drawable.prof_def);



                        Glide.with(ProfileActivity.this).load(Uri.parse(_data.get("avatar").toString())).apply(options).into(icon_profile);
                    }
                    if(_data.get("email_confirmed").toString().equals("no")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                        alert.setTitle("Your account is limited!");
                        alert.setMessage("An email has been sent to your email to confirm your account. Do not forget to check the spam folder.");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.setNeutralButton("Resend", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accountManager.resend(_data.get("email").toString(), AccountManager.data.getString("password", ""));
                            }
                        });
                        alert.show();
                    }
                }

                @Override
                public void onError(String message) {
                    AccountManager.data.edit().clear().apply();
                    finish();
                }
            });
        } else {
            Intent i = new Intent();
            i.setClass(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
    public static void showMessage(String s){
        Snackbar.make(coord, s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
