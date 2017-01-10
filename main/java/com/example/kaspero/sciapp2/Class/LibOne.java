package com.example.kaspero.sciapp2.Class;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.kaspero.sciapp2.Options.Options;

import boofcv.alg.color.ColorHsv;
import boofcv.android.ConvertBitmap;
import boofcv.android.ImplConvertBitmap;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import georegression.metric.UtilAngle;

import static android.R.id.input;

/**
 * Created by kaspero on 08.01.2017.
 */

public class LibOne {

    private static Bitmap bitmap;
    private Bitmap bitmapedit;

    static public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LibOne(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.bitmapedit =bitmap;
    }


    public Bitmap basicEdit(){

        GrayU8 grayU8 = new GrayU8();
        // Easiest way to convert a Bitmap into a BoofCV type
        GrayU8 image = ConvertBitmap.bitmapToGray(bitmapedit, (GrayU8)null, null);
        // If you are converting a sequence of images it is faster reuse a
        // previously declare image and buffer
        byte[] workBuffer = ConvertBitmap.declareStorage(bitmapedit, null);
        ConvertBitmap.bitmapToGray(bitmapedit, image, workBuffer);
        // Convert back into a Bitmap
        ConvertBitmap.grayToBitmap(image, bitmapedit, workBuffer);
        // another less efficient way
        bitmapedit = ConvertBitmap.grayToBitmap(image, Bitmap.Config.ARGB_8888);
        // Functions are also provided for multi-spectral images
        Planar<GrayF32> color = ConvertBitmap.bitmapToMS(bitmapedit, null, GrayF32.class, null);
        return bitmapedit;
    }

    public Bitmap findColorBoof(){

        float [] hsv = new float[3];
        ColorHsv.rgbToHsv(Options.getInstance().getHigh_R(),Options.getInstance().getHigh_G(),Options.getInstance().getHigh_B(), hsv);

        Planar<GrayF32> input = ConvertBitmap.bitmapToMS(bitmapedit, null, GrayF32.class, null);

        float maxDist2 = 0.2f*0.2f;

        ImplConvertBitmap.bitmapToMultiRGB_F32(bitmapedit,input);
        Planar<GrayF32> hsvBitmap = input.createSameShape();

        // Convert into HSV
        ColorHsv.rgbToHsv_F32(input,hsvBitmap);
        GrayF32 H = hsvBitmap.getBand(0);
        GrayF32 S = hsvBitmap.getBand(1);

        float adjustUnits = (float)(Math.PI/2.0);

        Bitmap output = Bitmap.createBitmap(bitmapedit.getWidth(),bitmapedit.getHeight(),Bitmap.Config.ARGB_8888);

        for( int y = 0; y < hsvBitmap.height; y++ ) {
            for( int x = 0; x < hsvBitmap.width; x++ ) {
                // Hue is an angle in radians, so simple subtraction doesn't work
                float dh = UtilAngle.dist(H.unsafe_get(x,y),hsv[0]);
                float ds = (S.unsafe_get(x,y)-hsv[1])*adjustUnits;
                // this distance measure is a bit naive, but good enough for to demonstrate the concept
                float dist2 = dh*dh + ds*ds;
                if( dist2 <= maxDist2 ) {
                    output.setPixel(x,y,Color.WHITE);
                }
            }
        }

          Log.v("DONKEY",hsvBitmap.height +" "+input.height);




        return output;
    }


}
