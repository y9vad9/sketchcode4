package com.sketch.code.two;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class MoreblockManager {
    static Activity act;
    static SharedPreferences data;
    public MoreblockManager(Activity activity) {
        act = activity;
        data = activity.getSharedPreferences("moreblocks", Context.MODE_PRIVATE);
    }
    public ArrayList<HashMap<String, Object>> getMoreblocks() {
        ArrayList<HashMap<String, Object>> arrayList;
        arrayList = new Gson().fromJson(data.getString("all","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        return arrayList;
    }
    public ArrayList<HashMap<String, Object>> getSavedMoreblocks() {
        ArrayList<HashMap<String, Object>> arrayList;
        arrayList = new Gson().fromJson(data.getString("saved","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        return arrayList;
    }
    public Activity getActivity() {
        return act;
    }
    public ArrayList<String> getMoreblocksString() {
        ArrayList<HashMap<String, Object>> arrayList;
        arrayList = new Gson().fromJson(data.getString("all","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        final ArrayList arrayList2 = new ArrayList();
        int x = 0;
        for(x=0; arrayList.size()>x; x++) {
            arrayList2.add(arrayList.get((int)x).get("name").toString());
        }
        return arrayList2;
    }
    public ArrayList<String> getSavedMoreblocksString() {
        ArrayList<HashMap<String, Object>> arrayList;
        arrayList = new Gson().fromJson(data.getString("saved","[]"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        final ArrayList arrayList2 = new ArrayList();
        int x = 0;
        for(x=0; arrayList.size()>x; x++) {
            arrayList2.add(arrayList.get((int)x).get("name").toString());
        }
        return arrayList2;
    }
    public boolean add(String block) {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(act, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            return false;
        }
        String blockss = FileUtil.readFile(FileUtil.getExternalStorageDir().concat("/.sketchware/collection/more_block/list"));
        FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.sketchware/collection/more_block/list"), blockss + "\n" + block);
        return true;
    }
    boolean save(String name, String subtitle, String datas){
        ArrayList<HashMap<String, Object>> map = getSavedMoreblocks();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("subtitle", subtitle);
        hashMap.put("data", datas);
        map.add(hashMap);
        data.edit().putString("saved", new Gson().toJson(map)).commit();
        return true;
    }
    boolean remove(String name)
    {
        ArrayList<HashMap<String, Object>> arrayList = getSavedMoreblocks();
        int x;
        boolean is = false;
        for(x=0; arrayList.size()>x; x++) {
            if(arrayList.get(x).get("name").equals(name))
            {
                arrayList.remove(x);
                is = true;
            }
        }
        data.edit().putString("saved", new Gson().toJson(arrayList)).commit();
        return is;
    }
    boolean isSaved(String name)
    {
        ArrayList<HashMap<String, Object>> arrayList = getSavedMoreblocks();
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

}
