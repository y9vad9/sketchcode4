package com.sketch.code.two;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FilePickerActivity extends AppCompatActivity {
    static RecyclerView.Adapter mAdapter;
    static RecyclerView.LayoutManager layoutManager;
    static RecyclerView recyclerView;
    private Toolbar toolbar;
    public static String currfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        FilesManager filesManager = new FilesManager();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(FilePickerActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FilesAdapter(filesManager.getStandartFolder(), FilePickerActivity.this);
        recyclerView.setAdapter(mAdapter);
        init();
    }
    public void init() {
        toolbar = findViewById(R.id.toolbar);
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
    }
    static void changePath(String path, Activity activity) {
        FilesManager filesManager = new FilesManager();
        mAdapter = new FilesAdapter(filesManager.goTo(path), activity);
        recyclerView.setAdapter(mAdapter);
    }
}
class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mDataset;
    private Activity act;
    FilesManager filesManager;

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
    public FilesAdapter(ArrayList<HashMap<String, Object>> myDataset, Activity activity) {
        act = activity;
        mDataset = myDataset;
        filesManager = new FilesManager();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView name = holder.itemView.findViewById(R.id.name);
        ImageView icon = holder.itemView.findViewById(R.id.icon);
        if(mDataset.get(position).get("isFile").toString().equals("true")) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.file)
                    .error(R.drawable.file);

            File file = new File(mDataset.get(position).get("path").toString());

            Glide.with(act).load(Uri.fromFile(file)).apply(options).into(icon);
        } else {
            icon.setImageDrawable(act.getDrawable(R.drawable.folder));
        }
        name.setText(mDataset.get(position).get("name").toString());
        if(mDataset.get(position).get("isFile").equals("false")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilePickerActivity.changePath(mDataset.get(position).get("path").toString(), act);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDataset.get(position).get("path").toString().contains(".jpg") || mDataset.get(position).get("path").toString().contains(".jpeg") || mDataset.get(position).get("path").toString().contains(".png")) {
                        Intent i = new Intent();
                        i.putExtra("path", mDataset.get(position).get("path").toString());
                        act.setResult(1, i);
                        act.finish();
                    } else {
                        Toast.makeText(act, "Your file shoud be image (jpg,jpeg,png)", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
