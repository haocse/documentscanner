//package com.haotran.documentscanner.fragments;
//
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.haotran.documentscanner.R;
//import com.haotran.capture.activity.adapters.CustomAdapter;
//
//public class MyDialogFragment extends DialogFragment {
//    private RecyclerView mRecyclerView;
//    private CustomAdapter adapter;
//    // this method create view for your Dialog
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //inflate layout with recycler view
//        View v = inflater.inflate(R.layout.wheel_view, container, false);
//        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        //setadapter
//        initDataset();
//        CustomAdapter adapter = new CustomAdapter(mDataset);
//        mRecyclerView.setAdapter(adapter);
//        //get your recycler view and populate it.
//        return v;
//    }
//
//    private static final int DATASET_COUNT = 60;
//    protected <String, String> mDataset;
//    private void initDataset() {
//        mDataset = new String[DATASET_COUNT];
//        for (int i = 0; i < DATASET_COUNT; i++) {
//            mDataset[i] = "This is element #" + i;
//        }
//    }
//}