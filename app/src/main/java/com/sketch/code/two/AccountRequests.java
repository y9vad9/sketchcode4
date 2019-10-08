package com.sketch.code.two;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import static com.sketch.code.two.JsonUtil.*;
import static com.sketch.code.two.AccountManager.*;

public class AccountRequests {
    public static void register(final HashMap<String, Object> _data, Activity activity) {
        RequestNetwork requestNetwork = new RequestNetwork(activity);
        requestNetwork.setParams(_data, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/create-user.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(getGsonDataKey(response, "errorcode").equals("1000")) {
                    account.onSuccess(1000, getGsonDataKey(response, "responce"));
                    data.edit().putString("email", _data.get("email").toString()).commit();
                    data.edit().putString("password", _data.get("password").toString()).commit();
                } else {
                    account.onError(Integer.parseInt(getGsonDataKey(response, "errorcode")), getGsonDataKey(response, "responce"));
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                account.onConnectionError(message);
            }
        });
    }
    public static void login(final HashMap<String, Object> data, Activity activity) {
        RequestNetwork requestNetwork = new RequestNetwork(activity);
        requestNetwork.setParams(data, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/log_in.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                if(getGsonDataKey(response, "errorcode").equals("0")) {
                    account.onSuccess(0, "Success");
                    AccountManager.data.edit().putString("email", data.get("email").toString()).commit();
                    AccountManager.data.edit().putString("password", data.get("password").toString()).commit();
                } else {
                    account.onError(1,  getGsonDataKey(response, "responce"));
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                account.onConnectionError(message);
            }
        });
    }
    public static void loadAccoundData(final Activity activity) {
        RequestNetwork requestNetwork = new RequestNetwork(activity);
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", data.getString("email",""));
        map.put("password", data.getString("password", ""));
        requestNetwork.setParams(map, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.POST, "https://neonteam.net/sketchcode/account/about.php", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response) {
                try{
                 HashMap<String, Object> hashMap = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>(){}.getType());
                 data.edit().putString("prem", JsonUtil.getGsonDataKey(response, "prem")).commit();
                 accountData.onDataReceived(hashMap);
                } catch (Exception e) {
                    activity.finish();
                }

            }

            @Override
            public void onErrorResponse(String tag, String message) {
                accountData.onError(message);
            }
        });
    }
}
