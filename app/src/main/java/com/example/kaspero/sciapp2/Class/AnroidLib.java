package com.example.kaspero.sciapp2.Class;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.ToolsCV.ToolsCV;

import static android.R.id.button1;


/**
 * Created by kaspero on 08.01.2017.
 */

public class AnroidLib {
    enum  TypeOfPixel {START,SAME, LINE, BODY};
    private static Bitmap bitmap =null;
    private Bitmap bitmapedit=null;

    static public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public AnroidLib(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.bitmapedit =bitmap;
    }




    /**
     * FUNCTION FIND COLOR ON BITMAP AND SHOW MARKER
     * CV TOOL IS BOOFCV
     *
     * */
    public void findColorBoof(final ImageView inputPhoto,final TextView progressView,
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
                Bitmap input =  bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputWithMarker = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                int HR,HG,HB,LR,LG,LB;

                HR = Options.getInstance().getHigh_R();
                HG = Options.getInstance().getHigh_G();
                HB = Options.getInstance().getHigh_B();

                LR = Options.getInstance().getLow_R();
                LG = Options.getInstance().getLow_G();
                LB = Options.getInstance().getLow_B();

                for(int i= 1; i<input.getHeight()-1;i++){
                    for(int j=1;j<input.getWidth()-1;j++){
                        int [] tabPixel =  new int [9];
                        tabPixel[0] = input.getPixel(j-1,i-1);
                        tabPixel[1] = input.getPixel(j,i-1);
                        tabPixel[2] = input.getPixel(j+1-1,i-1);

                        tabPixel[3] = input.getPixel(j-1,i);
                        tabPixel[4] = input.getPixel(j,i);
                        tabPixel[5] = input.getPixel(j+1,i);

                        tabPixel[6] = input.getPixel(j-1,i+1);
                        tabPixel[7] = input.getPixel(j,i+1);
                        tabPixel[8] = input.getPixel(j+1,i+1);
                        int numberInSet = 0;
                        int numberOutSet = 0;

                        for (int a =0 ;a<tabPixel.length;a++){
                            if ((Color.red(tabPixel[a])>=LR && Color.green(tabPixel[a])>= LG && Color.blue(tabPixel[a])>= LB)
                                && (Color.red(tabPixel[a])<=HR && Color.green(tabPixel[a])<= HG && Color.blue(tabPixel[a])<= HB)){
                                numberInSet++;
                            }else{
                                numberOutSet++;
                            }
                        }
                        if(numberInSet == 0){
                            continue;
                        }else if (numberOutSet == 0){
                            outputWithMarker.setPixel(j, i, Color.rgb(0, 100, 0));
                        }else if(numberInSet > 0 && numberOutSet > 0){
                            outputWithMarker.setPixel(j,i,Color.rgb(50, 255, 0));
                        }
                    }
                    publishProgress((int) ((i / (float) input.getHeight()) * 100));
                }
                return outputWithMarker;
            }
        }
        MarkerClass markerClass = new MarkerClass();
        markerClass.execute();
    }
}
