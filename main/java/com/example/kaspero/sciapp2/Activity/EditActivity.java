package com.example.kaspero.sciapp2.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaspero.sciapp2.Class.LibOne;
import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.R;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.ml.SVM.C;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    /**
     * VARIABLES
     */

    private final int RESULT_LOAD_IMAGE = 1;
    private Uri contentURI = null;
    private boolean SEARCH_BUTTON = false;
    private Bitmap editPhoto = null;
    private Bitmap editPhotoLast = null;
    private Mat imageMat;
    private Button optionButton, searchButton, infoButton, loadButton;
    private ImageView intentPhoto;
    private View mControlsView;
    private boolean mVisible;
    private TextView progresView;


    /**
     * SHOW AND HIDE fullscreen_content_controls
     */

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {

            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);

        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    /**
     * END SHOW AND HIDE fullscreen_content_controls
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        optionButton = (Button) findViewById(R.id.optionBtn);
        loadButton = (Button) findViewById(R.id.loadBtn);
        searchButton = (Button) findViewById(R.id.searchBtn);
        infoButton = (Button) findViewById(R.id.infoBtn);


        intentPhoto = (ImageView) findViewById(R.id.intentPhoto);
        progresView = (TextView) findViewById(R.id.progresView);

        progresView.setVisibility(TextView.INVISIBLE);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        optionButton.setOnClickListener(this);
        loadButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);

        if (Options.getInstance().getFromCamera()) {
            ContentResolver cr = this.getContentResolver();
            InputStream in = null;
            try {
                in = cr.openInputStream(Options.getInstance().getPhotoUriOpt());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            editPhoto = BitmapFactory.decodeStream(in, null, options);
            intentPhoto.setImageBitmap(editPhoto);
            Options.getInstance().setFromCamera(false);
            Options.getInstance().setPhotoUriOpt(null);
        }
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        if (btn == optionButton) {
            Intent intent = new Intent(this, OptionActivity.class);
            startActivity(intent);
        } else if (btn == loadButton) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
                // capture photo on Resume
            }
        } else if (btn == searchButton) {
            if (editPhoto != null) {
                searchRGB(editPhoto);
            }
        } else if (btn == infoButton) {
            if (editPhoto != null) {
                intentPhoto.setImageBitmap(editPhoto);
            }
        }
    }

    /**
     * ONLY TOGGLE DEF SIMPLY FOR HIDE AND SHOW
     */

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);

    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    /**
     * CAPTURE CHOOSEN PHOTO, NEXT TAKING URL, BITMAP IN
     * TO MEMMORY AND THE AND SET IN IMAGEVIEW
     */
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (imageReturnedIntent != null) {
                        contentURI = Uri.parse(imageReturnedIntent.getDataString());
                        ContentResolver cr = this.getContentResolver();
                        InputStream in = null;
                        try {
                            in = cr.openInputStream(contentURI);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        editPhoto = BitmapFactory.decodeStream(in, null, options);
                        intentPhoto.setImageBitmap(editPhoto);
                        if (editPhoto != null) {
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                searchButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.herro_button));
                            } else {
                                searchButton.setBackground(getResources().getDrawable(R.drawable.herro_button));
                            }
                        }

                    }
                }
                break;
        }
    }


    /**
     * RECOGNISE CV LIB [OPEN CV, ..., ...,]
     */
    public void searchRGB(Bitmap photo) {
        switch (Options.getInstance().getLibsComputerVision()) {
            case OPENCV:
                if (!OpenCVLoader.initDebug()) {
                    Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
                } else {
                    Log.d("OpenCV", "OpenCV library found inside package. Using it!");
                    mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
                }
                break;
            case BOOF:
                LibOne libOne = new LibOne(photo);
                photo = libOne.basicEdit();
                intentPhoto.setImageBitmap(photo);


                break;
        }
    }

    /**
     * OPENCV SEARCH RGB ENGINE
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    class MarkerClass extends AsyncTask<Bitmap, Integer, Bitmap> {
                        @Override
                        protected Bitmap doInBackground(Bitmap... params) {

                            /**                 INIT OF THE MATRIXS           */
                            Mat imageMat = new Mat();
                            Mat threshold = new Mat();
                            Mat finalmente = new Mat();

                            Utils.bitmapToMat(params[0], imageMat);
                            /**                 RGB ANDROID BITMAP CONVERT INTO BGR OPENCV COLOR FORMAT
                             *                  NEXT SET SCALAR  BRG VALUE
                             *                  FINALLY GaussianBlur*/

                            Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGB2BGR);
                            Bitmap result = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);
                            Bitmap result2 = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);


                            Log.v("DONKEY", "" + (Options.getInstance().getHigh_B() + " " + Options.getInstance().getHigh_G() + " " +
                                    Options.getInstance().getHigh_R()));
                            Log.v("DONKEYLow", "" + (Options.getInstance().getLow_B() + " " + Options.getInstance().getLow_G() + " " +
                                    Options.getInstance().getLow_R()));

                            Core.inRange(imageMat,
                                    new Scalar(Options.getInstance().getLow_B(),
                                            Options.getInstance().getLow_G(),
                                            Options.getInstance().getLow_R()),

                                    new Scalar(Options.getInstance().getHigh_B(),
                                            Options.getInstance().getHigh_G(),
                                            Options.getInstance().getHigh_R()),
                                    threshold);

                            GaussianBlur(threshold, finalmente, new Size(5, 5), 1, 1);

                            /**
                             *  Set marker
                             */
                            /**                 SET EVERYTHING INTO IMAGEVIEW*/
                            Utils.matToBitmap(finalmente, result);
                            Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2RGB);
                            Utils.matToBitmap(imageMat, result2);

                            for (int i = 0; i < result.getHeight(); i++) {
                                for (int j = 0; j < result.getWidth(); j++) {
                                    int pixTemp = result.getPixel(j, i);
                                    int redValue = Color.red(pixTemp);
                                    int blueValue = Color.blue(pixTemp);
                                    int greenValue = Color.green(pixTemp);

                                    if (redValue > 220 && blueValue > 220 && greenValue > 220) {
                                        result.setPixel(j, i, Color.rgb(255, 255, 255));
                                        int color = result2.getPixel(j, i);
                                        result2.setPixel(j, i,
                                                Color.argb(70,
                                                        Color.red(color),
                                                        255,
                                                        Color.blue(color)));
                                    } else if (redValue > 0 && blueValue > 0 && greenValue > 0) {
                                        result2.setPixel(j, i, Color.rgb(50, 255, 0));
                                    }
                                }
                                publishProgress((int) ((i / (float) result.getHeight()) * 100));
                            }
                            return result2;
                        }

                        @Override
                        protected void onProgressUpdate(Integer... progress) {
                            progresView.setVisibility(TextView.VISIBLE);
                            progresView.setText("" + progress[0] + " %");
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            intentPhoto.setImageBitmap(bitmap);
                            editPhotoLast = bitmap;
                            if(Options.getInstance().getSaveBitmap())
                                SaveImage(bitmap);
                            progresView.setVisibility(TextView.INVISIBLE);
                        }
                    }
                    new MarkerClass().execute(editPhoto);

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
// http://stackoverflow.com/questions/7887078/android-saving-file-to-external-storage/7887114#7887114
    private void SaveImage(Bitmap finalBitmap) {

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


