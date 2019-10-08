package com.sketch.code.two;

import android.app.Activity;

import java.util.HashMap;

public class ProfilesManager {
    Activity activity;
    public ProfilesManager(Activity _activity) {
        activity = _activity;
    }
    Activity getActivity() {
        return activity;
    }
    void getProfile(String id, final Profile profile) {
        RequestNetwork request = new RequestNetwork(getActivity());
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        request.setParams(map, RequestNetworkController.REQUEST_PARAM);
        request.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/getPubInfo.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Bad request") || response.equals("Not found")) {
                    profile.onError(response);
                } else {
                    profile.onLoad(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        });
    }
    void loadUserPost(String id, final Profile profile) {
        RequestNetwork request = new RequestNetwork(getActivity());
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        request.setParams(map, RequestNetworkController.REQUEST_PARAM);
        request.startRequestNetwork(RequestNetworkController.GET, "https://neonteam.net/sketchcode/userscontent/get-list-user-post.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Bad request") || response.equals("Not found")) {
                    profile.onError(response);
                } else {
                    profile.onLoad(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
            }
        });
    }
    void getIdFromMail(String mail, final Profile profile) {
        RequestNetwork request = new RequestNetwork(getActivity());
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", mail);
        request.setParams(map, RequestNetworkController.REQUEST_PARAM);
        request.startRequestNetwork(RequestNetworkController.GET, "https://neonteam.net/sketchcode/account/get-id-byemail.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Bad request") || response.equals("Not found")) {
                    profile.onError(response);
                } else {
                    profile.onLoad(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
            }
        });
    }
    void loadListCodes(String id, final Profile profile) {
        RequestNetwork request = new RequestNetwork(getActivity());
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        request.setParams(map, RequestNetworkController.REQUEST_PARAM);
        request.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/get-list-userscodes.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(response.equals("Bad request") || response.equals("Not found")) {
                    profile.onError(response);
                } else {
                    profile.onLoad(response);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
            }
        });
    }
    interface Profile {
        void onLoad(String data);
        void onError(String data);
    }
}
