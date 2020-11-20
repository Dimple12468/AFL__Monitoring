package com.theagriculture.app.Admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
//import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theagriculture.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity /*implements DrawerLocker*/ {

    //all permissions declared

    private final String TAG = "AdminActivity";
    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int RESULT_CODE = 786;

    BottomNavigationView navigation;
    FrameLayout frameLayout;

    bottom_nav bottom_nav;
    TextView title_top;
    int id;

    //for drawer layout
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    final Fragment mapFragmnt = new map_fragemnt();
    final Fragment locationFragment = new location_fragment();
    final Fragment adoFragment = new ado_fragment();
    final Fragment ddoFragment = new ddo_fragment();
    final Fragment countFragment = new count_fragment();
//    final Fragment districtAdo = new DistrictAdo();
    final Fragment districtStateFragment= new DistrictStateFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mapFragmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null)
        {
            setContentView(R.layout.activity_admin);
            navigation = findViewById(R.id.navigation);
            frameLayout = findViewById(R.id.frameLayout);
            bottom_nav = new bottom_nav();

            mDrawer = findViewById(R.id.drawer_view);
            nvDrawer = findViewById(R.id.navigation_view);


            // ask for permissions and intializes fragment according to
            // items in bottom navigation clicked
            if(getPermission()) {
                active = mapFragmnt;
                navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.adminshome:
//                            InitializeFragment(mapFragmnt);
                                fm.beginTransaction().hide(active).show(mapFragmnt).commit();
                                active = mapFragmnt;
                                return true;
                            case R.id.adminslocation:
//                            InitializeFragment(locationFragment);
                                fm.beginTransaction().hide(active).show(locationFragment).commit();
                                active = locationFragment;
                                return true;
                            case R.id.adminsado:
//                            InitializeFragment(adoFragment);
                                fm.beginTransaction().hide(active).show(adoFragment).commit();
                                active = adoFragment;
                                return true;
                            case R.id.adminsdda:
//                            InitializeFragment(ddoFragment);
                                fm.beginTransaction().hide(active).show(ddoFragment).commit();
                                active = ddoFragment;
                                return true;
                            case R.id.adminsdistrict_state:
//                            InitializeFragment(districtStateFragment);
                                fm.beginTransaction().hide(active).show(districtStateFragment).commit();
                                active = districtStateFragment;
                                return true;
                            default:
                                return false;
                        }
                    }
                });

            }
            fm.beginTransaction().add(R.id.frameLayout, districtStateFragment, "5").hide(ddoFragment).commit();
            fm.beginTransaction().add(R.id.frameLayout, ddoFragment, "4").hide(adoFragment).commit();
            fm.beginTransaction().add(R.id.frameLayout, adoFragment, "3").hide(locationFragment).commit();
            fm.beginTransaction().add(R.id.frameLayout, locationFragment, "2").hide(mapFragmnt).commit();
            fm.beginTransaction().add(R.id.frameLayout,mapFragmnt, "1").commit();
            fm.beginTransaction().show(mapFragmnt).commit();
        }
    }
    //end of onCreate


    //function to change fragment
    public void InitializeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.commit();
    }


    //function to get permissions
    public boolean getPermission() {
        List<String> Permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Permission.add(WRITE_EXTERNAL_STORAGE);
        }

        if (!Permission.isEmpty()) {
            String[] permissions = Permission.toArray(new String[Permission.size()]);
            ActivityCompat.requestPermissions(this, permissions, RESULT_CODE);
            return false;
        } else
            return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == RESULT_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            if (deniedCount == 0) {
                InitializeFragment(mapFragmnt);
            }
            else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(AdminActivity.this, permName)) {
                        showDialog("", "This app needs location and files permissions to work without any problems.",
                                "Yes, Grant permissions",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        getPermission();
                                    }
                                },
                                "No, Exit app",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }, false);
                    }
                    else {
                        showDialog("",
                                "You have denied some permissions. Allow all the permissions at [Setting] > [Permissions]",
                                "Go to Settings",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, "No, Exit App",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    private AlertDialog showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                   String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
                                   boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    @Override
    protected void onStart() {
       // mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        super.onStart();
    }

    private void showMap() {
        Log.d(TAG, "showMap: getsupport");
        FragmentManager mfragmentmanager = getSupportFragmentManager();
        Log.d(TAG, "showMap: getsupport" + mfragmentmanager);
        mfragmentmanager.beginTransaction().replace(R.id.frameLayout, mapFragmnt).commit();
        //InitializeFragment(mapFragmnt);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();//shows items in stack only done for ongoingDetailsFragment

    }

}