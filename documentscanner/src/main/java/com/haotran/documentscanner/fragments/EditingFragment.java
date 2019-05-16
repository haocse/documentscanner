package com.haotran.documentscanner.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditingFragment extends Fragment {


    public EditingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editing, container, false);
        String title = getArguments().getString("TITLE");
        int position = getArguments().getInt("POSITION");
        int size = getArguments().getInt("SIZE");
        String folder = getArguments().getString("FOLDER");
        File dir = ScanUtils.getBaseDirectoryFromPathString(folder, getActivity());
        File mPath = new File(dir, title);
        Bitmap myBitmap = BitmapFactory.decodeFile(mPath.getAbsolutePath());
        ((ImageView)view.findViewById(R.id.image)).setImageBitmap(myBitmap);

        ((TextView)view.findViewById(R.id.textView)).setText(position+1 + "/" + size);
        return view;
    }

}
