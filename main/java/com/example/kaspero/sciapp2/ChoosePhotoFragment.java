package com.example.kaspero.sciapp2;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import java.io.IOException;

import static android.R.attr.button;
import static android.R.attr.color;
import static android.app.Activity.RESULT_OK;
import static com.example.kaspero.sciapp2.R.id.fab;

public class ChoosePhotoFragment extends Fragment implements View.OnClickListener {

    private static int RESULT_LOAD_IMAGE = 1;

    private RelativeLayout imageContainer;
    private Button buttonLoadPhoto;
    private ImageView imageView;




    public ChoosePhotoFragment() {
        // Required empty public constructor
    }

    public static ChoosePhotoFragment newInstance() {
        ChoosePhotoFragment fragment = new ChoosePhotoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_choose_photo, container, false);

        buttonLoadPhoto = (Button)view.findViewById(R.id.chooseGalleryBtn);
        buttonLoadPhoto.setOnClickListener(this);

        imageView = (ImageView)view.findViewById(R.id.photoGaleryView);

        imageContainer = (RelativeLayout)view.findViewById(R.id.imagecontainer);



        return view;
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button)v;
        if (btn == buttonLoadPhoto){
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},111);
            }else{
                galleryIntent();
            }
        }
    }

    private void galleryIntent()
    {

//        Intent i = new Intent(
//                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, RESULT_LOAD_IMAGE);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), RESULT_LOAD_IMAGE);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageContainer.setBackgroundColor(Color.BLACK);
                    imageView.setImageURI(selectedImage);
                }
                break;
        }
    }


}
