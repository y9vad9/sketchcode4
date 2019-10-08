package com.sketch.code.two;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    ListView listView;
    RequestNetwork requestNetwork;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    PostManager postManager;
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNetwork = new RequestNetwork(getActivity());
        postManager = new PostManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<HashMap<String, Object>> map = postManager.getPosts();
        Collections.reverse(map);
        mAdapter = new HomeAdapter(map);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden) listView.setVisibility(View.GONE);
        else       listView.setVisibility(View.VISIBLE);
    }
}
class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    static ArrayList<HashMap<String, Object>> mDataset;
    static Activity act;
    PostManager postManager;
    public ArrayList<String> liked;

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
    public HomeAdapter(ArrayList<HashMap<String, Object>> myDataset) {
        act = MainActivity.activity;
        mDataset = myDataset;
        postManager = new PostManager(act);
        liked = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(ContextContainer.context)
                .inflate(R.layout.item_news, parent, false);
        ViewHolder vh = new ViewHolder(view);
        act = MainActivity.activity;
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView title = (TextView)holder.itemView.findViewById(R.id.news_title);
        TextView news_content = (TextView)holder.itemView.findViewById(R.id.news_content);
        final ImageView news_like = (ImageView)holder.itemView.findViewById(R.id.news_like);
        TextView news_date = (TextView)holder.itemView.findViewById(R.id.news_date);
        final TextView news_likes = (TextView)holder.itemView.findViewById(R.id.news_likes);
        ImageView news_comments = (ImageView)holder.itemView.findViewById(R.id.news_comments);
        title.setText(mDataset.get(position).get("title").toString());
        news_content.setText(Html.fromHtml(mDataset.get(position).get("text").toString().replaceAll("\\n","<br>")));
        news_date.setText(mDataset.get(position).get("date").toString());
        news_likes.setText(mDataset.get(position).get("likes").toString());
        news_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(ContextContainer.context, CommentsActivity.class);
                i.putExtra("id", mDataset.get(position).get("id").toString());
                act.startActivity(i);
            }
        });
        if(mDataset.get(position).get("liked").toString().contains(postManager.account.getString("email", "mail@mail.net"))) {
            news_like.setColorFilter(Color.parseColor("#0000FF"));
            news_likes.setTextColor(Color.parseColor("#0000FF"));
            liked.add("true");
        } else {
            news_like.setColorFilter(Color.parseColor("#656565"));
            news_likes.setTextColor(Color.parseColor("#656565"));
            liked.add("false");
        }
        news_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked.get(position).equals("true")) {
                    postManager.unlike(mDataset.get(position).get("id").toString(), new PostManager.LikeListeners() {
                        @Override
                        public void onSuccess() {
                            news_like.setColorFilter(Color.parseColor("#656565"));
                            news_likes.setTextColor(Color.parseColor("#656565"));
                            int lks = Integer.parseInt(news_likes.getText().toString());
                            lks = lks - 1;
                            news_likes.setText(Integer.toString(lks));
                            liked.set(position, "false");
                            postManager.updLiked(new PostManager.UpdateListener() {
                                @Override
                                public void onSuccess(String data) {
                                    mDataset = JsonUtil.ArrayfromJson(data);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            });
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(act, message, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onConnectionError(String message) {
                            Toast.makeText(act, message, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    postManager.like(mDataset.get(position).get("id").toString(), new PostManager.LikeListeners() {
                        @Override
                        public void onSuccess() {
                            news_like.setColorFilter(Color.parseColor("#0000FF"));
                            news_likes.setTextColor(Color.parseColor("#0000FF"));
                            int lks = Integer.parseInt(news_likes.getText().toString());
                            lks = lks + 1;
                            news_likes.setText(Integer.toString(lks));
                            liked.set(position, "true");
                            postManager.updLiked(new PostManager.UpdateListener() {
                                @Override
                                public void onSuccess(String data) {
                                    mDataset = JsonUtil.ArrayfromJson(data);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            });
                        }

                        @Override
                        public void onError(String message) {
                            news_like.setColorFilter(Color.parseColor("#656565"));
                            news_likes.setTextColor(Color.parseColor("#656565"));
                            Toast.makeText(act, message, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onConnectionError(String message) {
                            news_like.setColorFilter(Color.parseColor("#656565"));
                            news_likes.setTextColor(Color.parseColor("#656565"));
                            Toast.makeText(act, message, Toast.LENGTH_LONG).show();
                        }
                    });
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
