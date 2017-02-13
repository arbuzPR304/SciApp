package com.example.kaspero.sciapp2.ToolsCV;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import static android.R.attr.y;

/**
 * Created by kaspero on 11.01.2017.
 */
public class ToolsCV {
    private static ToolsCV ourInstance = new ToolsCV();

    Bitmap bitmapHelp= null;

    public Bitmap getBitmapHelp() {
        return bitmapHelp;
    }

    public void setBitmapHelp(Bitmap bitmapHelp) {
        this.bitmapHelp = bitmapHelp;
    }

    public static ToolsCV getInstance() {
        return ourInstance;
    }

    private ToolsCV() {
    }

    private void galleryAddPic(File f, Activity activity) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
    }

    // SOURCE http://stackoverflow.com/questions/7887078/android-saving-file-to-external-storage/7887114#7887114
    public void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/SciApp");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
