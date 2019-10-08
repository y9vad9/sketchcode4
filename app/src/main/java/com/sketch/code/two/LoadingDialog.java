package com.sketch.code.two;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

public class LoadingDialog {
    private AppCompatDialog dialog;
    private Activity activity;
    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }
    public void show() {
        dialog = new AppCompatDialog(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_loading, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new GradientDrawable() {{
            setColor(Color.WHITE);
            setCornerRadius(40);
        }});
        dialog.setCancelable(false);
        dialog.show();
    }
    public void dismiss() {
        dialog.dismiss();
    }
}
