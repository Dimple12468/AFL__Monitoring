package com.theagriculture.app.Dda;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theagriculture.app.Admin.EditActivity;
import com.theagriculture.app.Ado.ado_map_fragment;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DdaActivity extends AppCompatActivity {
    private TextView textView;
    private NavigationView navigationView;
    private ImageView imageView;
    private boolean doubleBackToExitPressedOnce = false;
    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int RESULT_CODE = 786;

    private BottomNavigationView navigation;
    FrameLayout frameLayout;

//    final Fragment ado_map_fragment = new ado_map_fragment();
    final Fragment map_fragemnt_dda = new map_fragemnt_dda();
    final Fragment DdaPendingFragment = new DdaPendingFragment();
    final Fragment DdaOngoingFragment = new DdaOngoingFragment();
    final Fragment DdaCompletedFragment = new DdaCompletedFragment();
    final Fragment adounderddo = new adounderddo();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = map_fragemnt_dda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_dda);

            frameLayout = findViewById(R.id.frameLayout_dda);
            navigation = findViewById(R.id.navigation_cmnd);

            //Get the name and type of user from the shared preferences file to display in header view of the drawer layout
            SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
            String typeofuser = preferences.getString("role", "");
            String username = preferences.getString("Name", "");

            if (getPermission()) {
//                InitializeFragment(map_fragemnt_dda);
                active = map_fragemnt_dda;
                navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.home_dda:
//                                InitializeFragment(map_fragemnt_dda);
                                fm.beginTransaction().hide(active).show(map_fragemnt_dda).commit();
                                active = map_fragemnt_dda;
                                //InitializeFragment(map_fragemnt_dda);
                                return true;
                            case R.id.pending_dda:
//                                InitializeFragment(DdaPendingFragment);
                                fm.beginTransaction().hide(active).show(DdaPendingFragment).commit();
                                active = DdaPendingFragment;
                                return true;
                            case R.id.ongoing_dda:
//                                InitializeFragment(DdaOngoingFragment);
                                fm.beginTransaction().hide(active).show(DdaOngoingFragment).commit();
                                active = DdaOngoingFragment;
                                return true;
                            case R.id.completed_dda:
//                                InitializeFragment(DdaCompletedFragment);
                                fm.beginTransaction().hide(active).show(DdaCompletedFragment).commit();
                                active = DdaCompletedFragment;
                                return true;
                            case R.id.ado_dda:
//                                InitializeFragment(adounderddo);
                                fm.beginTransaction().hide(active).show(adounderddo).commit();
                                active = adounderddo;
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }

            fm.beginTransaction().add(R.id.frameLayout_dda, adounderddo, "5").hide(DdaCompletedFragment).commit();
            fm.beginTransaction().add(R.id.frameLayout_dda, DdaCompletedFragment, "4").hide(DdaOngoingFragment).commit();
            fm.beginTransaction().add(R.id.frameLayout_dda, DdaOngoingFragment, "3").hide(DdaPendingFragment).commit();
            fm.beginTransaction().add(R.id.frameLayout_dda, DdaPendingFragment, "2").hide(map_fragemnt_dda).commit();
            fm.beginTransaction().add(R.id.frameLayout_dda,map_fragemnt_dda, "1").commit();
            fm.beginTransaction().show(map_fragemnt_dda).commit();


        }

    }
    private boolean getPermission() {
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

    public void InitializeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.replace(R.id.frameLayout_dda,fragment);
        fragmentTransaction.commit();

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
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportActionBar().setTitle("HOME");
                fm.beginTransaction().hide(active).show(map_fragemnt_dda).commit();
                active = map_fragemnt_dda;
//                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_dda, new map_fragemnt_dda()).commit();

            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
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
                    } else {
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
}