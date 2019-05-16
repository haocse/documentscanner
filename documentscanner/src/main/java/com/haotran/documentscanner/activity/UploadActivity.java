package com.haotran.documentscanner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.activity.adapters.CustomAdapter;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.util.ScanUtils;
import com.haotran.documentscanner.widget.adapter.SortAdapter;
import com.haotran.documentscanner.widget.model.SortBy;
import com.haotran.documentscanner.widget.widget.WheelView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.haotran.documentscanner.fragments.CaptureResultFragment.GROUP_NAME;

public class UploadActivity extends AppCompatActivity implements CustomAdapter.Handler {
    ViewPager mViewPager;
    String groupName;
    private CustomAdapter mAdapter;
//    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

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
//            Toast.makeText(UploadActivity.this, "popup...", Toast.LENGTH_SHORT).show();
//                160164@cisboxmail.com
            // to pdf
//            Log.d(">>>", "Sending...");
            // Call retrofit here.
            AlertDialog.Builder dialogBuilder;
            dialogBuilder = new AlertDialog.Builder(UploadActivity.this);
//                    .setTitle("Please choose");
//                    .setView(R.layout.wheel_view)

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                    .setIcon(android.R.drawable.ic_dialog_alert)
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.wheel_view, null);
            dialogBuilder.setView(dialogView);

            TextView textView = (TextView) dialogView.findViewById(R.id.textView);
//            textView.setText("test label");
            mAdapter = new CustomAdapter(this, mDataset);
            RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(mAdapter);

            WheelView wheelView = dialogView.findViewById(R.id.wheelView);
            wheelView.setWheelAdapter(new SortAdapter(getBaseContext()));
            wheelView.setSkin(WheelView.Skin.Holo);
            wheelView.setLoop(false);
            wheelView.setSelection(0);
            wheelView.setWheelData(getList());
            wheelView.setSelection(1);

            dialogView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = wheelView.getCurrentPosition();
                    Log.d(">>>", currentPosition + "");
                    alertDialog.dismiss();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(UploadActivity.this));
            if (alertDialog == null || (alertDialog != null && !alertDialog.isShowing())) alertDialog = dialogBuilder.create();
            alertDialog.show();

//            new MyDialogFragment().show(getSupportFragmentManager(), "tag");

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

                Toast.makeText(this, "Already sent!", Toast.LENGTH_SHORT).show();
                return;
                // check in uploadedFolder..
//                files = dirUploaded.listFiles(new FileFilter() {
//                    @Override
//                    public boolean accept(File pathname) {
//                        return pathname.getName().split("_")[0].equals(groupName);
//                    }
//                });
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
            Toast.makeText(this, "Your invoice was uploaded successfully.", Toast.LENGTH_SHORT).show();
            finish();


        });

        initDataset(); // for testing
//        mAdapter = new CustomAdapter(mDataset);

    }

    public String getElementByIndex(int index){
        return (String) mDataset.get( (mDataset.keySet().toArray())[ index ] );
    }

    public String getEmailByIndex( int index){
        return (String) (mDataset.keySet().toArray())[ index ];
    }

    private List<SortBy> getList() {
        List<SortBy> list = new ArrayList<>();
        for (int i = 0; i < mDataset.size(); i++) {
//            mDataset.keySet()
            /**
             * public String getElementByIndex(int index){
             *         return (String) mDataSet.get( (mDataSet.keySet().toArray())[ index ] );
             *     }
             *
             *     public String getEmailByIndex( int index){
             *         return (String) (mDataSet.keySet().toArray())[ index ];
             *     }
             */

            list.add(new SortBy(i, getElementByIndex(i), getEmailByIndex(i)));

        }

//        list.add(new SortBy(1, "name", "keyword"));
//        list.add(new SortBy(2, "name", "keyword"));
//        list.add(new SortBy(3, "name", "keyword"));
//        list.add(new SortBy(4, "name", "keyword"));
        return list;
    }

    private static final int DATASET_COUNT = 60;
    protected LinkedHashMap<String, String> mDataset;
    private void initDataset() {
        //For testing
        mDataset = new LinkedHashMap<>();
        mDataset.put("160164@cisboxmail.com","AH Pornic (160164mD)");
        mDataset.put("160288@cisboxmail.com","AS Roissy (160288mD)");
        mDataset.put("500138@cisboxmail.com","BLACK (500138mD)");
        mDataset.put("150513@cisboxmail.com","BLACK GMBH (150513mD)");
        mDataset.put("150505@cisboxmail.com","BLACK OUTLET 01 (150505mD)");
        mDataset.put("101143@cisboxmail.com","BLACK OUTLET 02 (101143mD)");
        mDataset.put("133649@cisboxmail.com","BLACK OUTLET 1 (133649mD)");
        mDataset.put("134404@cisboxmail.com","BLACK OUTLET 2 (134404mD)");
        mDataset.put("137887@cisboxmail.com","BLACK OUTLET 3 (137887mD)");
        mDataset.put("400010@cisboxmail.com","CHAI 33 (400010mD)");
        mDataset.put("143267@cisboxmail.com","COMPANY 01 (143267mD)");
        mDataset.put("143278@cisboxmail.com","COMPANY 02 (143278mD)");
        mDataset.put("143289@cisboxmail.com","COMPANY 03 (143289mD)");
        mDataset.put("143290@cisboxmail.com","COMPANY 03 (Ausgangs-RG) (143290mD)");
        mDataset.put("160313@cisboxmail.com","EH Lille (160313mD)");
        mDataset.put("141159@cisboxmail.com","MilkyWay (141159mD)");
        mDataset.put("500070@cisboxmail.com","WHITE (500070mD)");
        mDataset.put("199862@cisboxmail.com","WHITE GMBH (199862mD)");
        mDataset.put("184683@cisboxmail.com","WHITE OUTLET 01 (184683mD)");
        mDataset.put("199895@cisboxmail.com","WHITE OUTLET 02 (199895mD)");
        mDataset.put("133434@cisboxmail.com","WHITE OUTLET 1 (133434mD)");
//        for (int i = 0; i < DATASET_COUNT; i++) {
//            mDataset[i] = "This is element #" + i;
//        }
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


    @Override
    public void onItemClicked(int position) {
        Log.d(">>>", "Element " + position + " clicked.");

        //send email here.
        String email = mAdapter.getEmailByIndex(position);
        String debtor = mAdapter.getElementByIndex(position);
        Log.d(">>>", email + " clicked.");


//        UploadActivity.this.finish();

        ((TextView)findViewById(R.id.addDebtor)).setText(debtor);

        if (alertDialog.isShowing()) alertDialog.dismiss();

    }
}
