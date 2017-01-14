package com.example.kaspero.sciapp2.Class;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.ToolsCV.ToolsCV;



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
    public void findColorBoof(final ImageView inputPhoto,final TextView progressView){

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
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                progressView.setVisibility(TextView.VISIBLE);
                progressView.setText("" + progress[0] + " %");

            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                setOutputMarker(bitmap.copy(Bitmap.Config.ARGB_8888,true));
                Bitmap input =  bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap output = Bitmap.createBitmap(input.getWidth(),input.getHeight(), Bitmap.Config.ARGB_8888);
                Bitmap outputWithMarker = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                int HR,HG,HB,LR,LG,LB;

                HR = Options.getInstance().getHigh_R();
                HG = Options.getInstance().getHigh_G();
                HB = Options.getInstance().getHigh_B();

                LR = Options.getInstance().getLow_R();
                LG = Options.getInstance().getLow_G();
                LB = Options.getInstance().getLow_B();

                for (int i=0;i<input.getHeight();i++){
                    for(int j=0;j<input.getWidth();j++){
                        int pixel = input.getPixel(j,i);

                        if((Color.red(pixel)>=LR && Color.green(pixel)>= LG && Color.blue(pixel)>= LB)
                                && (Color.red(pixel)<=HR && Color.green(pixel)<= HG && Color.blue(pixel)<= HB)){
                            output.setPixel(j,i,Color.rgb(255,255,255));
                        }
                        else {output.setPixel(j,i,Color.rgb(0,0,0));}
                    }
                }

                int whiteColor =  Color.rgb(255,255,255);
                int blackColor =  Color.rgb(0,0,0);
                TypeOfPixel type = TypeOfPixel.START;

                for(int i= 1; i<output.getHeight()-1;i++){
                    for(int j=1;j<output.getWidth()-1;j++){
                        int [] tabPixel =  new int [9];
                        tabPixel[0] = output.getPixel(j-1,i-1);
                        tabPixel[1] = output.getPixel(j,i-1);
                        tabPixel[2] = output.getPixel(j+1-1,i-1);

                        tabPixel[3] = output.getPixel(j-1,i);
                        tabPixel[4] = output.getPixel(j,i);
                        tabPixel[5] = output.getPixel(j+1,i);

                        tabPixel[6] = output.getPixel(j-1,i+1);
                        tabPixel[7] = output.getPixel(j,i+1);
                        tabPixel[8] = output.getPixel(j+1,i+1);
                        int numberWhite = 0;
                        int numberBlack = 0;


                        for (int a =0 ;a<tabPixel.length;a++){
                            if (Color.rgb(Color.red(tabPixel[a]),Color.green(tabPixel[a]),Color.blue(tabPixel[a])) == whiteColor){
                                numberWhite++;
                            }else if (Color.rgb(Color.red(tabPixel[a]),Color.green(tabPixel[a]),Color.blue(tabPixel[a])) == blackColor){
                                numberBlack++;
                            }
                        }
                        Log.v("DONKEY", "white : "+ numberWhite+" black :"+numberBlack);

                        if(numberWhite == 0){
                            continue;
                        }else if (numberBlack == 0){
                            outputWithMarker.setPixel(j, i, Color.rgb(0, 100, 0));
                        }else if(numberBlack > 0 && numberWhite > 0){
                            outputWithMarker.setPixel(j,i,Color.rgb(50, 255, 0));
                        }

                    }
                    publishProgress((int) ((i / (float) output.getHeight()) * 100));
                }

                return outputWithMarker;

            }
        }

        MarkerClass markerClass = new MarkerClass();
        markerClass.execute();
    }


}
