package com.theagriculture.app.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.theagriculture.app.PrivacyPolicy;
import com.theagriculture.app.R;
import com.theagriculture.app.RegistrationActivity;
import com.theagriculture.app.login_activity;
import com.theagriculture.app.nav_drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static android.content.res.ColorStateList.*;

public class AdminActivity extends AppCompatActivity {

    //all permissions declared

    private final String TAG = "AdminActivity";
    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int RESULT_CODE = 786;

    BottomNavigationView navigation;
    FrameLayout frameLayout;

    //declare all fragments

    private map_fragemnt mapFragmnt;
    private location_fragment locationFragment;
    private ado_fragment adoFragment;
    private ddo_fragment ddoFragment;
    private count_fragment countFragment;
    private DistrictStateFragment districtStateFragment;
    private PrivacyPolicy privacyPolicy;

    //for drawer layout
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    private int fragment_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        navigation = findViewById(R.id.navigation);
        frameLayout = findViewById(R.id.frameLayout);

        mapFragmnt = new map_fragemnt();
        locationFragment = new location_fragment();
        adoFragment = new ado_fragment();
        ddoFragment = new ddo_fragment();
        countFragment = new count_fragment();
        districtStateFragment= new DistrictStateFragment();
        privacyPolicy = new PrivacyPolicy();



        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar);
        setSupportActionBar(toolbar);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_artboard_1);

        mDrawer = findViewById(R.id.drawer_view);
        nvDrawer = findViewById(R.id.navigation_view);

        View header = nvDrawer.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
        SharedPreferences preferences = getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("typeOfUser","");
        String username = preferences.getString("Name","");
        textUsername.setText(username);
        textUser.setText(typeofuser);
        // Toast.makeText(AdminActivity.this,textUsername.getText().toString().trim(),Toast.LENGTH_LONG).show();

        nvDrawer.setBackgroundColor(getResources().getColor(R.color.white));
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.logout_now:
                        //item.setChecked(true);
                        //mDrawer.closeDrawers();
                        /*
                        Intent login_intent = new Intent(AdminActivity.this,login_activity.class);
                        startActivity(login_intent);
                        finish();
                        return true;

                         */

                        SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        mDrawer.closeDrawers();
                        Intent intent = new Intent(AdminActivity.this, login_activity.class);
                        //Intent intent = new Intent(getApplicationContext(), login_activity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.privacy:
                        //item.setChecked(true);
                       // Toast.makeText(AdminActivity.this, "privacy clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        InitializeFragment(privacyPolicy);
                        return true;

                    case R.id.terms:
                        item.setChecked(true);
                        Toast.makeText(AdminActivity.this, "terms clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.help:
                        item.setChecked(true);
                        Toast.makeText(AdminActivity.this, "help clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.advance_settings:
                        item.setChecked(true);
                        Toast.makeText(AdminActivity.this, "settings clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;


                }
                return false;
            }
        });


        // Menu m = navigationView.getMenu(R.menu.admin_drawer);

        //MenuItem nav = navigationView.getMenu().findItem(R.id.nav_close_app);
        //nav.setActionView(R.layout.item_navigationdrawer_close_app);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        if(getPermission()) {
            InitializeFragment(mapFragmnt);
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    //fragment_id = item.getItemId();
                    switch (item.getItemId()) {
                        case R.id.adminshome:
                            InitializeFragment(mapFragmnt);
                            return true;
                        case R.id.adminslocation:
                            InitializeFragment(locationFragment);
                            return true;
                        case R.id.adminsado:
                            InitializeFragment(adoFragment);
                            return true;
                        case R.id.adminsdda:
                            InitializeFragment(ddoFragment);
                            return true;
                        case R.id.adminsdistrict_state:
                            InitializeFragment(districtStateFragment);
                            return true;
                        default:
                            return false;
                    }
                }
            });

        }

    }
    //end of onCreate

    //function to change fragment

    public void InitializeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }



    //function to get permissions
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
    //function for top drop down menu
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_bar , menu);
        //return true;

        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search something");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("dimple this is search place here");
                //if (fragment_id==R.id.adminsado)

                return false;
            }
        });

        //MenuItem action_done = menu.findItem(R.id.ic_file_upload);
        //menuIconColor(action_done, R.drawable.tab_color);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*public void menuIconColor(MenuItem menuItem, int color) {
        color = R.drawable.tab_color_new;
        Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                //Toast.makeText(this, "this is for hamburger menu", Toast.LENGTH_SHORT).show();
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //        int id = item.getItemId();
    //noinspection SimplifiableIfStatement
        /*if (id == R.id.logout) {
            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(AdminActivity.this, login_activity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(id == R.id.help){

        }

        if(id == R.id.policy){

        }

        if(id == R.id.service){

        }

        if(id == R.id.mohit){

        }
        return super.onOptionsItemSelected(item);
    }*/




    private void showMap() {
        Log.d(TAG, "showMap: getsupport");
        FragmentManager mfragmentmanager = getSupportFragmentManager();
        Log.d(TAG, "showMap: getsupport" + mfragmentmanager);
        mfragmentmanager.beginTransaction().replace(R.id.frameLayout, mapFragmnt).commit();
        //InitializeFragment(mapFragmnt);
    }

    /*
    @Override
    public void onBackPressed(){
        //finish();//will pop previous activity from stack
        Intent a = new Intent(Intent.ACTION_MAIN);//will exit app
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

     */

    @Override
    public void onBackPressed() {
        //to get activities in stack
        // ActivityManager result = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        //List<ActivityManager.RunningTaskInfo> services = result.getRunningTasks(Integer.MAX_VALUE);//Integer.MAX_VALUE=value of all running tasks

        //Toast.makeText(AdminActivity.this,services.get(0).topActivity.toString(),Toast.LENGTH_LONG).show();

        super.onBackPressed();//shows items in stack only done for ongoingDetailsFragment
        //finish();
        //}
    }

}