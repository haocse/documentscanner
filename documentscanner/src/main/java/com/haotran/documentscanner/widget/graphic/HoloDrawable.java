package com.haotran.documentscanner.widget.graphic;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.haotran.documentscanner.widget.common.WheelConstants;
import com.haotran.documentscanner.widget.widget.WheelView;


/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class HoloDrawable extends WheelDrawable {
    private Paint mHoloBgPaint, mHoloPaint;
    private int mWheelSize, mItemH;

    public HoloDrawable(int width, int height, WheelView.WheelViewStyle style, int wheelSize, int itemH) {
        super(width, height, style);
        mWheelSize = wheelSize;
        mItemH = itemH;
        init();
    }

    private void init() {
        mHoloBgPaint = new Paint();
        mHoloBgPaint.setColor(mStyle.backgroundColor != -1 ? mStyle.backgroundColor : WheelConstants.WHEEL_SKIN_HOLO_BG);
        mHoloPaint = new Paint();
        mHoloPaint.setStrokeWidth(mStyle.holoBorderWidth != -1 ? mStyle.holoBorderWidth : 3);
        mHoloPaint.setColor(mStyle.holoBorderColor != -1 ? mStyle.holoBorderColor : WheelConstants.WHEEL_SKIN_HOLO_BORDER_COLOR);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth, mHeight, mHoloBgPaint);
        if (mItemH != 0) {
            canvas.drawLine(0, mItemH * (mWheelSize / 2), mWidth, mItemH
                    * (mWheelSize / 2), mHoloPaint);
            canvas.drawLine(0, mItemH * (mWheelSize / 2 + 1), mWidth, mItemH
                    * (mWheelSize / 2 + 1), mHoloPaint);
        }
    }
}
