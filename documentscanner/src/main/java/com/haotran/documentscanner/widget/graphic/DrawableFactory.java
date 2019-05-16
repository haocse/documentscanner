package com.haotran.documentscanner.widget.graphic;
/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */

import android.graphics.drawable.Drawable;

import com.haotran.documentscanner.widget.widget.WheelView;


public class DrawableFactory {
    public static Drawable createDrawable(WheelView.Skin skin, int width, int height, WheelView.WheelViewStyle
            style, int wheelSize, int itemH) {
        if (skin.equals(WheelView.Skin.Common)) {
            return new CommonDrawable(width, height, style, wheelSize, itemH);
        } else if (skin.equals(WheelView.Skin.Holo)) {
            return new HoloDrawable(width, height, style, wheelSize, itemH);
        } else {
            return new WheelDrawable(width, height, style);
        }
    }
}
