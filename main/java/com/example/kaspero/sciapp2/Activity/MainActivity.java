package com.example.kaspero.sciapp2.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kaspero.sciapp2.Fragments.ChoosePhotoFragment;
import com.example.kaspero.sciapp2.Fragments.MainFragment;
import com.example.kaspero.sciapp2.Options.Options;
import com.example.kaspero.sciapp2.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity{

    private String mCurrentPhotoPath;
    private FragmentManager manager;
    static final int REQUEST_TAKE_PHOTO = 1;

    private Uri photoURI = null;


/**
 * CREATE IMAGE FILE  */

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(new Date());
        String imageFileName = "SciApp:" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /**
     * CREATE PHOTO MAKE INTENT  */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.kaspero.sciapp2",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

/**
 * ONCREATE HERE SET UP MAIN FRAGMENT
 * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Options.getInstance().getOnStart()) {
            Options.getInstance().loadOptions(this);
            Options.getInstance().setOnStart(false);
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sciAppTools);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = new MainFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment);
            transaction.commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

//                Options.getInstance().setFromCamera(true);
//                Intent intent = new Intent(context,EditActivity.class);
//                intent.putExtra("PHOTO",photoURI.toString());
//                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (photoURI!=null){
                Options.getInstance().setPhotoUriOpt(photoURI);
                Options.getInstance().setFromCamera(true);
            }
            Intent intent = new Intent(this,EditActivity.class);
            startActivity(intent);
        }
    }


    /**
     * FOR OPEN CHOOSE PHOTO FRAGMENT
     */
    public static void openChoosePhotoFragment(FragmentManager fragmentManager){

        Fragment newChoosePhotoFragment = new ChoosePhotoFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,newChoosePhotoFragment).addToBackStack(null).commit();



    }

    /**
     * This fuction is temple of open item from menu
     * @param fragmentManager
     */
    public static void openItemFromMenu(FragmentManager fragmentManager){

    }


}
//TODO dwie biblioteki, O APLIKACJI, ZAPIS DO PLIKU, kwadraty