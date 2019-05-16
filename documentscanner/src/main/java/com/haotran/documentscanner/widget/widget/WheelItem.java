package com.haotran.documentscanner.widget.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotran.documentscanner.widget.common.WheelConstants;
import com.haotran.documentscanner.widget.util.WheelUtils;


/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class WheelItem extends FrameLayout {
    private ImageView mImage;
    private TextView mText;

    public WheelItem(Context context) {
        super(context);
        init();
    }

    public WheelItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LinearLayout layout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, WheelUtils.dip2px(getContext(),
                WheelConstants.WHEEL_ITEM_HEIGHT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(WheelConstants.WHEEL_ITEM_PADDING, WheelConstants.WHEEL_ITEM_PADDING, WheelConstants.WHEEL_ITEM_PADDING, WheelConstants.WHEEL_ITEM_PADDING);
        layout.setGravity(Gravity.CENTER);
        addView(layout, layoutParams);
        mImage = new ImageView(getContext());
        mImage.setTag(WheelConstants.WHEEL_ITEM_IMAGE_TAG);
        mImage.setVisibility(View.GONE);
        LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageParams.rightMargin = WheelConstants.WHEEL_ITEM_MARGIN;
        layout.addView(mImage, imageParams);
        mText = new TextView(getContext());
        mText.setTag(WheelConstants.WHEEL_ITEM_TEXT_TAG);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setSingleLine();
        mText.setIncludeFontPadding(false);
        mText.setGravity(Gravity.LEFT);
        mText.setPadding(WheelUtils.dip2px(getContext(), 50), WheelConstants.WHEEL_ITEM_PADDING, 0, WheelConstants.WHEEL_ITEM_PADDING);
        mText.setTextColor(Color.BLACK);
        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.addView(mText, textParams);
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public void setImage(int resId) {
        mImage.setVisibility(View.VISIBLE);
        mImage.setImageResource(resId);
    }
}
