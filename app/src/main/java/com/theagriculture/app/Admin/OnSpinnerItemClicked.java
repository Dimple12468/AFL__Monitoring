package com.theagriculture.app.Admin;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.theagriculture.app.R;

public class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(), "spinner clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
