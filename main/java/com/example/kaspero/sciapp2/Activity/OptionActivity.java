package com.example.kaspero.sciapp2.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.R;
public class OptionActivity extends AppCompatActivity {

    CheckBox openCV;
    CheckBox boof;
    CheckBox other;
    CheckBox saveCheckBTN;
    Button saveBTN;
    EditText rValue, gValue, bValue;
    EditText rValueLow, gValueLow, bValueLow;

    Button rgbColorHigh, rgbColorLow;

    Options.LibsComputerVision lib = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        rValue = (EditText) findViewById(R.id.rValue);
        rValue.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        rValue.setText(String.valueOf(Options.getInstance().getHigh_R()), TextView.BufferType.EDITABLE);
        rValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) <= 255 && Integer.parseInt(s.toString()) > 0) {
                        int color = ((ColorDrawable) rgbColorHigh.getBackground()).getColor();
                        setColorsOn(Integer.parseInt(s.toString()), Color.green(color), Color.blue(color), true);
                    }
                } catch (Exception io) {
                    Log.e("ERROR", io.toString());
                }
            }
        });

        gValue = (EditText) findViewById(R.id.gValue);
        gValue.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        gValue.setText(String.valueOf(Options.getInstance().getHigh_G()), TextView.BufferType.EDITABLE);
        gValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) <= 255 && Integer.parseInt(s.toString()) > 0) {
                        int color = ((ColorDrawable) rgbColorHigh.getBackground()).getColor();
                        setColorsOn(Color.red(color), Integer.parseInt(s.toString()), Color.blue(color), true);
                    }
                } catch (Exception io) {
                    Log.e("ERROR", io.toString());
                }
            }
        });

        bValue = (EditText) findViewById(R.id.bValue);
        bValue.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        bValue.setText(String.valueOf(Options.getInstance().getHigh_B()), TextView.BufferType.EDITABLE);
        bValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) <= 255 && Integer.parseInt(s.toString()) > 0) {
                        int color = ((ColorDrawable) rgbColorHigh.getBackground()).getColor();
                        setColorsOn(Color.red(color), Color.green(color), Integer.parseInt(s.toString()), true);
                    }
                } catch (Exception io) {
                    Log.e("ERROR", io.toString());
                }
            }
        });

        rValueLow = (EditText) findViewById(R.id.rValueLow);
        rValueLow.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        rValueLow.setText(String.valueOf(Options.getInstance().getLow_R()), TextView.BufferType.EDITABLE);
        rValueLow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) <= 255 && Integer.parseInt(s.toString()) > 0) {
                        int color = ((ColorDrawable) rgbColorLow.getBackground()).getColor();
                        setColorsOn(Integer.parseInt(s.toString()), Color.green(color), Color.blue(color), false);
                    }
                } catch (Exception io) {
                    Log.e("ERROR", io.toString());
                }
            }
        });


        gValueLow = (EditText) findViewById(R.id.gValueLow);
        gValueLow.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        gValueLow.setText(String.valueOf(Options.getInstance().getLow_G()), TextView.BufferType.EDITABLE);
        gValueLow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) <= 255 && Integer.parseInt(s.toString()) > 0) {
                        int color = ((ColorDrawable) rgbColorLow.getBackground()).getColor();
                        setColorsOn(Color.red(color), Integer.parseInt(s.toString()), Color.blue(color), false);
                    }
                } catch (Exception io) {
                    Log.e("ERROR", io.toString());
                }
            }
        });

        bValueLow = (EditText) findViewById(R.id.bValueLow);
        bValueLow.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        bValueLow.setText(String.valueOf(Options.getInstance().getLow_B()), TextView.BufferType.EDITABLE);
        bValueLow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) <= 255 && Integer.parseInt(s.toString()) > 0) {
                        int color = ((ColorDrawable) rgbColorLow.getBackground()).getColor();
                        setColorsOn(Color.red(color), Color.green(color), Integer.parseInt(s.toString()), false);
                    }
                } catch (Exception io) {
                    Log.e("ERROR", io.toString());
                }
            }
        });

        openCV = (CheckBox) findViewById(R.id.openCV);
        boof = (CheckBox) findViewById(R.id.boof);
        other = (CheckBox) findViewById(R.id.other);

        lib = Options.getInstance().getLibsComputerVision();

        rgbColorLow = (Button) findViewById(R.id.rgbButtonLow);
        rgbColorHigh = (Button) findViewById(R.id.rgbButtonHigh);

        rgbColorHigh.setBackgroundColor(Color.rgb(
                Options.getInstance().getHigh_R(),
                Options.getInstance().getHigh_G(),
                Options.getInstance().getHigh_R())
        );

        rgbColorLow.setBackgroundColor(Color.rgb(
                Options.getInstance().getLow_R(),
                Options.getInstance().getLow_G(),
                Options.getInstance().getLow_R())
        );


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

        saveCheckBTN = (CheckBox) findViewById(R.id.saveCheckBTN);
        if (Options.getInstance().getSaveBitmap()) {
            saveCheckBTN.setChecked(true);
        }


        saveBTN = (Button) findViewById(R.id.saveBTN);

        openCV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boof.setChecked(false);
                    other.setChecked(false);
                    lib = Options.LibsComputerVision.OPENCV;
                }
            }
        });

        boof.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    openCV.setChecked(false);
                    other.setChecked(false);
                    lib = Options.LibsComputerVision.BOOF;
                }
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                        ) {
                    int RH, GH, BH, RL, BL, GL;
                    RH = Integer.parseInt(rValue.getText().toString());
                    GH = Integer.parseInt(gValue.getText().toString());
                    BH = Integer.parseInt(bValue.getText().toString());
                    RL = Integer.parseInt(rValueLow.getText().toString());
                    GL = Integer.parseInt(gValueLow.getText().toString());
                    BL = Integer.parseInt(bValueLow.getText().toString());

                    if (RH >= RL && GH >= GL && BH >= BL) {
                        if (RH <= 255 && GH <= 255 && BH <= 255) {
                            Options.getInstance().setHigh_R(RH);
                            Options.getInstance().setHigh_G(GH);
                            Options.getInstance().setHigh_B(BH);
                            Options.getInstance().setLow_R(RL);
                            Options.getInstance().setLow_B(GL);
                            Options.getInstance().setLow_G(BL);

                            Options.getInstance().setLibsComputerVision(lib);

                            if (saveCheckBTN.isChecked()) {
                                Options.getInstance().setSaveBitmap(true);
                            } else {
                                Options.getInstance().setSaveBitmap(false);
                            }
                            // TODO DO SAVE INTO XML FILE
                            finish();
                        } else {
                            messageValidation("Wartość większa od 255!");
                        }
                    } else {
                        messageValidation("Próg dolny jest więszy od progu górnego!");
                    }
                } else {
                    messageValidation("Jendo z pól nie jest wypełnione!");
                }
            }
        });
    }

    /**
     * level = true : high RGBcolor
     * level = false : low RGBcolor
     */
    private void setColorsOn(int r, int g, int b, boolean level) {
        if (level)
            rgbColorHigh.setBackgroundColor(Color.rgb(r, g, b));
        else
            rgbColorLow.setBackgroundColor(Color.rgb(r, g, b));
    }

    private void messageValidation(String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(s)
                .setTitle("SciApp")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


