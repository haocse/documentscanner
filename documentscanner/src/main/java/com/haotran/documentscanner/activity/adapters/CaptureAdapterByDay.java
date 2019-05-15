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
        final String movieGenre = movieList.get(position).getDay();
        final String nextMovieGenre = movieList.get(position + 1).getDay();

        return !movieGenre.equals(nextMovieGenre);

//        final char movieTitleFirstCharacter = movieList.get(position).getTitle().charAt(0);
//        final char nextMovieTitleFirstCharacter = movieList.get(position + 1).getTitle().charAt(0);
//        return movieTitleFirstCharacter != nextMovieTitleFirstCharacter;
    }

    @Override
    public void onBindItemViewHolder(final MovieViewHolder holder, final int position) {
        final Capture movie = movieList.get(position);

        holder.textMovieTitle.setText(movie.getTitle());
//        holder.textMovieGenre.setText(movie.getGenre());
//        holder.textMovieYear.setText(String.valueOf(movie.getYear()));

//        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(movie));
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final Context context = subheaderHolder.itemView.getContext();
        final Capture nextMovie = movieList.get(nextItemPosition);
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
