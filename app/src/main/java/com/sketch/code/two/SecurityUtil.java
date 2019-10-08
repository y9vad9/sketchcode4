package com.sketch.code.two;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityUtil {
        public static boolean verify;
    public static boolean verifyInstallerId(Context context, String packageName) {
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));
        final String installer = context.getPackageManager().getInstallerPackageName(packageName);
        return installer != null && validInstallers.contains(installer);
    }

    public static void notVerifiedApp(final Activity activity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Error");
        dialog.setMessage("Download sketchcode from play store :)");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
