package com.haotran.documentscanner.widget.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;

/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class WheelUtils {
    private static final String TAG = "WheelView";

    public static void log(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.d(TAG, msg);
        }
    }

    public static <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }

    public static TextView findTextView(View view) {
        if (view instanceof TextView) {
            return (TextView) view;
        } else {
            if (view instanceof ViewGroup) {
                return findTextView(((ViewGroup) view).getChildAt(0));
            } else {
                return null;
            }
        }
    }

    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }
}
