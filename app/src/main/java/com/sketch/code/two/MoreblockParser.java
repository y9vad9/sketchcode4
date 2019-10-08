package com.sketch.code.two;

import android.app.Activity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MoreblockParser {
    public void loadList(Activity activity, final LoadListeners load) {
        HashMap<String, Object> dataSet = new HashMap<>();
        dataSet.put("moreblocks", FileUtil.readFile(FileUtil.getExternalStorageDir().concat("/.sketchware/collection/more_block/list")));
        RequestNetwork requestNetwork = new RequestNetwork(activity);
        requestNetwork.setParams(dataSet, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/tools/m.php", "Moreblocks", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                load.onResponse(response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                load.onError(message);
            }
        });
    }
    public ArrayList<HashMap<String, Object>> format(String data) {
        ArrayList<HashMap<String, Object>> map = JsonUtil.ArrayfromJson(data);
        String temp = data;

        return map;
    }
    public interface LoadListeners {
        void onResponse(String response);
        void onError(String message);
    }
}
