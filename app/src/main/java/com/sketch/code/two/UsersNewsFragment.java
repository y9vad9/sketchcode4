package com.sketch.code.two;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.sketch.code.two.JsonUtil.getValue;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersNewsFragment extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    UsersContentManager usersContentManager;


    public UsersNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        usersContentManager = new UsersContentManager(getActivity());
        usersContentManager.getNews(new UsersContentManager.LoadListener() {
            @Override
            public void onLoaded(String data) {
                ArrayList<HashMap<String, Object>> map = JsonUtil.ArrayfromJson(data);
                Collections.reverse(map);
                mAdapter = new Home2Adapter(map);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onError(String message) {

            }
        });
        return view;
    }

}
class Home2Adapter extends RecyclerView.Adapter<Home2Adapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mDataset;
    private Activity act;
    UsersContentManager usersContentManager;
    public ArrayList<String> liked;
    AccountManager accountManager;
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
    public Home2Adapter(ArrayList<HashMap<String, Object>> myDataset) {
        act = MainActivity.activity;
        mDataset = myDataset;
        usersContentManager = new UsersContentManager(act);
        liked = new ArrayList<>();
        accountManager = new AccountManager(act);
        profilesManager = new ProfilesManager(act);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Home2Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(ContextContainer.context)
                .inflate(R.layout.item_usernews, parent, false);
        ViewHolder vh = new ViewHolder(view);
        act = MainActivity.activity;
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TextView username = (TextView)holder.itemView.findViewById(R.id.username);
        TextView news_content = (TextView)holder.itemView.findViewById(R.id.news_content);
        final ImageView news_like = (ImageView)holder.itemView.findViewById(R.id.news_like);
        TextView news_date = (TextView)holder.itemView.findViewById(R.id.news_date);
        final TextView news_likes = (TextView)holder.itemView.findViewById(R.id.news_likes);
        final ImageView icon = (ImageView) holder.itemView.findViewById(R.id.icon);
        final ImageView vericon = (ImageView) holder.itemView.findViewById(R.id.vericon);
        news_content.setText(mDataset.get(position).get("text").toString());
        news_date.setText(mDataset.get(position).get("date").toString());
        news_likes.setText(mDataset.get(position).get("likes").toString());
        profilesManager.getProfile(mDataset.get(position).get("userid").toString(), new ProfilesManager.Profile() {
            @Override
            public void onLoad(final String data) {
                username.setText(JsonUtil.getValue(data, "nickname"));
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.prof_def)
                        .error(R.drawable.prof_def);


                Glide.with(act).load(Uri.parse(JsonUtil.getValue(data, "avatar"))).apply(options).into(icon);
                if(JsonUtil.getValue(data, "isVerified").equals("yes")) {
                    vericon.setVisibility(View.VISIBLE);
                } else {
                    vericon.setVisibility(View.GONE);
                }
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setClass(act, PubProfActivity.class);
                        i.putExtra("name", getValue(data, "nickname"));
                        i.putExtra("email", getValue(data, "email"));
                        i.putExtra("avatar", getValue(data, "avatar"));
                        i.putExtra("id", mDataset.get(position).get("userid").toString());
                        act.startActivity(i);
                    }
                });
                username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setClass(act, PubProfActivity.class);
                        i.putExtra("name", getValue(data, "nickname"));
                        i.putExtra("email", getValue(data, "email"));
                        i.putExtra("avatar", getValue(data, "avatar"));
                        i.putExtra("id", mDataset.get(position).get("userid").toString());
                        act.startActivity(i);
                    }
                });

            }

            @Override
            public void onError(String data) {

            }
        });
        if(mDataset.get(position).get("liked").toString().contains(accountManager.data.getString("email", "mail@mail.net"))) {
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
                    usersContentManager.unlike(mDataset.get(position).get("id").toString(), new PostManager.LikeListeners() {
                        @Override
                        public void onSuccess() {
                            news_like.setColorFilter(Color.parseColor("#656565"));
                            news_likes.setTextColor(Color.parseColor("#656565"));
                            int lks = Integer.parseInt(news_likes.getText().toString());
                            lks = lks - 1;
                            news_likes.setText(Integer.toString(lks));
                            liked.set(position, "false");
                            usersContentManager.getNews(new UsersContentManager.LoadListener() {
                                @Override
                                public void onLoaded(String data) {
                                    ArrayList<HashMap<String, Object>> map = JsonUtil.ArrayfromJson(data);
                                    Collections.reverse(map);
                                    mDataset = map;
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
                    usersContentManager.like(mDataset.get(position).get("id").toString(), new UsersContentManager.LikeListener() {
                        @Override
                        public void onSuccess() {
                            news_like.setColorFilter(Color.parseColor("#0000FF"));
                            news_likes.setTextColor(Color.parseColor("#0000FF"));
                            int lks = Integer.parseInt(news_likes.getText().toString());
                            lks = lks + 1;
                            news_likes.setText(Integer.toString(lks));
                            liked.set(position, "true");
                            usersContentManager.getNews(new UsersContentManager.LoadListener() {
                                @Override
                                public void onLoaded(String data) {
                                    ArrayList<HashMap<String, Object>> map = JsonUtil.ArrayfromJson(data);
                                    Collections.reverse(map);
                                    mDataset = map;
                                }

                                @Override
                                public void onError(String message) {

                                }
                            });
                        }

                        @Override
                        public void onError(String message) {

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

