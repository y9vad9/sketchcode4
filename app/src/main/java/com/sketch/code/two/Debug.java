package com.sketch.code.two;

import android.app.Activity;

public class Debug {
    private static String error_path = FileUtil.getExternalStorageDir() + "/sketchcode/data/reports/error.txt";
    private static String response_path = FileUtil.getExternalStorageDir() + "/sketchcode/data/reports/response.txt";

    public static void reportError(Exception e) {
        FileUtil.writeFile(error_path, e.toString());
        return;
    }
    public static void writeResponse(String s){
        FileUtil.writeFile(response_path, s);
    }
}
