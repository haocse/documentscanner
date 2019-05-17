package com.haotran.documentscanner.activity.adapters;

import android.graphics.Typeface;
import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.model.Capture;
import com.haotran.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;


public abstract class BaseCaptureAdapter extends SectionedRecyclerViewAdapter<BaseCaptureAdapter.SubheaderHolder, BaseCaptureAdapter.CaptureViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(Capture capture);
        void onSubheaderClicked(int position);
    }

    List<Capture> captureList;

    OnItemClickListener onItemClickListener;

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;
//        ImageView mArrow;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);
//            this.mArrow = (ImageView) itemView.findViewById(R.id.arrow);

            if(meduiumTypeface == null) {
                meduiumTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Medium.ttf");
            }
            this.mSubheaderText.setTypeface(meduiumTypeface);
        }

    }

    static class CaptureViewHolder extends RecyclerView.ViewHolder {

        TextView textCaptureTitle;
        TextView textCaptureTime;
        TextView location;
        TextView timestamp;
        TextView page;

        CaptureViewHolder(View itemView) {
            super(itemView);
            this.textCaptureTitle = (TextView) itemView.findViewById(R.id.captureTitle);
            this.textCaptureTime = (TextView) itemView.findViewById(R.id.timestamp);
            this.location = (TextView) itemView.findViewById(R.id.localtion);
            this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            this.page = (TextView) itemView.findViewById(R.id.page);
        }
    }

    BaseCaptureAdapter(List<Capture> itemList) {
        super();
        this.captureList = itemList;
    }

    @Override
    public CaptureViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new CaptureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_capture, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
    }

    @Override
    @CallSuper
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {

        boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));

//        if (isSectionExpanded) {
//            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_up_black_24dp));
//        } else {
//            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));
//        }

//        subheaderHolder.itemView.setOnClickListener(v -> onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition()));

    }

    @Override
    public int getItemSize() {
        return captureList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
