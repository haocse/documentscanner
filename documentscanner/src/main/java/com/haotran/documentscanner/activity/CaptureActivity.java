package com.haotran.documentscanner.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.haotran.documentscanner.R;

public class CaptureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        LinearLayout toobar = (LinearLayout) findViewById(R.id.toolbar);
        toobar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }
}
