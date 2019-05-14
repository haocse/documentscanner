package com.haotran.documentscanner.interfaces;

import android.graphics.Bitmap;

import com.haotran.documentscanner.enums.ScanHint;

/**
 * Interface between activity and surface view
 */

public interface IScanner {
    void displayHint(ScanHint scanHint);
    void onPictureClicked(Bitmap bitmap);
    void onNope(long name);
    void onYep(long name);
}
