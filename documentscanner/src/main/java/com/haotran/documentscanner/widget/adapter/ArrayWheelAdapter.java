package com.haotran.documentscanner.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.haotran.documentscanner.widget.widget.WheelItem;

/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class ArrayWheelAdapter<T> extends BaseWheelAdapter<T> {
    private Context mContext;

    public ArrayWheelAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new WheelItem(mContext);
        }
        WheelItem wheelItem = (WheelItem) convertView;
        T item = getItem(position);
        if (wheelItem instanceof CharSequence) {
            wheelItem.setText((CharSequence) item);
        } else {
            wheelItem.setText(item.toString());
        }
        return convertView;
    }
}
