package com.sketch.code.two;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.transform.stream.StreamSource;

public class CodesManager {
    static SharedPreferences codes;
    static Activity activity;
    public CodesManager(Activity _activity){
        codes = _activity.getSharedPreferences("codes", Context.MODE_PRIVATE);
        activity = _activity;
    }
    private static String cleanTextContent(String text)
    {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }
    public Activity getActivity() {
        return activity;
    }
    public ArrayList<HashMap<String, Object>> getSavedCodes() {
        ArrayList<HashMap<String, Object>> arrayList = new Gson().fromJson(codes.getString("saved","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        return arrayList;
    }
    public boolean isSaved(String name){
        ArrayList<HashMap<String, Object>> arrayList = getSavedCodes();
        int x;
        boolean is = false;
        for(x=0; arrayList.size()>x; x++) {
            if(arrayList.get(x).get("name").equals(name))
            {
                is = true;
            }
        }
        return is;
    }
    public ArrayList<HashMap<String, Object>> getCodes() {
        ArrayList<HashMap<String, Object>> arrayList = new Gson().fromJson(codes.getString("all","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        return arrayList;
    }
    public ArrayList<String> getCodesString() {
        ArrayList<HashMap<String, Object>> arrayList = getCodes();
        final ArrayList<String> arrayList2 = new ArrayList();
        int x = 0;
        for(x=0; arrayList.size()>x; x++) {
            arrayList2.add(arrayList.get((int)x).get("name").toString());
        }
        return arrayList2;
    }
    public void removeSavedCode(String name){
        ArrayList<HashMap<String, Object>> arrayList = getSavedCodes();
        int x;
        boolean is = false;
        for(x=0; arrayList.size()>x; x++) {
            if(arrayList.get(x).get("name").equals(name))
            {
                arrayList.remove(x);
            }
        }
        codes.edit().putString("saved", new Gson().toJson(arrayList)).commit();
    }
    public ArrayList<String> getSavedCodesString() {
        ArrayList<HashMap<String, Object>> arrayList = new Gson().fromJson(codes.getString("saved","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        final ArrayList arrayList2 = new ArrayList();
        int x = 0;
        for(x=0; arrayList.size()>x; x++) {
            arrayList2.add(arrayList.get((int)x).get("name").toString());
        }
        return arrayList2;
    }
    public void save(String name, String code, String help) {
            ArrayList<HashMap<String, Object>> arrayList = getSavedCodes();
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("code", code);
            map.put("help", help);
            arrayList.add(map);
            codes.edit().putString("saved", new Gson().toJson(arrayList)).commit();
        }
}
