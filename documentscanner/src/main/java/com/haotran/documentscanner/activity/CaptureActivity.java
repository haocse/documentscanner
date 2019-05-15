package com.haotran.documentscanner.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.fragments.CaptureResultFragment;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class CaptureActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 101;
    ViewPager mViewPager;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        LinearLayout toobar = (LinearLayout) findViewById(R.id.toolbar);
        toobar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ((AppCompatTextView)findViewById(R.id.tvTitleToolbar)).setText("Capture");
        setStatusBarColor(R.color.colorPrimaryDark);

        //ViewPager
        mViewPager = findViewById(R.id.viewPager);
        setupViewPager(mViewPager);
        ((TabLayout)findViewById(R.id.tabLayout)).setupWithViewPager(mViewPager);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });

    }
    void startScan() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private void setupViewPager(ViewPager viewPager) {
        this.adapter = new Adapter(getSupportFragmentManager());
        this.adapter.addFragment(new CaptureResultFragment(), "All");
        this.adapter.addFragment(new CaptureResultFragment(), "Storage");
        this.adapter.addFragment(new CaptureResultFragment(), "Uploaded");
//        this.adapter.addFragment(new CaptureResultFragment(), "Grid");
//        this.adapter.addFragment(new CaptureResultFragment(), "By Day");
        viewPager.setAdapter(this.adapter);
    }


    public void setStatusBarColor(int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                if(null != data && null != data.getExtras()) {
                    ArrayList<String> fileNames = data.getExtras().getStringArrayList(ScanConstants.SCANNED_RESULT);
                    String filePath = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, getBaseContext()).getPath();
                    Bitmap baseBitmap = ScanUtils.decodeBitmapFromFile(filePath, fileNames.get(0));
//                    scannedImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    scannedImageView.setImageBitmap(baseBitmap);

                    ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
                    baseBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
                    byte[] imageData = imageByteArray.toByteArray();

                    setDpi(imageData, 300);
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                    String filename = System.currentTimeMillis() + ".png";

//                    String filename = "300dpi.png";
//                    try {
//                        File mPath = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, getBaseContext());
//                        File file = new File(mPath, filename);
//                        FileOutputStream fileOutputStream = new FileOutputStream(file);
//                        fileOutputStream.write(imageData);
//                        fileOutputStream.close();
//                    } catch (Exception e) {
//                        Log.e(">>>", e.getMessage());
//                    }

                    File mPath = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, getBaseContext());

                    File[] list = mPath.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            return file.getName().endsWith(".png");
                        }
                    });

                    Log.d(">>>", list.length + "");
                    for (int i = 0; i < list.length; i++) {
                        Log.d(">>>", list[i].getName());
                    }
                }
            } else if(resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }

    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
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

        void addFragment(Fragment fragment, String title) {
            Bundle args = new Bundle();
            args.putInt("POSITION", mFragmentList.size());
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
