package com.haotran.testing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.haotran.documentscanner.R;
import com.haotran.documentscanner.activity.ScanActivity;
import com.haotran.documentscanner.constants.ScanConstants;
import com.haotran.documentscanner.util.ScanUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private static final int CAPTURE_REQUEST_CODE = 101;
    private ImageView scannedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scannedImageView = findViewById(R.id.scanned_image);

        startScan();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
//                startCapture();
            }
        });
    }

    private void startCapture() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void startScan() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                if(null != data && null != data.getExtras()) {
                    String filePath = data.getExtras().getString(ScanConstants.SCANNED_RESULT);
                    Bitmap baseBitmap = ScanUtils.decodeBitmapFromFile(filePath, ScanConstants.IMAGE_NAME);
                    scannedImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    scannedImageView.setImageBitmap(baseBitmap);

                    ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
                    baseBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
                    byte[] imageData = imageByteArray.toByteArray();
                    setDpi(imageData, 300);

                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String filename = "300dpi.png";
                    try {
                        File file = new File(path, filename);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(imageData);
                        fileOutputStream.close();
                    } catch (Exception e) {
                        Log.e(">>>", e.getMessage());
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
}
