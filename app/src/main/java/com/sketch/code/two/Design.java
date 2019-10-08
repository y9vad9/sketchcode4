package com.sketch.code.two;

import android.graphics.Color;
import android.view.View;

public class Design {
    static void borderWithRound(View view,float cornerRadius, int width, int _col1, int _col2, int _col3, String hexColor) {
        int[] colors = {Color.rgb((int) _col1,(int) _col2,(int)_col3),Color.rgb((int) _col1,(int) _col2,(int)_col3)};
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.BR_TL, colors);
        gd.setCornerRadius((float)cornerRadius);
        gd.setStroke((int)width, (Color.parseColor(hexColor)));
        view.setBackground(gd);
    }
}
