package com.theagriculture.app.Ado;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.theagriculture.app.Admin.AdminActivity;
import com.theagriculture.app.Admin.EditActivity;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdoActivity extends AppCompatActivity {


    DrawerLayout drawer;
    private boolean doubleBackToExitPressedOnce = false;
    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int RESULT_CODE = 786;

    private NavigationView navigationView;
    private BottomNavigationView navigation;
    FrameLayout frameLayout;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    private ado_pending_fragment ado_pending_fragment;
    private ado_complete_fragment ado_complete_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ado);
        navigationView = findViewById(R.id.navigation_view_ado);
        frameLayout = findViewById(R.id.frameLayout_ado);
        navigation = findViewById(R.id.navigation_ado);
        // for Drawer layout
        mDrawer = findViewById(R.id.drawer_view_ado);
        nvDrawer = findViewById(R.id.navigation_view_ado);

        //Define toolbar that displays the app name
        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_ado);
        setSupportActionBar(toolbar);

        //define the action bar with hamburger icon
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.hamb);

        //define the fragments
        ado_pending_fragment = new ado_pending_fragment();
        ado_complete_fragment = new ado_complete_fragment();

        //Get the name and type of user from the shared preferences file to display in header view of the drawer layout
        SharedPreferences preferences = getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("typeOfUser","");
        String username = preferences.getString("Name","");

        //Defining the drawer layout
        //get the header of drawer layout and set user name and type
        View header = nvDrawer.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
        textUsername.setText(username);
        textUser.setText(typeofuser);
        //set the navigation options in drawer layout
        nvDrawer.setBackgroundColor(getResources().getColor(R.color.white));
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout_now:
                        SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        mDrawer.closeDrawers();
                        Intent intent = new Intent(AdoActivity.this, login_activity.class);
                        //Intent intent = new Intent(getApplicationContext(), login_activity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.privacy:
                        item.setChecked(true);
                        Toast.makeText(AdoActivity.this, "privacy clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.terms:
                        item.setChecked(true);
                        Toast.makeText(AdoActivity.this, "terms clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.help:
                        item.setChecked(true);
                        Toast.makeText(AdoActivity.this, "help clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.advance_settings:
                        item.setChecked(true);
                        Toast.makeText(AdoActivity.this, "settings clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;


                }
                return false;
            }
        });

        //if all permissions granted then initilize pending fragment and set navigation items
        if(getPermission()) {
            InitializeFragment(ado_pending_fragment);
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.pending_ado:
                            InitializeFragment(ado_pending_fragment);
                            return true;
                        case R.id.completed_ado:
                            InitializeFragment(ado_complete_fragment);
                            return true;
                        default:
                            return false;
                    }
                }
            });

        }

    }


    @Override
    public void onBackPressed() {
                super.onBackPressed();
        }

    public void InitializeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.replace(R.id.frameLayout_ado,fragment);
        fragmentTransaction.commit();

    }

    //function to ask permissions
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
                navigationView.setCheckedItem(R.id.nav_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_ado, new map_fragemnt_ado()).commit();

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

    //function for top drop down menu
    @Override
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
                return false;
            }
        });

        //MenuItem action_done = menu.findItem(R.id.ic_file_upload);
        //menuIconColor(action_done, R.drawable.tab_color);
        return super.onCreateOptionsMenu(menu);
    }

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

}
