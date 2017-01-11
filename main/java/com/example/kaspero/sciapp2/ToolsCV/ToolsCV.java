package com.example.kaspero.sciapp2.ToolsCV;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by kaspero on 11.01.2017.
 */
public class ToolsCV {
    private static ToolsCV ourInstance = new ToolsCV();

    public static ToolsCV getInstance() {
        return ourInstance;
    }

    private ToolsCV() {
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
