package com.haotran.documentscanner.widget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.haotran.documentscanner.widget.adapter.ArrayWheelAdapter;
import com.haotran.documentscanner.widget.adapter.BaseWheelAdapter;
import com.haotran.documentscanner.widget.adapter.SimpleWheelAdapter;
import com.haotran.documentscanner.widget.common.WheelConstants;
import com.haotran.documentscanner.widget.common.WheelViewException;
import com.haotran.documentscanner.widget.graphic.DrawableFactory;
import com.haotran.documentscanner.widget.util.WheelUtils;

import java.util.HashMap;
import java.util.List;

/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class WheelView<T> extends ListView implements IWheelView<T> {
    private int mItemH = 0;
    private int mWheelSize = WHEEL_SIZE;
    private boolean mLoop = LOOP;
    private List<T> mList = null;
    private int mCurrentPositon = -1;
    private String mExtraText;
    private int mExtraTextColor;
    private int mExtraTextSize;
    private int mExtraMargin;
    private int mSelection = 0;
    private boolean mClickable = CLICKABLE;
    private Paint mTextPaint;
    private Skin mSkin = Skin.None;
    private WheelViewStyle mStyle;
    private WheelView mJoinWheelView;
    private HashMap<String, List<T>> mJoinMap;
    private BaseWheelAdapter<T> mWheelAdapter;
    private OnWheelItemSelectedListener<T> mOnWheelItemSelectedListener;
    private OnWheelItemClickListener<T> mOnWheelItemClickListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WheelConstants.WHEEL_SCROLL_HANDLER_WHAT) {
                if (mOnWheelItemSelectedListener != null) {
                    if (getSelectionItem() != null) {
                        mOnWheelItemSelectedListener.onItemSelected(getCurrentPosition(), getSelectionItem());
                    }
                }
                if (mJoinWheelView != null) {
                    if (!mJoinMap.isEmpty()) {
                        mJoinWheelView.resetDataFromTop(mJoinMap.get(mList.get
                                (getCurrentPosition()))
                        );
                    } else {
                        throw new WheelViewException("JoinList is error.");
                    }
                }
            }
        }
    };
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mOnWheelItemClickListener != null) {
                mOnWheelItemClickListener.onItemClick(getCurrentPosition(), getSelectionItem());
            }
        }
    };
    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                View itemView = getChildAt(0);
                if (itemView != null) {
                    float deltaY = itemView.getY();
                    if (deltaY == 0 || mItemH == 0) {
                        return;
                    }
                    if (Math.abs(deltaY) < mItemH / 2) {
                        int d = getSmoothDistance(deltaY);
                        smoothScrollBy(d, WheelConstants
                                .WHEEL_SMOOTH_SCROLL_DURATION);
                    } else {
                        int d = getSmoothDistance(mItemH + deltaY);
                        smoothScrollBy(d, WheelConstants.WHEEL_SMOOTH_SCROLL_DURATION);
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (visibleItemCount != 0) {
                refreshCurrentPosition(false);
            }
        }
    };

    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, WheelViewStyle style) {
        super(context);
        setStyle(style);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnWheelItemSelectedListener(OnWheelItemSelectedListener<T> onWheelItemSelectedListener) {
        mOnWheelItemSelectedListener = onWheelItemSelectedListener;
    }

    public void setOnWheelItemClickListener(OnWheelItemClickListener<T> onWheelItemClickListener) {
        mOnWheelItemClickListener = onWheelItemClickListener;
    }

    private void init() {
        if (mStyle == null) {
            mStyle = new WheelViewStyle();
        }
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setTag(WheelConstants.TAG);
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(Color.TRANSPARENT);
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setDividerHeight(0);
        setOnItemClickListener(mOnItemClickListener);
        setOnScrollListener(mOnScrollListener);
        setOnTouchListener(mTouchListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        addOnGlobalLayoutListener();
    }

    private void addOnGlobalLayoutListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (getChildCount() > 0 && mItemH == 0) {
                    mItemH = getChildAt(0).getHeight();
                    if (mItemH != 0) {
                        ViewGroup.LayoutParams params = getLayoutParams();
                        params.height = mItemH * mWheelSize;
                        refreshVisibleItems(getFirstVisiblePosition(), getCurrentPosition() + mWheelSize / 2, mWheelSize / 2);
                        setBackground();
                    } else {
                        throw new WheelViewException("wheel item is error.");
                    }
                }
            }
        });
    }

    public WheelViewStyle getStyle() {
        return mStyle;
    }

    public void setStyle(WheelViewStyle style) {
        mStyle = style;
    }

    private void setBackground() {
        Drawable drawable = DrawableFactory.createDrawable(mSkin, getWidth(), mItemH * mWheelSize, mStyle, mWheelSize, mItemH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    public Skin getSkin() {
        return mSkin;
    }

    public void setSkin(Skin skin) {
        mSkin = skin;
    }

    @Override
    public void setWheelSize(int wheelSize) {
        if ((wheelSize & 1) == 0) {
            throw new WheelViewException("wheel size must be an odd number.");
        }
        mWheelSize = wheelSize;
        if (mWheelAdapter != null) {
            mWheelAdapter.setWheelSize(wheelSize);
        }
    }

    @Override
    public void setLoop(boolean loop) {
        if (loop != mLoop) {
            mLoop = loop;
            setSelection(0);
            if (mWheelAdapter != null) {
                mWheelAdapter.setLoop(loop);
            }
        }
    }

    @Override
    public void setWheelClickable(boolean clickable) {
        if (clickable != mClickable) {
            mClickable = clickable;
            if (mWheelAdapter != null) {
                mWheelAdapter.setClickable(clickable);
            }
        }
    }

    public void resetDataFromTop(final List<T> list) {
        if (WheelUtils.isEmpty(list)) {
            throw new WheelViewException("join map data is error.");
        }
        WheelView.this.postDelayed(new Runnable() {
            @Override
            public void run() {
                setWheelData(list);
                WheelView.super.setSelection(mSelection);
                refreshCurrentPosition(true);
            }
        }, 10);
    }

    public int getSelection() {
        return mSelection;
    }

    @Override
    public void setSelection(final int selection) {
        mSelection = selection;
        setVisibility(View.INVISIBLE);
        WheelView.this.postDelayed(new Runnable() {
            @Override
            public void run() {
                WheelView.super.setSelection(getRealPosition(selection));
                refreshCurrentPosition(false);
                setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public void join(WheelView wheelView) {
        if (wheelView == null) {
            throw new WheelViewException("wheelview cannot be null.");
        }
        mJoinWheelView = wheelView;
    }

    public void joinDatas(HashMap<String, List<T>> map) {
        mJoinMap = map;
    }

    private int getRealPosition(int positon) {
        if (WheelUtils.isEmpty(mList)) {
            return 0;
        }
        if (mLoop) {
            int d = Integer.MAX_VALUE / 2 / mList.size();
            return positon + d * mList.size() - mWheelSize / 2;
        }
        return positon;
    }

    public int getCurrentPosition() {
        return mCurrentPositon;
    }

    public T getSelectionItem() {
        int position = getCurrentPosition();
        position = position < 0 ? 0 : position;
        if (mList != null && mList.size() > position) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null && adapter instanceof BaseWheelAdapter) {
            setWheelAdapter((BaseWheelAdapter) adapter);
        } else {
            throw new WheelViewException("please invoke setWheelAdapter " + "method.");
        }
    }

    @Override
    public void setWheelAdapter(BaseWheelAdapter<T> adapter) {
        super.setAdapter(adapter);
        mWheelAdapter = adapter;
        mWheelAdapter.setData(mList).setWheelSize(mWheelSize).setLoop(mLoop).setClickable(mClickable);
    }

    @Override
    public void setWheelData(List<T> list) {
        if (WheelUtils.isEmpty(list)) {
            throw new WheelViewException("wheel datas are error.");
        }
        mList = list;
        if (mWheelAdapter != null) {
            mWheelAdapter.setData(list);
        }
    }

    public void setExtraText(String text, int textColor, int textSize, int
            margin) {
        mExtraText = text;
        mExtraTextColor = textColor;
        mExtraTextSize = textSize;
        mExtraMargin = margin;
    }

    public int getWheelCount() {
        return !WheelUtils.isEmpty(mList) ? mList.size() : 0;
    }

    private int getSmoothDistance(float scrollDistance) {
        if (Math.abs(scrollDistance) <= 2) {
            return (int) scrollDistance;
        } else if (Math.abs(scrollDistance) < 12) {
            return scrollDistance > 0 ? 2 : -2;
        } else {
            return (int) (scrollDistance / 6);
        }
    }

    private void refreshCurrentPosition(boolean join) {
        if (getChildAt(0) == null || mItemH == 0) {
            return;
        }
        int firstPosition = getFirstVisiblePosition();
        if (mLoop && firstPosition == 0) {
            return;
        }
        int position = 0;
        if (Math.abs(getChildAt(0).getY()) <= mItemH / 2) {
            position = firstPosition;
        } else {
            position = firstPosition + 1;
        }
        refreshVisibleItems(firstPosition, position + mWheelSize / 2,
                mWheelSize / 2);
        if (mLoop) {
            position = (position + mWheelSize / 2) % getWheelCount();
        }
        if (position == mCurrentPositon && !join) {
            return;
        }
        mCurrentPositon = position;
        mWheelAdapter.setCurrentPosition(position);
        mHandler.removeMessages(WheelConstants.WHEEL_SCROLL_HANDLER_WHAT);
        mHandler.sendEmptyMessageDelayed(WheelConstants
                .WHEEL_SCROLL_HANDLER_WHAT, WheelConstants
                .WHEEL_SCROLL_DELAY_DURATION);
    }

    private void refreshVisibleItems(int firstPosition, int curPosition, int offset) {
        for (int i = curPosition - offset; i <= curPosition + offset; i++) {
            View itemView = getChildAt(i - firstPosition);
            if (itemView == null) {
                continue;
            }
            if (mWheelAdapter instanceof ArrayWheelAdapter || mWheelAdapter
                    instanceof SimpleWheelAdapter) {
                TextView textView = (TextView) itemView.findViewWithTag
                        (WheelConstants.WHEEL_ITEM_TEXT_TAG);
                refreshTextView(i, curPosition, itemView, textView);
            } else {
                TextView textView = WheelUtils.findTextView(itemView);
                if (textView != null) {
                    refreshTextView(i, curPosition, itemView, textView);
                }
            }
        }
    }

    private void refreshTextView(int position, int curPosition, View
            itemView, TextView textView) {
        if (curPosition == position) {
            int textColor = mStyle.selectedTextColor != -1 ? mStyle
                    .selectedTextColor : (mStyle.textColor != -1 ? mStyle
                    .textColor : WheelConstants.WHEEL_TEXT_COLOR);
            float defTextSize = mStyle.textSize != -1 ? mStyle.textSize : WheelConstants.WHEEL_TEXT_SIZE;
            float textSize = mStyle.selectedTextSize != -1 ? mStyle
                    .selectedTextSize : (mStyle.selectedTextZoom != -1 ? (defTextSize * mStyle.selectedTextZoom) :
                    defTextSize);
            setTextView(itemView, textView, textColor, textSize, 1.0f);
        } else {
            int textColor = mStyle.textColor != -1 ? mStyle.textColor :
                    WheelConstants.WHEEL_TEXT_COLOR;
            float textSize = mStyle.textSize != -1 ? mStyle.textSize :
                    WheelConstants.WHEEL_TEXT_SIZE;
            int delta = Math.abs(position - curPosition);
            float alpha = (float) Math.pow(mStyle.textAlpha != -1 ? mStyle.textAlpha : WheelConstants
                    .WHEEL_TEXT_ALPHA, delta);
            setTextView(itemView, textView, textColor, textSize, alpha);
        }
    }

    private void setTextView(View itemView, TextView textView, int textColor, float textSize, float textAlpha) {
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        itemView.setAlpha(textAlpha);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!TextUtils.isEmpty(mExtraText)) {
            Rect targetRect = new Rect(0, mItemH * (mWheelSize / 2), getWidth
                    (), mItemH * (mWheelSize / 2 + 1));
            mTextPaint.setTextSize(mExtraTextSize);
            mTextPaint.setColor(mExtraTextColor);
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            int baseline = (targetRect.bottom + targetRect.top - fontMetrics
                    .bottom - fontMetrics.top) / 2;
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mExtraText, targetRect.centerX() + mExtraMargin,
                    baseline, mTextPaint);
        }
    }

    public enum Skin {
        Common, Holo, None
    }

    public interface OnWheelItemSelectedListener<T> {
        void onItemSelected(int position, T t);
    }

    public interface OnWheelItemClickListener<T> {
        void onItemClick(int position, T t);
    }

    public static class WheelViewStyle {
        public int backgroundColor = -1;
        public int holoBorderColor = -1;
        public int holoBorderWidth = -1;
        public int textColor = -1;
        public int selectedTextColor = -1;
        public int textSize = -1;
        public int selectedTextSize = -1;
        public float textAlpha = -1;
        public float selectedTextZoom = -1;

        public WheelViewStyle() {
        }

        public WheelViewStyle(WheelViewStyle style) {
            this.backgroundColor = style.backgroundColor;
            this.holoBorderColor = style.holoBorderColor;
            this.holoBorderWidth = style.holoBorderWidth;
            this.textColor = style.textColor;
            this.selectedTextColor = style.selectedTextColor;
            this.textSize = style.textSize;
            this.selectedTextSize = style.selectedTextSize;
            this.textAlpha = style.textAlpha;
            this.selectedTextZoom = style.selectedTextZoom;
        }
    }
}
