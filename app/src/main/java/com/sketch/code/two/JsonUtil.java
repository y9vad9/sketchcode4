package com.sketch.code.two;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class JsonUtil {
    public static HashMap getGsonData(String str){
        HashMap<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }
    public static String getGsonDataKey(String str, String key){
        try {
            HashMap<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            return map.get(key).toString();
        }catch (Exception e){

            return "Error: " + e.toString();
        }

    }
    public static Activity context;
    public static void showMessage(String s){
        Toast.makeText(context, s,Toast.LENGTH_LONG).show();
    }
    public static ArrayList<HashMap<String, Object>> ArrayfromJson(String data) {
       ArrayList<HashMap<String, Object>> arrayList = new Gson().fromJson(data, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
       return arrayList;
    }
    public static String getValue(String value, String key) {
        HashMap<String, Object> map = new Gson().fromJson(value, new TypeToken<HashMap<String, Object>>(){}.getType());
        return map.get(key).toString();
    }
    public static ArrayList<HashMap<String, Object>> reverse(ArrayList<HashMap<String, Object>> map) {
        Collections.reverse(map);
        return map;
    }
    public static ArrayList<String> getStringFromHashMap(ArrayList<HashMap<String, Object>> map, String key) {
        ArrayList<String> map2 = new ArrayList<>();
        for(int x=0; map.size()>x; x++) {
            map2.add(map.get(x).get(key).toString());
        }
        return map2;
    }
}
