package com.haotran.documentscanner.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.haotran.documentscanner.widget.common.WheelData;
import com.haotran.documentscanner.widget.widget.WheelItem;


/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class SimpleWheelAdapter extends BaseWheelAdapter<WheelData> {
    private Context mContext;

    public SimpleWheelAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new WheelItem(mContext);
        }
        WheelItem item = (WheelItem) convertView;
        item.setImage(mList.get(position).getId());
        item.setText(mList.get(position).getName());
        return convertView;
    }
}
