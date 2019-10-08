package com.sketch.code.two;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sketch.code.two.JsonUtil.getValue;

public class CommentsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    PostManager postManager;
    ImageView send;
    TextView comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        postManager = new PostManager(CommentsActivity.this);
        initViews();
        init();
    }
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        list = findViewById(R.id.list);
        send = findViewById(R.id.send);
        list.setHasFixedSize(true);
        comment = findViewById(R.id.comment);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        list.setLayoutManager(layoutManager);
        TransparentProgressDialog.showLoading(CommentsActivity.this);
        postManager.loadComments(new PostManager.Comments() {
            @Override
            public void onResponse(String response) {
                mAdapter = new CommentsAdapter(postManager.getComments(),CommentsActivity.this);
                list.setAdapter(mAdapter);
                list.smoothScrollToPosition(postManager.getComments().size());
                TransparentProgressDialog.hideLoading(CommentsActivity.this);
            }

            @Override
            public void onError(String message) {
                TransparentProgressDialog.hideLoading(CommentsActivity.this);
            }
        }, getIntent().getStringExtra("id"));

    }
    public void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().trim().equals("")){
                    showMessage("Comment cannot be empty");
                } else {
                    postManager.sendComment(comment.getText().toString(), getIntent().getStringExtra("id"), new PostManager.SendListener() {
                        @Override
                        public void onResultSuccess(String response) {
                            postManager.loadComments(new PostManager.Comments() {
                                @Override
                                public void onResponse(String response) {
                                    mAdapter = new CommentsAdapter(postManager.getComments(), CommentsActivity.this);
                                    list.setAdapter(mAdapter);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            }, getIntent().getStringExtra("id"));
                            if(!response.equals("Success")) {
                                showMessage(response);
                            }

                        }

                        @Override
                        public void onResultError(String response) {
                            postManager.loadComments(new PostManager.Comments() {
                                @Override
                                public void onResponse(String response) {
                                    mAdapter = new CommentsAdapter(postManager.getComments(), CommentsActivity.this);
                                    list.setAdapter(mAdapter);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            }, getIntent().getStringExtra("id"));
                            showMessage(response);
                        }

                        @Override
                        public void onConnectionError(String message) {
                            showMessage(message);
                        }
                    });
                }
                comment.setText("");
            }
        });
    }
    public void showMessage(String s) {
        CoordinatorLayout coord = findViewById(R.id.coord);
        Snackbar.make(coord, s, Snackbar.LENGTH_SHORT).show();
    }
}
class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mDataset;
    private Activity act;
    PostManager postManager;
    ProfilesManager profilesManager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ViewHolder(View view) {
            super(view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentsAdapter(ArrayList<HashMap<String, Object>> myDataset, Activity activity) {
        act = activity;
        mDataset = myDataset;
        postManager = new PostManager(activity);
        profilesManager = new ProfilesManager(activity);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_layoyt, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView name = (TextView) holder.itemView.findViewById(R.id.name);
        TextView message = (TextView) holder.itemView.findViewById(R.id.message);
        ImageView icon = (ImageView) holder.itemView.findViewById(R.id.icon);
        ImageView vericon = (ImageView)holder.itemView.findViewById(R.id.vericon);
        if(mDataset.get(position).get("isVerified").toString().equals("yes")) {
            vericon.setVisibility(View.VISIBLE);
        } else {
            vericon.setVisibility(View.GONE);
        }
        if(!mDataset.get(position).get("avatar").toString().equals("%default%")) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.prof_def)
                    .error(R.drawable.prof_def);


            Glide.with(act).load(Uri.parse(mDataset.get(position).get("avatar").toString())).apply(options).into(icon);
        } else {
            icon.setImageDrawable(act.getDrawable(R.drawable.prof_def));
        }
        name.setText(mDataset.get(position).get("name").toString());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent i = new Intent();
                i.setClass(act, PubProfActivity.class);
                i.putExtra("name", mDataset.get(position).get("name").toString());
                i.putExtra("email", mDataset.get(position).get("email").toString());
                i.putExtra("avatar", mDataset.get(position).get("avatar").toString());
                profilesManager.getIdFromMail(mDataset.get(position).get("email").toString(), new ProfilesManager.Profile() {
                    @Override
                    public void onLoad(String data) {
                        i.putExtra("id", data);
                        act.startActivity(i);
                    }

                    @Override
                    public void onError(String data) {

                    }
                });

            }
        });
        message.setText(mDataset.get(position).get("text").toString());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dlgsettings = new AlertDialog.Builder(act);
                dlgsettings.setTitle("Comment settings");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(act, android.R.layout.simple_list_item_1);
                arrayAdapter.add("Copy");
                if(mDataset.get(position).get("email").toString().equals(postManager.account.getString("email", "")) || postManager.isAdmin()) {
                    arrayAdapter.add("Remove");
                }


                dlgsettings.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if(strName.equals("Copy")) {
                            ClipboardManager clipboard = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("sketchcode", mDataset.get(position).get("text").toString());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(act, "Copied", Toast.LENGTH_SHORT).show();
                        } else if(strName.equals("Remove")) {
                            postManager.removeComment(mDataset.get(position).get("id").toString(), new PostManager.RemoveListener() {
                                @Override
                                public void OnRemoveSuccess(String message) {
                                    Toast.makeText(act, "Removed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onRemoveError(String message) {
                                    Toast.makeText(act, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                dlgsettings.show();
                return false;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
