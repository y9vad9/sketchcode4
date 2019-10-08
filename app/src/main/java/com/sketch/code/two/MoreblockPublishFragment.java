package com.sketch.code.two;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreblockPublishFragment extends Fragment {


    public MoreblockPublishFragment() {
        // Required empty public constructor
    }
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    MoreblockParser moreblockParser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moreblock_publish, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.list);
        moreblockParser = new MoreblockParser();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        moreblockParser.loadList(getActivity(), new MoreblockParser.LoadListeners() {
            @Override
            public void onResponse(String response) {
                try {
                    mAdapter = new MoreblockAdapter(moreblockParser.format(response));
                }catch (Exception e) {
                    Debug.writeResponse(moreblockParser.format(response).toString());
                    Debug.reportError(e);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
        return view;
    }

}
class MoreblockAdapter extends RecyclerView.Adapter<MoreblockAdapter.ViewHolder> {
    static ArrayList<HashMap<String, Object>> mDataset;
    static Activity act;


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
    public MoreblockAdapter(ArrayList<HashMap<String, Object>> myDataset) {
        act = MainActivity.activity;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoreblockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(ContextContainer.context)
                .inflate(R.layout.item_moreblock, parent, false);
        act = MainActivity.activity;
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView name = (TextView) holder.itemView.findViewById(R.id.name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

