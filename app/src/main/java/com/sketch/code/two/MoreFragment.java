package com.sketch.code.two;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;


public class MoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView icon_prof;
    private LinearLayout img_p;
    private LinearLayout openprof;
    private TextView nameacc;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        final AccountManager accountManager = new AccountManager(getActivity());

        // Setup of menu
        ArrayList<HashMap<String, Object>> map = new ArrayList<>();
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("title", getString(R.string.profile));
        obj.put("action", "profile");
        map.add(obj);
        obj = new HashMap<>();
        obj.put("title", getString(R.string.saved));
        obj.put("action", "saved");
        map.add(obj);
        HashMap<String, Object> obj2 = new HashMap<>();
        obj2.put("title", getString(R.string.settings));
        obj2.put("action", "settings");
        map.add(obj2);
        obj = new HashMap<>();
        obj.put("title", getString(R.string.rate));
        obj.put("action", "rate");
        map.add(obj);
        obj = new HashMap<>();
        obj.put("title", getString(R.string.help_more));
        obj.put("action", "help");
        map.add(obj);
        ArrayList<Drawable> drawables = new ArrayList<>();

        drawables.add(MainActivity.activity.getDrawable(R.drawable.round_account_circle_24));
        drawables.add(MainActivity.activity.getDrawable(R.drawable.saved));
        drawables.add(MainActivity.activity.getDrawable(R.drawable.settings));
        drawables.add(MainActivity.activity.getDrawable(R.drawable.star));
        drawables.add(MainActivity.activity.getDrawable(R.drawable.help));


        mAdapter = new MenuRAdapter(map, drawables);
        recyclerView.setAdapter(mAdapter);
        // LOGIC
        openprof = (LinearLayout) view.findViewById(R.id.openprof);
        openprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManager.openProfile();
            }
        });

        icon_prof = (ImageView)view.findViewById(R.id.icon_prof);
        if(accountManager.getSavedProfileIconUrl().equals("%default%")) {
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.prof_def)
                    .error(R.drawable.prof_def);



            Glide.with(getActivity()).load(Uri.parse(accountManager.getSavedProfileIconUrl())).apply(options).into(icon_prof);
        }
        nameacc = (TextView)view.findViewById(R.id.nameacc);
        if(accountManager.getSavedUsername().equals("")) {

        } else {
            nameacc.setText(accountManager.getSavedUsername());
        }
        return view;
    }


}
class MenuRAdapter extends RecyclerView.Adapter<MenuRAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mDataset;
    private ArrayList<Drawable> drawables;
    private Activity act;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ViewHolder(View view, TextView title) {
            super(view);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MenuRAdapter(ArrayList<HashMap<String, Object>> myDataset, ArrayList<Drawable> _drawables) {
        act = MainActivity.activity;
        mDataset = myDataset;
        drawables = _drawables;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MenuRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(ContextContainer.context)
                .inflate(R.layout.menu_layout, parent, false);
        TextView title = view.findViewById(R.id.title);
        ImageView icon = view.findViewById(R.id.icon);
        ViewHolder vh = new ViewHolder(view, title);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView title = holder.itemView.findViewById(R.id.title);
        ImageView icon = holder.itemView.findViewById(R.id.icon);
        title.setText(mDataset.get(position).get("title").toString());
        icon.setImageDrawable(drawables.get(position));
        LinearLayout root = holder.itemView.findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDataset.get(position).get("action").equals("profile")) {
                    AccountManager accountManager = new AccountManager(act);
                    accountManager.openProfile();
                } else if(mDataset.get(position).get("action").equals("help")) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(act);
                    View view = act.getLayoutInflater().inflate(R.layout.help_layout, null);
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();
                    LinearLayout root = (LinearLayout)view.findViewById(R.id.root);
                    MaterialButton viewme = (MaterialButton)view.findViewById(R.id.viewme);
                    MaterialButton join = (MaterialButton)view.findViewById(R.id.join);
                    viewme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://t.me/ytneonyt"));
                            act.startActivity(i);
                        }
                    });
                    join.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://t.me/joinchat/Hz1O0Uqv55JkpCF3X_YFog"));
                            act.startActivity(i);
                        }
                    });

                } else if(mDataset.get(position).get("action").equals("rate")) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.sketch.code.two"));
                    act.startActivity(i);
                } else if(mDataset.get(position).get("action").equals("settings")) {
                    Intent i = new Intent();
                    i.setClass(act, SettingsActivity.class);
                    act.startActivity(i);
                } else if(mDataset.get(position).get("action").equals("saved")) {
                    Intent i = new Intent();
                    i.setClass(act, UsersContentActivity.class);
                    i.putExtra("type", "saved");
                    i.putExtra("name", "Saved");
                    act.startActivity(i);
                } else if(mDataset.get(position).get("action").equals("sub")) {

                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
