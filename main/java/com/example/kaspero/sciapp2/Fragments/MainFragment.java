package com.example.kaspero.sciapp2.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kaspero.sciapp2.Activity.EditActivity;
import com.example.kaspero.sciapp2.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    Button chooseBtn;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        chooseBtn = (Button)view.findViewById(R.id.chooseBtn);
        chooseBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        Button btn = (Button)v;

        if(btn == chooseBtn){
//            FragmentManager fragmentManager = getFragmentManager();
//            MainActivity.openChoosePhotoFragment(fragmentManager);
            Intent intent = new Intent(getActivity(),EditActivity.class);
            startActivity(intent);

        }

    }
}
