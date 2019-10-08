package com.sketch.code.two;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsManager {
    Activity activity;
    ArrayList<HashMap<String, Object>> settings;
    ArrayList<View.OnClickListener> clickListeners;
    public SettingsManager(Activity act)
    {
        activity = act;
        settings = new ArrayList<>();
        clickListeners = new ArrayList<>();
    }
    public void add(String title, String subtitle, View.OnClickListener clickListener) {
        clickListeners.add(clickListener);
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", "Choose theme");
        map.put("subtitle", "Choose the topic that you prefer.");
        settings.add(map);
    }
    public ArrayList<HashMap<String, Object>> getSettings() {
        return settings;
    }
    public ArrayList<View.OnClickListener> getSettingsListeners() {
        return clickListeners;
    }

}
