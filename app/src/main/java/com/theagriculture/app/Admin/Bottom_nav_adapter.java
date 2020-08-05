package com.theagriculture.app.Admin;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theagriculture.app.R;

public class Bottom_nav_adapter extends BottomNavigationView {

    Context context;
    Fragment fragment;
    int bottom_id;

    public Bottom_nav_adapter(@NonNull Context context, Fragment fragment,int bottom_id) {
        super(context);
        this.context = context;
        this.fragment = fragment;
        this.bottom_id = bottom_id;
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {

        if (bottom_id == R.id.adminshome){
            InitializeFragment(fragment);
        }else if (bottom_id == R.id.adminslocation) {
            InitializeFragment(fragment);
        }else if (bottom_id == R.id.adminsado) {
            InitializeFragment(fragment);
        }else if (bottom_id == R.id.adminsdda) {
            InitializeFragment(fragment);
        }else if (bottom_id == R.id.adminsdistrict_state) {
            InitializeFragment(fragment);
        }

        super.setOnNavigationItemSelectedListener(listener);
    }

    public void InitializeFragment(Fragment fragment) {

        AppCompatActivity activity = (AppCompatActivity) fragment.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack(null).commit();
      /*  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.commit();*/

    }
}
