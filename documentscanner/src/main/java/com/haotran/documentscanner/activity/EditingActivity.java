package com.haotran.documentscanner.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.fragments.CaptureResultFragment;
import com.haotran.documentscanner.fragments.EditingFragment;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.haotran.documentscanner.fragments.CaptureResultFragment.GROUP_NAME;

public class EditingActivity extends AppCompatActivity {
    ViewPager mViewPager;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        LinearLayout toobar = findViewById(R.id.toolbar);
        toobar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ((AppCompatTextView)findViewById(R.id.tvTitleToolbar)).setText("Review");
//        tabLayout = ((TabLayout)findViewById(R.id.tabLayout));

        String groupName = Objects.requireNonNull(getIntent().getExtras()).getString(GROUP_NAME);
        //ViewPager
        mViewPager = findViewById(R.id.viewPager);
        setupViewPager(mViewPager, groupName);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditingActivity.this, UploadActivity.class);
                intent.putExtra(GROUP_NAME, groupName);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager, String groupName) {
        this.adapter = new Adapter(getSupportFragmentManager());
        // find files that match with name
        // Find in uploaded folder..
        File dir = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, getBaseContext());
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().split("_")[0].equals(groupName);
            }
        });

        File dirUploaded = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.UPLOADED_IMAGE_DIR, getBaseContext());
        File[] filesUploaded = dirUploaded.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().split("_")[0].equals(groupName);
            }
        });

        int length = files.length + filesUploaded.length;

        for (int i = 0; i < files.length; i++) {
            this.adapter.addFragment(new EditingFragment(), files[i].getName(), length, ScanConstants.RAW_IMAGE_DIR);
        }
        for (int i = 0; i < filesUploaded.length; i++) {
            this.adapter.addFragment(new EditingFragment(), filesUploaded[i].getName(), length, ScanConstants.UPLOADED_IMAGE_DIR);
        }
        ((TextView)findViewById(R.id.page)).setText(files.length + "");
//        this.adapter.addFragment(new EditingFragment(), "1"); // add position 1
//        this.adapter.addFragment(new EditingFragment(), "2");
//        this.adapter.addFragment(new EditingFragment(), "3");
//        this.adapter.addFragment(new CaptureResultFragment(), "Grid");
//        this.adapter.addFragment(new CaptureResultFragment(), "By Day");
        viewPager.setAdapter(this.adapter);
    }

    private static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title, int length, String folder) {
            Bundle args = new Bundle();
            args.putInt("POSITION", mFragmentList.size());
            args.putString("TITLE", title);
            args.putInt("SIZE", length);
            args.putString("FOLDER", folder);

            fragment.setArguments(args);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
