package com.example.kaspero.sciapp2.Class;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.ToolsCV.ToolsCV;

import boofcv.alg.color.ColorHsv;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.android.ConvertBitmap;
import boofcv.android.ImplConvertBitmap;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import georegression.metric.UtilAngle;



/**
 * Created by kaspero on 08.01.2017.
 */

public class LibOne {

    private static Bitmap bitmap =null;
    private Bitmap bitmapedit=null;

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


    /**
     * FUNCTION FIND COLOR ON BITMAP AND SHOW MARKER
     * CV TOOL IS BOOFCV
     *
     * */
    public void findColorBoof(final ImageView inputPhoto, final TextView progressView,
                              final Button button1,
                              final Button button2,
                              final Button button3,
                              final Button button4){

        class MarkerClass extends AsyncTask<Void, Integer, Bitmap> {

            private Bitmap outputMarker=null;

            public MarkerClass() {
            }

            public Bitmap getOutputMarker() {
                if (outputMarker== null){
                    return bitmap;
                }else
                    return outputMarker;
            }

            public void setOutputMarker(Bitmap outputMarker) {
                this.outputMarker = outputMarker;
            }

            @Override
            protected void onPostExecute(Bitmap marker) {
                super.onPostExecute(marker);
                inputPhoto.setImageBitmap(marker);
                setOutputMarker(marker.copy(Bitmap.Config.ARGB_8888,true));
                if(Options.getInstance().getSaveBitmap())
                    ToolsCV.getInstance().saveImage(marker);
                progressView.setVisibility(TextView.INVISIBLE);
                button1.setEnabled(true);
                button2.setEnabled(true);
                button3.setEnabled(true);
                button4.setEnabled(true);


            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                progressView.setVisibility(TextView.VISIBLE);
                progressView.setText("" + progress[0] + " %");

            }


            @Override
            protected Bitmap doInBackground(Void... params) {
                publishProgress(1);
                //TODO SET UP IN OPTIONS TOLERANT VALUE

                float tolerant = 0.1f;

                /**GET OPTION INPUT CONVERT RGB TO HSV*/
                float [] hsv = new float[3];
                ColorHsv.rgbToHsv(Options.getInstance().getLow_R(),Options.getInstance().getLow_G(),Options.getInstance().getLow_B(), hsv);
                publishProgress(2);

                /**MAKE PLANAR OBJECT FROM (@param bitmapedit) AND hsvBitmap SI*/
                Planar<GrayF32> input = ConvertBitmap.bitmapToMS(bitmapedit, null, GrayF32.class, null);
                publishProgress(3);
                Planar<GrayF32> hsvBitmap = input.createSameShape();
                ImplConvertBitmap.bitmapToMultiRGB_F32(bitmapedit,input);
                publishProgress(4);
                ColorHsv.rgbToHsv_F32(input,hsvBitmap);
                publishProgress(5);

                /**GET HS FROM @param hsvBitmap */
                GrayF32 H = hsvBitmap.getBand(0);
                GrayF32 S = hsvBitmap.getBand(1);

                /**CALCULATE TOLERANT DISTANCE IN HSV VALUE*/
                float maxDist2 = tolerant*tolerant;

                // Adjust the relative importance of Hue and Saturation.
                // Hue has a range of 0 to 2*PI and Saturation from 0 to 1
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
                            output.setPixel(x,y,Color.rgb(255,255,255));
                        }
                    }
                    if((int) ((y / (float)hsvBitmap.height) * 100)/2>5)
                        {publishProgress((int) ((y / (float) hsvBitmap.height) * 100)/2);}

                }
                /**INIT */
                GrayU8 inputGaussian = ConvertBitmap.bitmapToGray(output, (GrayU8)null, null);
                GrayU8 outputGausian = inputGaussian.createSameShape();

                /**INIT radius matix region*/
                int radius = 1;

                /**APPLY GAUSIAN*/
                GBlurImageOps.gaussian(inputGaussian,outputGausian,1,radius,null);

                /**INPUT GAUSSIAN INTO BITMAP*/
                output = ConvertBitmap.grayToBitmap(outputGausian, Bitmap.Config.ARGB_8888);

                /**SET MARKER ON BITMAP*/

                Bitmap outputMarker = bitmapedit.copy(Bitmap.Config.ARGB_8888,true);
                for (int i = 0; i < output.getHeight(); i++) {
                    for (int j = 0; j < output.getWidth(); j++) {
                        int pixTemp = output.getPixel(j, i);
                        int redValue = Color.red(pixTemp);
                        int blueValue = Color.blue(pixTemp);
                        int greenValue = Color.green(pixTemp);
                        if (redValue > 240 && blueValue > 240 && greenValue > 240) {
                            outputMarker.setPixel(j, i, Color.rgb(0, 100, 0));
                        } else if (redValue > 100 && blueValue > 100 && greenValue > 100) {
                            outputMarker.setPixel(j, i, Color.rgb(50, 255, 0));
                        }
                    }
                    publishProgress((int) ((i / (float) output.getHeight()) * 100)/2+50);
                }
            return outputMarker;

            }
        }

        MarkerClass markerClass = new MarkerClass();
        markerClass.execute();
    }


}
