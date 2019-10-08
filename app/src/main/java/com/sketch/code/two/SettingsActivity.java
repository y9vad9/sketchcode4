package com.sketch.code.two;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.froyo.codeview.Theme;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        initLogic();
    }
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        list = findViewById(R.id.list);

    }
    public void initLogic() {
        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SettingsActivity.this);
        list.setLayoutManager(layoutManager);
        SettingsManager settingsManager = new SettingsManager(SettingsActivity.this);
        settingsManager.add("App Theme", "Choose theme what comfortable for you", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(SettingsActivity.this);
                builderSingle.setTitle("Choose theme");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("Light");
                arrayAdapter.add("Dark");
                arrayAdapter.add("Blue");

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        Toast.makeText(SettingsActivity.this, "Will be added!", Toast.LENGTH_SHORT).show();
                    }
                });
                builderSingle.show();
            }
        });
        mAdapter = new SettingsAdapter(settingsManager.getSettings(), settingsManager.getSettingsListeners(), SettingsActivity.this);
        list.setAdapter(mAdapter);
    }
}
class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mDataset;
    private ArrayList<View.OnClickListener> clickListeners;
    private Activity act;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ViewHolder(View view, TextView title, TextView subtitle) {
            super(view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SettingsAdapter(ArrayList<HashMap<String, Object>> myDataset, ArrayList<View.OnClickListener> _onClickListeners, Activity activity) {
        act = activity;
        mDataset = myDataset;
        clickListeners = _onClickListeners;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settingslayout, parent, false);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        ViewHolder vh = new ViewHolder(view, title, subtitle);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout root = holder.itemView.findViewById(R.id.root);
        root.setOnClickListener(clickListeners.get(position));
        TextView title = holder.itemView.findViewById(R.id.title);
        TextView subtitle = holder.itemView.findViewById(R.id.subtitle);
        title.setText(mDataset.get(position).get("title").toString());
        subtitle.setText(mDataset.get(position).get("subtitle").toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
