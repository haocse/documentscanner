package com.haotran.documentscanner.widget.graphic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;

import com.haotran.documentscanner.widget.common.WheelConstants;
import com.haotran.documentscanner.widget.widget.WheelView;


/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class CommonDrawable extends WheelDrawable {
    private static final int[] SHADOWS_COLORS =
            {
                    0xFF111111,
                    0x00AAAAAA,
                    0x00AAAAAA
            };
    private GradientDrawable mTopShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            SHADOWS_COLORS);
    private GradientDrawable mBottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
            SHADOWS_COLORS);
    private Paint mCommonBgPaint, mCommonPaint, mCommonDividerPaint, mCommonBorderPaint;
    private int mWheelSize, mItemH;

    public CommonDrawable(int width, int height, WheelView.WheelViewStyle style, int wheelSize, int itemH) {
        super(width, height, style);
        mWheelSize = wheelSize;
        mItemH = itemH;
        init();
    }

    private void init() {
        mCommonBgPaint = new Paint();
        mCommonBgPaint.setColor(mStyle.backgroundColor != -1 ? mStyle.backgroundColor : WheelConstants.WHEEL_SKIN_COMMON_BG);
        mCommonPaint = new Paint();
        mCommonPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_COLOR);
        mCommonDividerPaint = new Paint();
        mCommonDividerPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_DIVIDER_COLOR);
        mCommonDividerPaint.setStrokeWidth(2);
        mCommonBorderPaint = new Paint();
        mCommonBorderPaint.setStrokeWidth(6);
        mCommonBorderPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_BORDER_COLOR);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth, mHeight, mCommonBgPaint);
        if (mItemH != 0) {
            canvas.drawRect(0, mItemH * (mWheelSize / 2), mWidth, mItemH
                    * (mWheelSize / 2 + 1), mCommonPaint);
            canvas.drawLine(0, mItemH * (mWheelSize / 2), mWidth, mItemH
                    * (mWheelSize / 2), mCommonDividerPaint);
            canvas.drawLine(0, mItemH * (mWheelSize / 2 + 1), mWidth, mItemH
                    * (mWheelSize / 2 + 1), mCommonDividerPaint);
            mTopShadow.setBounds(0, 0, mWidth, mItemH);
            mTopShadow.draw(canvas);
            mBottomShadow.setBounds(0, mHeight - mItemH, mWidth, mHeight);
            mBottomShadow.draw(canvas);
            canvas.drawLine(0, 0, 0, mHeight, mCommonBorderPaint);
            canvas.drawLine(mWidth, 0, mWidth, mHeight, mCommonBorderPaint);
        }
    }
}
