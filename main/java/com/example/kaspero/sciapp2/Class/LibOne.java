package com.example.kaspero.sciapp2.Class;

import android.graphics.Bitmap;
import android.util.Log;

import boofcv.android.ConvertBitmap;
import boofcv.struct.image.GrayU8;

/**
 * Created by kaspero on 08.01.2017.
 */

public class LibOne {

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LibOne(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public Bitmap basicEdit(){

        GrayU8 grayU8 = new GrayU8();
        Log.v("DONKEY","COS DZIALA");


        return bitmap;
    }


}
