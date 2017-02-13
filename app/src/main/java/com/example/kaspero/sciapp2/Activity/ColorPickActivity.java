package com.example.kaspero.sciapp2.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.R;
import com.example.kaspero.sciapp2.ToolsCV.ToolsCV;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ColorPickActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 500;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private ImageView ColorImage;
    private Button selectedBTN;


    private View mControlsView;


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
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color_pick);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        selectedBTN =(Button) findViewById(R.id.selectedBTN);

        final Context c = this;
        selectedBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,OptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggle();
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


        ColorImage = (ImageView) findViewById(R.id.ColorImage);
        ColorImage.setImageBitmap(ToolsCV.getInstance().getBitmapHelp());
        ColorImage.setOnTouchListener(new View.OnTouchListener() {

            Canvas canvas = new Canvas();
            Bitmap onTouchBitmap =null;

            @Override
            public boolean onTouch(View v, MotionEvent ev) {

                switch (ev.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        hide();
                        break;
                    case MotionEvent.ACTION_UP:
                        show();
                        break;

                }

                final int evX = (int) ev.getX();
                final int evY = (int) ev.getY();

                ImageView img = (ImageView) v;
                if(onTouchBitmap!=null)
                    img.setImageBitmap(onTouchBitmap);
                img.setDrawingCacheEnabled(true);
                Bitmap imgbmp = Bitmap.createBitmap(img.getDrawingCache());
                img.setDrawingCacheEnabled(false);


                int pxl = imgbmp.getPixel(evX, evY);
                int redComponent = Color.red(pxl);
                int greenComponent = Color.green(pxl);
                int blueComponent = Color.blue(pxl);


                Options.getInstance().setHigh_R(redComponent);
                Options.getInstance().setHigh_G(greenComponent);
                Options.getInstance().setHigh_B(blueComponent);


                Options.getInstance().setLow_R(redComponent);
                Options.getInstance().setLow_G(greenComponent);
                Options.getInstance().setLow_B(blueComponent);

                onTouchBitmap = drawCircle(imgbmp,evX,evY,canvas,img);

                return true;
            }
        });
    }

    private Bitmap drawCircle(Bitmap bitmap,int x, int y,Canvas canvas,ImageView img){

        Bitmap bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        canvas.setBitmap(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(250,199,64));
        paint.setStrokeWidth(7);
        canvas.drawCircle(x,y, 25, paint);
        img.setImageBitmap(bitmap);

        return bitmapCopy;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }



    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
