package com.sketch.code.two;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FilesManager {
    String currentpath;
    public FilesManager() {

    }
    public ArrayList<HashMap<String, Object>> getStandartFolder() {
        ArrayList<HashMap<String, Object>> path = new ArrayList<>();
        ArrayList<String> map = new ArrayList<>();
        FileUtil.listDir(FileUtil.getExternalStorageDir(), map);
        FilePickerActivity.currfile = FileUtil.getExternalStorageDir();
        currentpath = FileUtil.getExternalStorageDir();
        for(int x=0; map.size()>x; x++) {
            HashMap<String, Object> file = new HashMap<>();
            file.put("path",map.get(x));
            file.put("isFile", Boolean.toString(FileUtil.isFile(map.get(x))));
            file.put("size", FileUtil.getFileLength(map.get(x)));
            file.put("name", Uri.parse(map.get(x)).getLastPathSegment());
            path.add(file);
        }
        return path;
    }
    public ArrayList<HashMap<String, Object>> goTo(String _path) {
        ArrayList<HashMap<String, Object>> path = new ArrayList<>();
        ArrayList<String> map = new ArrayList<>();
        FileUtil.listDir(_path, map);
        FilePickerActivity.currfile = FileUtil.getExternalStorageDir();
        currentpath = FileUtil.getExternalStorageDir();
        for(int x=0; map.size()>x; x++) {
            HashMap<String, Object> file = new HashMap<>();
            file.put("path",map.get(x));
            file.put("isFile", Boolean.toString(FileUtil.isFile(map.get(x))));
            file.put("size", FileUtil.getFileLength(map.get(x)));
            file.put("name", Uri.parse(map.get(x)).getLastPathSegment());
            path.add(file);
        }
        return path;
    }
}
