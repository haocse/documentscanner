package com.haotran.documentscanner.activity.adapters;

import android.content.Context;

import com.haotran.documentscanner.model.Capture;

import java.util.List;

public class CaptureAdapterByDay extends BaseCaptureAdapter {

    public CaptureAdapterByDay(List<Capture> itemList) {
        super(itemList);
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
        final Capture movie = captureList.get(position);

        holder.textCaptureTitle.setText(movie.getTitle());
//        holder.textCaptureGenre.setText(movie.getGenre());
//        holder.textCaptureYear.setText(String.valueOf(movie.getYear()));

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(movie));
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
