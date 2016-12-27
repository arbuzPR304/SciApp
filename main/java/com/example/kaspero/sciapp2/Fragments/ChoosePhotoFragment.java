package com.example.kaspero.sciapp2.Fragments;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kaspero.sciapp2.Activity.EditActivity;
import com.example.kaspero.sciapp2.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class ChoosePhotoFragment extends Fragment implements View.OnClickListener {
    // CONST VAR FOR TAKING PHOTO
    public final static String EXTRA_URI = "com.example.SciApp2.URI";
    private final int RESULT_LOAD_IMAGE = 1;
    private RelativeLayout imageContainer;
    private Button buttonLoadPhoto,buttonEditPhoto;
    private ImageView imageView;

    private Uri contentURI;




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
        buttonEditPhoto = (Button)view.findViewById(R.id.editBtn);
        buttonEditPhoto.setOnClickListener(this);
        imageView = (ImageView)view.findViewById(R.id.photoGaleryView);
        imageContainer = (RelativeLayout)view.findViewById(R.id.imagecontainer);
        buttonEditPhoto.setEnabled(false);

        return view;
    }


    // ON CLICK - FOR TAKE BITMAP FROM GALLERY
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
        if(btn == buttonEditPhoto){
            openEditMode();
        }
    }
    public void openEditMode(){
        Intent intent = new Intent(getActivity(),EditActivity.class);
        intent.putExtra(EXTRA_URI, contentURI.toString());
        startActivity(intent);



    }

    // OPEN GALLERY INTENT
    private void galleryIntent()
    {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), RESULT_LOAD_IMAGE);
    }

    /**
     * CAPTURE CHOOSEN PHOTO, NEXT TAKING URL, BITMAP IN
     * TO MEMMORY AND THE AND SET IN IMAGEVIEW
     * AND ENABLE EDIT BUTTON
     * */
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    if(imageReturnedIntent != null){
                        contentURI = Uri.parse(imageReturnedIntent.getDataString());
                        ContentResolver cr = getActivity().getContentResolver();
                        InputStream in = null;
                        try {
                            in = cr.openInputStream(contentURI);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize=4;
                        Bitmap thumb = BitmapFactory.decodeStream(in,null,options);
                        imageView.setImageBitmap(thumb);
                        if (thumb != null) {
                            buttonEditPhoto.setEnabled(true);
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                buttonEditPhoto.setBackgroundDrawable( getResources().getDrawable(R.drawable.herro_button) );
                            } else {
                                buttonEditPhoto.setBackground( getResources().getDrawable(R.drawable.herro_button));
                            }
                        }
                            imageContainer.setBackgroundColor(Color.BLACK);

                    }
                }
                break;
        }
    }


}
