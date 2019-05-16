package com.haotran.documentscanner.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.haotran.documentscanner.fragments.CaptureResultFragment.GROUP_NAME;

public class UploadActivity extends AppCompatActivity {
    ViewPager mViewPager;
    String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        LinearLayout toobar = findViewById(R.id.toolbar);
        toobar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ((AppCompatTextView)findViewById(R.id.tvTitleToolbar)).setText("Upload");

        groupName = Objects.requireNonNull(getIntent().getExtras()).getString(GROUP_NAME);

        findViewById(R.id.ivBack).setOnClickListener(v -> {
            startReviewActivity();
        });

        findViewById(R.id.addDebtor).setOnClickListener(v -> {
            // show pop-up for choosing debtor...
//            Toast.makeText(UploadActivity.this, "sending...", Toast.LENGTH_SHORT).show();
//                160164@cisboxmail.com
            // to pdf
//            Log.d(">>>", "Sending...");
        });

        findViewById(R.id.upload).setOnClickListener(v -> {
            // compress file


            File dir = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.RAW_IMAGE_DIR, getBaseContext());
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().split("_")[0].equals(groupName);
                }
            });

            File dirUploaded = ScanUtils.getBaseDirectoryFromPathString(ScanConstants.UPLOADED_IMAGE_DIR, getBaseContext());
            // check if it's already uploaded///
            File file;
            if (files.length == 0) {
                // check in uploadedFolder..
                files = dirUploaded.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().split("_")[0].equals(groupName);
                    }
                });
            }
            file = files[0];

//            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
//            byte[] b = baos.toByteArray();
//            String base64 = Base64.encodeToString(b, Base64.DEFAULT);

//            String base64 = getBase64FromPath2(file.getAbsolutePath());
            String base64 = getBase64FromPath2(file.getAbsolutePath());

            Log.d(">>>", file.getAbsolutePath());
            Log.d(">>>", base64);

            // if it's in uploaded folder -> ignore these lines.
            if (files.length != 0) // make sure it's in raw folder..
                for (int i = 0; i < files.length; i++) {
                    File f = files[i]; //src.
                    File fd = new File(ScanUtils.getBaseDirectoryFromPathString(ScanConstants.UPLOADED_IMAGE_DIR, getBaseContext()), f.getName());
                    try {
                        copy(f,fd); //
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    f.delete();

                }

            // send an email here
            // Default to pass.
             // move files to UPLOADED_IMAGE_DIR...
            finish();


        });
    }

    public void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    private void startReviewActivity() {
        Intent intent = new Intent(UploadActivity.this, EditingActivity.class);
        intent.putExtra(GROUP_NAME, groupName);
        UploadActivity.this.startActivity(intent);

        UploadActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(this, "back...", Toast.LENGTH_SHORT).show();
//        finish();
        startReviewActivity();
    }

    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            InputStream inputStream = new FileInputStream(path);//You can get an inputStream using any IO API
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            bytes = output.toByteArray();
            base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64;

    }

    public static String getBase64FromPath2(String path) {
        String base64 = "";
        try {/*from w  w w.j a v  a2 s  .  c  om*/
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length,
                    Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }


}
