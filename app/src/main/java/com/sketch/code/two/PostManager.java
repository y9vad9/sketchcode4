package com.sketch.code.two;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PostManager {
    PostManager.Posts posts;
    Activity activity;
    SharedPreferences data;
    SharedPreferences account;
    Comments comments;
    ArrayList<HashMap<String, Object>> _comments;
    SendListener sendListener;
    public PostManager (Activity _activity){
        activity = _activity;
        data = getActivity().getSharedPreferences("news", Context.MODE_PRIVATE);
        account = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
    }
    public Activity getActivity() {
        return activity;
    }
    public void loadFeed(Posts _posts) {
        posts = _posts;
        new RequestNetwork(activity).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/news/list-news.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                posts.onResponse(response);
                data.edit().putString("all", response).commit();
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                data.edit().putString("all", "[]").commit();
                posts.onError(message);
            }
        });
    }
    public ArrayList<HashMap<String, Object>> getPosts() {
        ArrayList<HashMap<String, Object>> map = new Gson().fromJson(data.getString("all", "[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        return map;
    }
        public ArrayList<HashMap<String, Object>> stringToJsonArray(String s)
        {
            ArrayList<HashMap<String, Object>> map = new Gson().fromJson(s, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            return map;
        }

        public ArrayList<HashMap<String, Object>> getComments() {
        ArrayList<HashMap<String, Object>> map = new Gson().fromJson(data.getString("comments", "[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        return map;
        }
    public interface Posts {
        void onResponse(String response);
        void onError(String message);
    }
    public interface Comments {
        void onResponse(String response);
        void onError(String message);
    }
    public void loadComments(Comments _comments, String id) {
        comments = _comments;
        new RequestNetwork(getActivity()).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/news/getCommentsToPost.php?id=" + id, "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                data.edit().putString("comments", response).apply();
                comments.onResponse(response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                comments.onError(message);
            }
        });
    }
    public void sendComment(String content, String postId, final SendListener _sendListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("postId", postId);
        map.put("comment", content);
        map.put("email", account.getString("email",""));
        map.put("password", account.getString("password",""));
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/news/addComment.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Success")) {
                    _sendListener.onResultSuccess(response);
                } else {
                    _sendListener.onResultError(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                _sendListener.onConnectionError(message);
            }
        });
    }
    public void like(String postId, final LikeListeners likeListeners) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", postId);
        map.put("email", account.getString("email",""));
        map.put("password", account.getString("password",""));
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/news/likepost.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Success")) {
                    likeListeners.onSuccess();
                } else {
                    likeListeners.onError(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                likeListeners.onConnectionError(message);
            }
        });
    }
    void removeComment(String id, final RemoveListener removeListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("email", account.getString("email",""));
        map.put("password", account.getString("password",""));
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/news/removeComment.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Success")) {
                    removeListener.OnRemoveSuccess(JsonUtil.getGsonDataKey(response, "responce"));
                } else {
                    removeListener.onRemoveError(JsonUtil.getGsonDataKey(response, "responce"));
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                removeListener.onRemoveError(message);
            }
        });
    }
    public boolean isAdmin() {
        if(account.getString("prem", "no").equals("yes")) {
            return true;
        } else {
            return false;
        }
    }
    public void unlike(String postId, final LikeListeners likeListeners) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", postId);
        map.put("email", account.getString("email",""));
        map.put("password", account.getString("password",""));
        RequestNetwork requestNetwork = new RequestNetwork(getActivity());
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/news/unlikepost.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Success")) {
                    likeListeners.onSuccess();
                } else {
                    likeListeners.onError(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                likeListeners.onConnectionError(message);
            }
        });
    }
    public interface SendListener {
        void onResultSuccess(String response);
        void onResultError(String response);
        void onConnectionError(String message);
    }
    public interface LikeListeners {
        void onSuccess();
        void onError(String message);
        void onConnectionError(String message);
    }
    public void updLiked(final UpdateListener updateListener) {
        loadFeed(new Posts() {
            @Override
            public void onResponse(String response) {
                data.edit().putString("all", response).apply();
                updateListener.onError(response);
            }

            @Override
            public void onError(String message) {
                updateListener.onError(message);
            }
        });
    }
    public interface UpdateListener {
        void onSuccess(String data);
        void onError(String message);
    }
    public interface RemoveListener {
        void OnRemoveSuccess(String message);
        void onRemoveError(String message);
    }
}
