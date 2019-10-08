package com.sketch.code.two;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;

public class TransparentProgressDialog {
    public static ProgressDialog progress;
    public static ProgressDialog showLoading(final Activity activity){
        if(progress==null) {
            progress = ProgressDialog.show(activity, null, null, true);
            progress.setContentView(R.layout.loading);
            progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    activity.finish();
                }
            });

            progress.show();
        } else {
          progress.dismiss();
            progress = ProgressDialog.show(activity, null, null, true);
            progress.setContentView(R.layout.loading);
            progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    activity.finish();
                }
            });

            progress.show();
        }
        return progress;
    }
    public static ProgressDialog hideLoading(Activity activity){
        if(progress==null){

        } else {
            progress.dismiss();
        }
        return progress;
    }
}
