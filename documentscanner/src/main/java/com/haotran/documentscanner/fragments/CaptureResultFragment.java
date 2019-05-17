package com.haotran.documentscanner.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.activity.EditingActivity;
import com.haotran.documentscanner.activity.adapters.BaseCaptureAdapter;
import com.haotran.documentscanner.activity.adapters.CaptureAdapterByDay;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.model.Capture;
import com.haotran.documentscanner.util.GridDividerDecoration;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class CaptureResultFragment extends Fragment implements BaseCaptureAdapter.OnItemClickListener/*, NewCaptureDialogFragment.DialogListener*/ {

    public static final String GROUP_NAME = "GROUP_NAME";
    private List<Capture> mMovieList;
    private List<Capture> mMovieListStorage;
    private List<Capture> mMovieListUploaded;

    private Comparator<Capture> movieComparator;

    private RecyclerView recyclerView;

    private BaseCaptureAdapter mSectionedRecyclerAdapter;

    private GridDividerDecoration gridDividerDecoration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capture_t, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gridDividerDecoration = new GridDividerDecoration(getContext());

//        recyclerView.addItemDecoration(gridDividerDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

//        Resources resources = getResources();
//        String[] names = resources.getStringArray(R.array.names);
//        String[] genres = resources.getStringArray(R.array.genres);
//        int[] years = resources.getIntArray(R.array.years);
//        int[] uploadeds = resources.getIntArray(R.array.uploaded);


        showFiles();
    }

    @Override
    public void onResume() {
        super.onResume();
        showFiles();
    }

    void showFiles() {
        File dir = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, getActivity());
        File[] files = dir.listFiles(file -> file.getName().endsWith(".png"));

        File dirUploaded = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.UPLOADED_IMAGE_DIR, getActivity());
        File[] filesUploaded = dirUploaded.listFiles(file -> file.getName().endsWith(".png"));

        mMovieList = new ArrayList<>();
        mMovieListStorage = new ArrayList<>();
        mMovieListUploaded = new ArrayList<>();

        for(int i = 0; i < files.length; i++) {
            String day = "OLDER";
            String name = files[i].getName().replaceAll(".png", "").split("_")[0];
            try {
                if (DateUtils.isToday(Long.parseLong(name))) {
                    day = "TODAY";
                } else if (DateUtils.isToday(Long.parseLong(name) + DateUtils.DAY_IN_MILLIS)){
                    day = "YESTERDAY";
                } else {
                }
            } catch (Exception e) {
            }

            Capture movie = new Capture(name, day, false);
            if (mMovieListStorage.contains(movie)) {
                continue;
            }

            mMovieListStorage.add(movie);
        }

        for(int i = 0; i < filesUploaded.length; i++) {
            String day = "OLDER";
            String name = filesUploaded[i].getName().replaceAll(".png", "").split("_")[0];
            try {
                if (DateUtils.isToday(Long.parseLong(name))) {
                    day = "TODAY";
                } else if (DateUtils.isToday(Long.parseLong(name) + DateUtils.DAY_IN_MILLIS)){
                    day = "YESTERDAY";
                } else {
                }
            } catch (Exception e) {
            }

            Capture movie = new Capture(name, day, true);
            if (mMovieListUploaded.contains(movie)) {
                continue;
            }

            mMovieListUploaded.add(movie);
        }

        mMovieList.addAll(mMovieListStorage);
        mMovieList.addAll(mMovieListUploaded);
//        if (false) {
//            mMovieListUploaded.add(movie);
//        } else {
//            mMovieListStorage.add(movie);
//        }

        int position = getArguments().getInt("POSITION");

        switch (position) {
            case 0:
                setAdapterByDay();
                break;
            case 1:
                setAdapterByDayStorage();
                break;
            case 2:
                setAdapterByDayUploaded();
                break;
//            case 3:
//                setAdapterWithGridLayout();
//                break;
//            case 4:
//                setAdapterByDay();
//                break;
        }

        mSectionedRecyclerAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mSectionedRecyclerAdapter);
    }

    private void setAdapterByDay() {
        this.movieComparator = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
        Collections.sort(mMovieList, Collections.reverseOrder(movieComparator));
//        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new CaptureAdapterByDay(getActivity(), mMovieList);
    }

    private void setAdapterByDayStorage() {
        this.movieComparator = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
        Collections.sort(mMovieListStorage, Collections.reverseOrder(movieComparator));
//        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new CaptureAdapterByDay(getActivity(), mMovieListStorage);
    }

    private void setAdapterByDayUploaded() {
        this.movieComparator = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
        Collections.sort(mMovieListUploaded, Collections.reverseOrder(movieComparator));
//        Collections.sort(mMovieList, movieComparator);
        mSectionedRecyclerAdapter = new CaptureAdapterByDay(getActivity(), mMovieListUploaded);
    }

//    private void setAdapterByName() {
//        this.movieComparator = (o1, o2) -> o1.getTitle().compareTo(o2.getTitle());
//        Collections.sort(mMovieList, movieComparator);
//        mSectionedRecyclerAdapter = new CaptureAdapterByTitle(mMovieList);
//    }
//
//    private void setAdapterByGenre() {
//        this.movieComparator = (o1, o2) -> o1.getGenre().compareTo(o2.getGenre());
//        Collections.sort(mMovieList, movieComparator);
//        mSectionedRecyclerAdapter = new CaptureAdapterByGenre(mMovieList);
//    }
//
//    private void setAdapterByDecade() {
//        this.movieComparator = (o1, o2) -> o1.getYear() - o2.getYear();
//        Collections.sort(mMovieList, movieComparator);
//        mSectionedRecyclerAdapter = new CaptureAdapterByDecade(mMovieList);
//    }

//    private void setAdapterWithGridLayout() {
//        setAdapterByGenre();
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        mSectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
//    }

    @Override
    public void onItemClicked(Capture capture) {
//        int position = getArguments().getInt("POSITION");
//        int index = 0;
//        switch (position) {
//            case 0:
////                setAdapterByDay();
//                index = mMovieList.indexOf(movie);
//                break;
//            case 1:
////                setAdapterByDayStorage();
//                index = mMovieListStorage.indexOf(movie);
//                break;
//            case 2:
////                setAdapterByDayUploaded
//                index = mMovieListUploaded.indexOf(movie);
//                break;
////            case 3:
////                setAdapterWithGridLayout();
////                break;
////            case 4:
////                setAdapterByDay();
////                break;
//        }


//        mMovieList.remove(movie);

//        mSectionedRecyclerAdapter.notifyItemRemovedAtPosition(index);

        Intent intent = new Intent(getActivity(), EditingActivity.class);
        intent.putExtra(GROUP_NAME, capture.getTitle().split("_")[0]);
        startActivity(intent);
    }

    @Override
    public void onSubheaderClicked(int position) {
        if (mSectionedRecyclerAdapter.isSectionExpanded(mSectionedRecyclerAdapter.getSectionIndex(position))) {
            mSectionedRecyclerAdapter.collapseSection(mSectionedRecyclerAdapter.getSectionIndex(position));
        } else {
            mSectionedRecyclerAdapter.expandSection(mSectionedRecyclerAdapter.getSectionIndex(position));
        }
    }

    public void onFabClick() {
//        NewCaptureDialogFragment newMovieDialogFragment = new NewCaptureDialogFragment();
//        newMovieDialogFragment.setTargetFragment(this, 1);
//        newMovieDialogFragment.show(getFragmentManager(), "newMovie");
    }

//    @Override
//    public void onCaptureCreated(Capture capture) {
//        for (int i = 0; i < mMovieList.size(); i++) {
//            if (movieComparator.compare(mMovieList.get(i), capture) >= 0) {
//                mMovieList.add(i, capture);
//                mSectionedRecyclerAdapter.notifyItemInsertedAtPosition(i);
//                return;
//            }
//        }
//        mMovieList.add(mMovieList.size(), capture);
//        mSectionedRecyclerAdapter.notifyItemInsertedAtPosition(mMovieList.size() - 1);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_expand_all_sections:
//                mSectionedRecyclerAdapter.expandAllSections();
//                break;
//            case R.id.action_collapse_all_sections:
//                mSectionedRecyclerAdapter.collapseAllSections();
//                break;
//        }
//
//        return true;
//    }
}
