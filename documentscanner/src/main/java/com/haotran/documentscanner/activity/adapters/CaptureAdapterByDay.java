package com.haotran.documentscanner.activity.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.model.Capture;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CaptureAdapterByDay extends BaseCaptureAdapter {
    Context context;
    public CaptureAdapterByDay(Context context, List<Capture> itemList) {
        super(itemList);
        this.context = context;
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final String capture = captureList.get(position).getDay();
        final String nextCapture = captureList.get(position + 1).getDay();

        return !capture.equals(nextCapture);

//        final char movieTitleFirstCharacter = captureList.get(position).getTitle().charAt(0);
//        final char nextMovieTitleFirstCharacter = captureList.get(position + 1).getTitle().charAt(0);
//        return movieTitleFirstCharacter != nextMovieTitleFirstCharacter;
    }

    @Override
    public void onBindItemViewHolder(final CaptureViewHolder holder, final int position) {
        final Capture capture = captureList.get(position);

        holder.textCaptureTitle.setText(capture.getTitle());


        // Convert title to correct time.

        holder.timestamp.setText( convetTime(capture.getTitle()));
        holder.location.setText(capture.isUploaded() ? "Uploaded" : "Local Storage");
        if (capture.isUploaded()) {
            holder.location.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        } else {

        }
        holder.page.setText(getPageSize(capture) + " page");

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(capture));
    }

    private int getPageSize(Capture capture) {
        String title = capture.getTitle();
        File dir;
        if (capture.isUploaded()) {
            dir = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.UPLOADED_IMAGE_DIR, context);
        } else  {
            dir = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, context);
        }

        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().split("_")[0].equals(title);
            }
        });
        return files.length;
    }

    private String convetTime(String title) {
        Log.d(">>>", title);

//        String time = "Aug 23, 2018 | 17:30";
        String time = "This is time...";
        DateFormat f = new SimpleDateFormat("MMM dd, yyyy | HH:mm");
//        System.out.println();
        time = f.format(Long.valueOf(title));
        return time;
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final Context context = subheaderHolder.itemView.getContext();
        final Capture nextMovie = captureList.get(nextItemPosition);
        final int sectionSize = getSectionSize(getSectionIndex(subheaderHolder.getAdapterPosition()));

//        final String title = nextMovie.getTitle().substring(0, 1);

        final String title = nextMovie.getDay();

//        final String subheaderText = String.format(
//                context.getString(R.string.subheader),
//                title,
//                context.getResources().getQuantityString(R.plurals.item, sectionSize, sectionSize)
//        );
        final String subheaderText = title;

        subheaderHolder.mSubheaderText.setText(subheaderText);
    }
}
