package com.haotran.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.haotran.capture.activity.CaptureActivity;
import com.haotran.demo.R;
//import com.haotran.capture.activity.CaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private static final int CAPTURE_REQUEST_CODE = 101;
    private ImageView scannedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannedImageView = findViewById(R.id.scanned_image);

//        startScan();
        startCapture();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startScan();
//                startCapture();
            }
        });
    }

    private void startCapture() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);


    }

//    private void startScan() {
//        Intent intent = new Intent(this, ScanActivity.class);
//        startActivityForResult(intent, REQUEST_CODE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
    }

    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
    }
}
