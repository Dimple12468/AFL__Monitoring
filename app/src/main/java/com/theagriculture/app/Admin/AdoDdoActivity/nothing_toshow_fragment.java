package com.theagriculture.app.Admin.AdoDdoActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theagriculture.app.R;

public class nothing_toshow_fragment extends Fragment {

    ImageView imageView;
    TextView textView;
    public nothing_toshow_fragment() {
        // Required empty public constructor
    }


   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nothing_toshow, container, false);
        imageView = view.findViewById(R.id.no_data);
        textView = view.findViewById(R.id.no_data_text);
        //AppCompatActivity activity = (AppCompatActivity)getContext();
        //activity.getSupportFragmentManager().beginTransaction().remove(AdoDdoCompleted()).addToBackStack(null).commit();
        return view;
    }
}