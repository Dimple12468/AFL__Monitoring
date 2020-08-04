package com.theagriculture.app.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theagriculture.app.Admin.AdminActivity;
import com.theagriculture.app.Admin.DistrictStateFragment;
import com.theagriculture.app.Admin.ado_fragment;
import com.theagriculture.app.Admin.count_fragment;
import com.theagriculture.app.Admin.ddo_fragment;
import com.theagriculture.app.Admin.location_fragment;
import com.theagriculture.app.Admin.map_fragemnt;
import com.theagriculture.app.R;

public class bottom_nav extends AppCompatActivity {

    BottomNavigationView navigation_admin;
    private map_fragemnt mapFragmnt;
    private location_fragment locationFragment;
    private ado_fragment adoFragment;
    private ddo_fragment ddoFragment;
    private DistrictStateFragment districtStateFragment;
    
    AdminActivity adminActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);
        navigation_admin = findViewById(R.id.navigation_admin);

        mapFragmnt = new map_fragemnt();
        locationFragment = new location_fragment();
        adoFragment = new ado_fragment();
        ddoFragment = new ddo_fragment();
        districtStateFragment= new DistrictStateFragment();
        adminActivity = new AdminActivity();
    }

    public void bottom_navigation_admin(){
        //if(adminActivity.getPermission()) {
            //InitializeFragment(mapFragmnt);
            navigation_admin.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    
                    switch (item.getItemId()) {
                        case R.id.adminshome:
                            InitializeFragment(mapFragmnt);
                            return true;
                        case R.id.adminslocation:
                            //((DrawerLocker) locationFragment).setDrawerEnabled(true);
                            //title_top.setText("Locations");
                           // InitializeFragment(locationFragment);
                            Toast.makeText(adminActivity, "locations clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.adminsado:
                            // ((DrawerLocker) adoFragment).setDrawerEnabled(false);
                            //title_top.setText("ADO");
                            InitializeFragment(adoFragment);
                            return true;
                        case R.id.adminsdda:
                            // title_top.setText("DDA");
                            //InitializeFragment(ddoFragment);
                            Toast.makeText(adminActivity, "dda clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.adminsdistrict_state:
                            // title_top.setText("District Stats");
                           // InitializeFragment(districtStateFragment);
                            Toast.makeText(adminActivity, "stats clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            // title_top.setText("AFL Monitoring");
                            return false;
                    }
                }
            });

        //}

    }

    public void InitializeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.commit();

    }

}