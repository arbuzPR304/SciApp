package com.example.kaspero.sciapp2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class ChoosePhotoFragment extends Fragment {

    private TabHost tabHost;


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


        tabHost = (TabHost)view.findViewById(R.id.tabHost);
        tabHost.addTab(tabHost.newTabSpec("Galeria").setIndicator("Galeria"));
        tabHost.addTab(tabHost.newTabSpec("Galeria").setIndicator("Galeria"));

        return view;
    }

}
