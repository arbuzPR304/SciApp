package com.example.kaspero.sciapp2.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.R;

import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class OptionActivity extends AppCompatActivity {

    CheckBox openCV;
    CheckBox boof;
    CheckBox other;
    CheckBox saveCheckBTN;
    Button saveBTN;
    EditText rValue,gValue,bValue;
    EditText rValueLow,gValueLow,bValueLow;

    Options.LibsComputerVision lib = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        rValue = (EditText) findViewById(R.id.rValue);
        rValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        rValue.setText(String.valueOf(Options.getInstance().getHigh_R()), TextView.BufferType.EDITABLE);

        gValue = (EditText) findViewById(R.id.gValue);
        gValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        gValue.setText(String.valueOf(Options.getInstance().getHigh_G()), TextView.BufferType.EDITABLE);

        bValue = (EditText) findViewById(R.id.bValue);
        bValue.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        bValue.setText(String.valueOf(Options.getInstance().getHigh_B()), TextView.BufferType.EDITABLE);

        rValueLow = (EditText) findViewById(R.id.rValueLow);
        rValueLow.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        rValueLow.setText(String.valueOf(Options.getInstance().getLow_R()), TextView.BufferType.EDITABLE);

        gValueLow = (EditText) findViewById(R.id.gValueLow);
        gValueLow.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        gValueLow.setText(String.valueOf(Options.getInstance().getLow_G()), TextView.BufferType.EDITABLE);

        bValueLow = (EditText) findViewById(R.id.bValueLow);
        bValueLow.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        bValueLow.setText(String.valueOf(Options.getInstance().getLow_B()), TextView.BufferType.EDITABLE);

        openCV = (CheckBox)findViewById(R.id.openCV);
        boof = (CheckBox)findViewById(R.id.boof);
        other = (CheckBox)findViewById(R.id.other);

        lib = Options.getInstance().getLibsComputerVision();

        switch (lib) {
            case OPENCV:
                openCV.setChecked(true);
                break;
            case BOOF:
                boof.setChecked(true);
                break;
            case OTHER:
                other.setChecked(true);
                break;

        }

        saveCheckBTN = (CheckBox)findViewById(R.id.saveCheckBTN);
        if (Options.getInstance().getSaveBitmap()){
            saveCheckBTN.setChecked(true);
        }




        saveBTN = (Button)findViewById(R.id.saveBTN);

        openCV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    boof.setChecked(false);
                    other.setChecked(false);
                    lib = Options.LibsComputerVision.OPENCV;
                }
            }
        });

        boof.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    openCV.setChecked(false);
                    other.setChecked(false);
                    lib = Options.LibsComputerVision.BOOF;
                }
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    openCV.setChecked(false);
                    boof.setChecked(false);
                    lib = Options.LibsComputerVision.OTHER;
                }
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rValue.getText().toString().isEmpty() &&
                    !gValue.getText().toString().isEmpty() &&
                    !bValue.getText().toString().isEmpty() &&
                    !rValueLow.getText().toString().isEmpty() &&
                    !gValueLow.getText().toString().isEmpty() &&
                    !bValueLow.getText().toString().isEmpty() &&
                    (openCV.isChecked() || boof.isChecked() || other.isChecked())
                    )
                {
                    int RH,GH,BH,RL,BL,GL;
                    RH = Integer.parseInt(rValue.getText().toString());
                    GH = Integer.parseInt(gValue.getText().toString());
                    BH = Integer.parseInt(bValue.getText().toString());
                    RL = Integer.parseInt(rValueLow.getText().toString());
                    GL = Integer.parseInt(gValueLow.getText().toString());
                    BL = Integer.parseInt(bValueLow.getText().toString());

                    if(RH >= RL && GH >= GL && BH >= BL){
                        if(RH <= 255 && GH <= 255 && BH <= 255){
                            Options.getInstance().setHigh_R(RH);
                            Options.getInstance().setHigh_G(GH);
                            Options.getInstance().setHigh_B(BH);
                            Options.getInstance().setLow_R(RL);
                            Options.getInstance().setLow_B(GL);
                            Options.getInstance().setLow_G(BL);

                            Options.getInstance().setLibsComputerVision(lib);

                            if(saveCheckBTN.isChecked()){
                                Options.getInstance().setSaveBitmap(true);
                            }else{
                                Options.getInstance().setSaveBitmap(false);
                            }

                            // TODO DO SAVE INTO XML FILE

                            finish();
                        }
                        else
                            Log.v("DONKEY","is bigger than 255");
                    }else{
                        Log.v("DONKEY","LOW IS BIGGER THAN HIGH");
                    }
                }else{
                    Log.v("DONKEY","ONE INPUT IS EMPTY");
                }
            }
        });

    }
}

/**
 * SOURCE CODE HERE:    http://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
 * input EditText from range 0 to 255
 * */
class InputFilterMinMax implements InputFilter {
    private int min, max;
    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }
    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
