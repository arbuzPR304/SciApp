package com.example.kaspero.sciapp2.Options;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.Log;

import com.example.kaspero.sciapp2.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.InputStream;


/**
 * Created by kaspero on 27.12.2016.
 */
public class Options {
    public static enum LibsComputerVision {OPENCV,BOOF};

/**GETTER*/
    public int getHigh_RGB() {
        return high_RGB;
    }
    public int getLow_RGB() {
        return low_RGB;
    }
    public LibsComputerVision getLibsComputerVision() {
        return libsComputerVision;
    }

    public Boolean getSaveBitmap() {
        return saveBitmap;
    }

/** SETTER */
    public void setHigh_RGB(int high_R,int high_G,int high_B) {
        int test = Color.rgb(high_R,high_G,high_B);
        this.high_RGB = test;
    }

    public void setLow_RGB(int low_R,int low_G,int low_B) {
        int test = Color.rgb(low_R,low_G,low_B);
        this.low_RGB = test;
    }

    public void setLibsComputerVision(LibsComputerVision libsComputerVision) {
        this.libsComputerVision = libsComputerVision;
    }

    public void setSaveBitmap(Boolean saveBitmap) {
        this.saveBitmap = saveBitmap;
    }

    private int high_RGB;
    private int low_RGB;
    private LibsComputerVision libsComputerVision;
    private Boolean saveBitmap;

    private static Options ourInstance = new Options();
    public static Options getInstance() {
        return ourInstance;
    }
    private Options(){

    }

    public static void loadOptions(Context activity){

        try{
            XmlResourceParser xrp  = activity.getResources().getXml(R.xml.lastoptions);
            Log.v("DONKEY","WORK");

        }catch (Exception e) {
            e.printStackTrace();
            Log.v("DONKEY","ERROR");}

    }
}
