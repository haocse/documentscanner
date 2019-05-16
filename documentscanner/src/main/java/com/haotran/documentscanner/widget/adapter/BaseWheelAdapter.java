package com.haotran.documentscanner.widget.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.haotran.documentscanner.widget.util.WheelUtils;
import com.haotran.documentscanner.widget.widget.IWheelView;

import java.util.List;

/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public abstract class BaseWheelAdapter<T> extends BaseAdapter {
    protected List<T> mList = null;
    protected boolean mLoop = IWheelView.LOOP;
    protected int mWheelSize = IWheelView.WHEEL_SIZE;
    protected boolean mClickable = IWheelView.CLICKABLE;
    private int mCurrentPositon = -1;

    protected abstract View bindView(int position, View convertView, ViewGroup parent);

    public final void setCurrentPosition(int position) {
        mCurrentPositon = position;
    }

    @Override
    public final int getCount() {
        if (mLoop) {
            return Integer.MAX_VALUE;
        }
        return !WheelUtils.isEmpty(mList) ? (mList.size() + mWheelSize - 1) : 0;
    }

    @Override
    public final long getItemId(int position) {
        return !WheelUtils.isEmpty(mList) ? position % mList.size() : position;
    }

    @Override
    public final T getItem(int position) {
        return !WheelUtils.isEmpty(mList) ? mList.get(position % mList.size()) : null;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mClickable ? false : true;
    }

    @Override
    public boolean isEnabled(int position) {
        if (mClickable) {
            if (mLoop) {
                if (position % mList.size() == mCurrentPositon) {
                    return true;
                }
            } else {
                if (position == (mCurrentPositon + mWheelSize / 2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (mLoop) {
            position = position % mList.size();
        } else {
            if (position < mWheelSize / 2) {
                position = -1;
            } else if (position >= mWheelSize / 2 + mList.size()) {
                position = -1;
            } else {
                position = position - mWheelSize / 2;
            }
        }
        View view;
        if (position == -1) {
            view = bindView(0, convertView, parent);
        } else {
            view = bindView(position, convertView, parent);
        }
        if (!mLoop) {
            if (position == -1) {
                view.setVisibility(View.INVISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    public final BaseWheelAdapter setClickable(boolean clickable) {
        if (clickable != mClickable) {
            mClickable = clickable;
            super.notifyDataSetChanged();
        }
        return this;
    }

    public final BaseWheelAdapter setLoop(boolean loop) {
        if (loop != mLoop) {
            mLoop = loop;
            super.notifyDataSetChanged();
        }
        return this;
    }

    public final BaseWheelAdapter setWheelSize(int wheelSize) {
        mWheelSize = wheelSize;
        super.notifyDataSetChanged();
        return this;
    }

    public final BaseWheelAdapter setData(List<T> list) {
        mList = list;
        super.notifyDataSetChanged();
        return this;
    }

    @Override
    public final void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public final void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}
