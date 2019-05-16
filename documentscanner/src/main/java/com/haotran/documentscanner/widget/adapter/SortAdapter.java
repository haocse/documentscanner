package com.haotran.documentscanner.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.widget.model.SortBy;

public class SortAdapter extends BaseWheelAdapter<SortBy> {
    private Context mContext;

    public SortAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected View bindView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, null);
            viewHolder.textView = convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mList.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}