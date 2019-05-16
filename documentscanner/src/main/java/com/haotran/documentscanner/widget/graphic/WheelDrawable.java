package com.haotran.documentscanner.widget.graphic;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.haotran.documentscanner.widget.common.WheelConstants;
import com.haotran.documentscanner.widget.widget.WheelView;


/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class WheelDrawable extends Drawable {
    protected int mWidth;
    protected int mHeight;
    protected WheelView.WheelViewStyle mStyle;
    private Paint mBgPaint;

    public WheelDrawable(int width, int height, WheelView.WheelViewStyle style) {
        mWidth = width;
        mHeight = height;
        mStyle = style;
        init();
    }

    private void init() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mStyle.backgroundColor != -1 ? mStyle.backgroundColor : WheelConstants.WHEEL_BG);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
