package com.sketch.code.two;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineCodesManager {
    Activity activity;
    public OnlineCodesManager(Activity _activity) {
        activity = _activity;
    }
    public void loadComments(final LoadListener loadListener) {
        new RequestNetwork(activity).startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/userscontent/getComments1.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                loadListener.onLoad(response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                loadListener.onError(message);
            }
        });
    }
    public ArrayList<HashMap<String, Object>> sortById(String data, String id) {
        ArrayList<HashMap<String, Object>> arrayList = JsonUtil.ArrayfromJson(data);
        ArrayList<HashMap<String, Object>> arrayList1 = new ArrayList<>();
        for(int x=0; arrayList.size()>x; x++) {
            if (arrayList.get(x).get("postId").equals(id)) {
                arrayList1.add(arrayList.get(x));
            }
        }
        return arrayList1;
    }
    public interface LoadListener {
        void onLoad(String data);
        void onError(String message);
    }
}
