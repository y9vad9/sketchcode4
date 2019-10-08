package com.sketch.code.two;

import android.app.Activity;

import java.util.HashMap;

import static com.sketch.code.two.AccountManager.data;

public class UsersContentManager {
    static Activity activity;
    public UsersContentManager(Activity _activity) {
        activity = _activity;
    }
    public void loadCodes(final LoadListeners loadListeners) {
        new RequestNetwork(activity).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/get-list-codes.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                loadListeners.onLoaded(response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                loadListeners.onError(message);
            }
        });
    }
    public interface LoadListeners {
        void onLoaded(String data);
        void onError(String message);
    }
    public void loadMoreblocks(final LoadListeners loadListeners) {
        new RequestNetwork(activity).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/get-list-moreblocks.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                loadListeners.onLoaded(response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                loadListeners.onError(message);
            }
        });
    }
    void uploadCode(AccountManager accountManager, String name, String code, String subtitle, final UploadListener uploadlistener) {
        if(name.trim().equals("") || code.trim().equals("") || subtitle.trim().equals("")) {
            uploadlistener.onError("Info cannot be empty!");
        } else {
            RequestNetwork requestNetwork = new RequestNetwork(activity);
            HashMap<String, Object> map = new HashMap<>();
            map.put("email", accountManager.data.getString("email", ""));
            map.put("password", accountManager.data.getString("password", ""));
            map.put("name", name);
            map.put("code", code);
            map.put("subtitle", subtitle);
            requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
            requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/upload-code.php", "", new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response) {
                    if (JsonUtil.getValue(response, "responce").equals("Success"))
                        uploadlistener.onSuccess();
                    else
                        uploadlistener.onError(JsonUtil.getValue(response, "responce"));
                }

                @Override
                public void onErrorResponse(String tag, String message) {
                    uploadlistener.onConnError(message);
                }
            });
        }
    }
    void uploadPost(AccountManager accountManager, String text, final UploadListener uploadlistener) {
        if(text.trim().equals("")) {
            uploadlistener.onError("Message cannot be empty");
        } else {
            RequestNetwork requestNetwork = new RequestNetwork(activity);
            HashMap<String, Object> map = new HashMap<>();
            map.put("email", accountManager.data.getString("email", ""));
            map.put("password", accountManager.data.getString("password", ""));
            map.put("text", text);
            requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
            requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/upload-post.php", "", new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response) {
                    if (JsonUtil.getValue(response, "responce").equals("Success"))
                        uploadlistener.onSuccess();
                    else
                        uploadlistener.onError(JsonUtil.getValue(response, "responce"));
                }

                @Override
                public void onErrorResponse(String tag, String message) {
                    uploadlistener.onConnError(message);
                }
            });
        }
    }
    public void getNews(final LoadListener loadListener) {
        new RequestNetwork(activity).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/get-list-news.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                loadListener.onLoaded(response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                loadListener.onError(message);
            }
        });
    }
    interface UploadListener {
        void onSuccess();
        void onError(String message);
        void onConnError(String message);
    }
    interface LoadListener {
        void onLoaded(String data);
        void onError(String message);
    }
    interface LikeListener {
        void onSuccess();
        void onError(String message);
    }
        AccountManager accountManager;
        void like(String postId, final LikeListener likeListener) {
            accountManager = new AccountManager(UsersContentManager.activity);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", postId);
            map.put("email", accountManager.data.getString("email",""));
            map.put("password", data.getString("password",""));
            RequestNetwork requestNetwork = new RequestNetwork(UsersContentManager.activity);
            requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
            requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/likepost.php", "", new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response) {
                    if(response.equals("Success")) {
                        likeListener.onSuccess();
                    } else {
                        likeListener.onError(response);
                    }
                }

                @Override
                public void onErrorResponse(String tag, String message) {
                    likeListener.onError(message);
                }
            });
        }
        void unlike(String postId, final PostManager.LikeListeners likeListeners) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", postId);
            map.put("email", accountManager.data.getString("email",""));
            map.put("password", accountManager.data.getString("password",""));
            RequestNetwork requestNetwork = new RequestNetwork(UsersContentManager.activity);
            requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
            requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/unlikepost.php", "", new RequestNetwork.RequestListener() {
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
}
