package com.maxwu.pulltofresh.app.ui;

import android.content.Context;

/**
 * User MaxWu
 * Data 2015/9/22
 * Time 16:59
 */
public class Utils {

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}

