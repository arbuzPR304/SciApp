package com.example.kaspero.sciapp2.Options;
import android.content.Context;
import android.content.res.XmlResourceParser;
import com.example.kaspero.sciapp2.R;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by kaspero on 27.12.2016.
 * Class set options from input and last from xml file
 *
 */
public class Options {
    public static enum LibsComputerVision {OPENCV,BOOF,OTHER};

    public int getHigh_R() {
        return high_R;
    }

    public void setHigh_R(int high_R) {
        this.high_R = high_R;
    }

    public int getHigh_G() {
        return high_G;
    }

    public void setHigh_G(int high_G) {
        this.high_G = high_G;
    }

    public int getHigh_B() {
        return high_B;
    }

    public void setHigh_B(int high_B) {
        this.high_B = high_B;
    }

    public int getLow_R() {
        return low_R;
    }

    public void setLow_R(int low_R) {
        this.low_R = low_R;
    }

    public int getLow_G() {
        return low_G;
    }

    public void setLow_G(int low_G) {
        this.low_G = low_G;
    }

    public int getLow_B() {
        return low_B;
    }

    public void setLow_B(int low_B) {
        this.low_B = low_B;
    }

/**GETTER*/

    public LibsComputerVision getLibsComputerVision() {
        return libsComputerVision;
    }

    public Boolean getSaveBitmap() {
        return saveBitmap;
    }

/** SETTER */

    public void setLibsComputerVision(LibsComputerVision libsComputerVision) {
        this.libsComputerVision = libsComputerVision;
    }

    public void setSaveBitmap(Boolean saveBitmap) {
        this.saveBitmap = saveBitmap;
    }

    private int high_R;
    private int high_G;
    private int high_B;

    private int low_R;
    private int low_G;
    private int low_B;

    private LibsComputerVision libsComputerVision;
    private Boolean saveBitmap;

    private static Options ourInstance = new Options();
    public static Options getInstance() {
        return ourInstance;
    }
    private Options(){

    }
//source https://developer.android.com/reference/org/xmlpull/v1/XmlPullParser.html
    public  void loadOptions(Context context){
        String temp="null";
        String tempName = "null";
        int hr =0, hg=0, hb=0, lr=0,lg=0,lb=0;

        try{
            XmlResourceParser xpp=context.getResources().getXml(R.xml.lastoptions);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    tempName = xpp.getName();
                }
                switch (tempName){
                    case "HighR":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            hr = Integer.parseInt(temp);
                            setHigh_R(hr);
                        }
                        break;
                    case "HighG":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            hg = Integer.parseInt(temp);
                            setHigh_G(hg);
                        }
                        break;
                    case "HighB":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            hb = Integer.parseInt(temp);
                            setHigh_B(hb);

                        }
                        break;
                    case "LowR":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            lr = Integer.parseInt(temp);
                            setLow_R(lr);
                        }
                        break;
                    case "LowG":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            lg = Integer.parseInt(temp);
                            setLow_G(lg);
                        }
                        break;
                    case "LowB":
                        if(eventType == XmlPullParser.TEXT){
                            temp = xpp.getText();
                            lb = Integer.parseInt(temp);
                            setLow_B(lb);
                        }
                        break;
                    case "LibsComputerVision":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            setLibsComputerVision(LibsComputerVision.valueOf(temp));
                        }
                        break;
                    case "SaveBitmap":
                        if(eventType == XmlPullParser.TEXT) {
                            temp = xpp.getText();
                            setSaveBitmap(Boolean.parseBoolean(temp));
                        }
                        break;
                }

                eventType = xpp.next();
            }

        }catch (Exception e){
            e.printStackTrace();
            setHigh_R(hr);setHigh_B(hb);setHigh_G(hg);
            setLow_B(hb);setLow_G(hg);setLow_R(hr);
            setLibsComputerVision(LibsComputerVision.valueOf("OPENCV"));
            setSaveBitmap(true);
        }
    }
}
