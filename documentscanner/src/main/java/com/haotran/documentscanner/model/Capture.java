package com.haotran.documentscanner.model;

import android.support.annotation.Nullable;
import android.util.Log;

public class Capture {

    private String title;

    private String day;

    private boolean uploaded;


    public Capture(String title, String day, boolean uploaded) {
        this.title = title;
        this.day = day;
        this.uploaded = uploaded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Capture{" +
                "title='" + title + '\'' +
                '}';
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Capture capture = (Capture) object;
            if (this.title.split("_")[0].equals(capture.getTitle().split("_")[0])) {
                Log.d(">>>", "equalllll");
                result = true;
            }
        }
        return result;
    }
}
