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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    //declare all fragments

    private map_fragemnt mapFragmnt;
    private location_fragment locationFragment;
    private ado_fragment adoFragment;
    private ddo_fragment ddoFragment;
    private count_fragment countFragment;
    private DistrictStateFragment districtStateFragment;
    private DistrictAdo districtAdo;
    bottom_nav bottom_nav;
    //private PrivacyPolicy privacyPolicy;
    TextView title_top;
    int id;

    //for drawer layout
    //private DrawerLayout mDrawer;
    //private NavigationView nvDrawer;

    private int fragment_id;
    Fragment fragment1;
    Fragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        navigation = findViewById(R.id.navigation);
        frameLayout = findViewById(R.id.frameLayout);
        //title_top = findViewById(R.id.app_name);
        //id = R.id.app_name;

        mapFragmnt = new map_fragemnt();
        locationFragment = new location_fragment();
        adoFragment = new ado_fragment();
        ddoFragment = new ddo_fragment();
        countFragment = new count_fragment();
        districtStateFragment= new DistrictStateFragment();
 //       privacyPolicy = new PrivacyPolicy();
        districtAdo = new DistrictAdo();
        bottom_nav = new bottom_nav();

        //for toolbar text
        /*if (districtAdo.isVisible()){
            System.out.println("Dimple in admin for ambal wala text");
            title_top.setText(districtAdo.for_title_top());
        }else {
            System.out.println("Dimple in else else for ambal wala text");
            title_top.setText("AFL Monitoring");
        }*/
       // districtAdo.for_title_top(title_top,R.id.app_name);



        // toolbar for admin activity
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar);
//        setSupportActionBar(toolbar);

/*        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_artboard_1);*/

/*        mDrawer = findViewById(R.id.drawer_view);
        nvDrawer = findViewById(R.id.navigation_view);

        View header = nvDrawer.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
        SharedPreferences preferences = getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("typeOfUser","");
        String username = preferences.getString("Name","");
        textUsername.setText(username);
        textUser.setText(typeofuser);*/
        // Toast.makeText(AdminActivity.this,textUsername.getText().toString().trim(),Toast.LENGTH_LONG).show();

        //navigation items listerner in navigation drawer
/*        nvDrawer.setBackgroundColor(getResources().getColor(R.color.white));
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.logout_now:                                                                                */
                        //item.setChecked(true);
                        //mDrawer.closeDrawers();
                        /*
                        Intent login_intent = new Intent(AdminActivity.this,login_activity.class);
                        startActivity(login_intent);
                        finish();
                        return true;

                         */

/*                        SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
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
        });*/


        // ask for permissions and intializes fragment according to
        // items in bottom navigation clicked
       if(getPermission()) {
           InitializeFragment(mapFragmnt);

            //bottom_nav.bottom_navigation_admin();
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    //todo style for fab
                    //fragment_id = item.getItemId();
                   /* if (item.getItemId() == R.id.adminsdistrict_state){
                        //setTheme(R.style.calendar_theme);
                        getTheme().applyStyle(R.style.calendar_theme,true);
                    }
                    if (item.getItemId() == R.id.adminshome){
                       // getTheme().applyStyle(R.style.original,true);
                        getTheme().applyStyle(R.style.AppTheme,true);
                        //setTheme(R.style.AppTheme);
                    }*/
                   switch (item.getItemId()) {
                        case R.id.adminshome:
                           // ((DrawerLocker) mapFragmnt).setDrawerEnabled(true);
                           // title_top.setText("AFL Monitoring");
                            InitializeFragment(mapFragmnt);
                            return true;
                        case R.id.adminslocation:
                            //((DrawerLocker) locationFragment).setDrawerEnabled(true);
                            //title_top.setText("Locations");
                            InitializeFragment(locationFragment);
                            return true;
                        case R.id.adminsado:
                           // ((DrawerLocker) adoFragment).setDrawerEnabled(false);
                            //title_top.setText("ADO");
                            InitializeFragment(adoFragment);
                            return true;
                        case R.id.adminsdda:
                           // title_top.setText("DDA");
                            InitializeFragment(ddoFragment);
                            return true;
                        case R.id.adminsdistrict_state:
                           // title_top.setText("District Stats");
                            InitializeFragment(districtStateFragment);
                            return true;
                        default:
                           // title_top.setText("AFL Monitoring");
                            return false;
                    }
                }
            });

        }

    }
    //end of onCreate


    //function to change fragment
    public void InitializeFragment(Fragment fragment) {
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentManager fragmentManager2 = getSupportFragmentManager();

        /*if (fragment == adoFragment) {
            switch (fragmentManager1.getBackStackEntryCount()) {
                case 0:
                    fragment1 = new DistrictAdo();
                    break;
                case 1:
                    fragment1 = new AdoDdoActivityFragment();
                    break;
                case 2:
                    fragment1 = new AdoDdoPending();
                    break;
                default:
                    fragment1 = new ado_fragment();
                    break;
            }
        } else if (fragment == locationFragment){
            switch (fragmentManager2.getBackStackEntryCount()) {
                case 0:
                    fragment2 = new PendingDetailsFragment();
                    break;
                case 1:
                    fragment2 = new ongoingDetailsFragment();
                    break;
                case 2:
                    fragment2 = new CompleteDetailsFragment();
                    break;
                default:
                    fragment2 = new location_fragment();
                    break;
            }
        }*/

       // fragment = fragmentManager.findFragmentById(R.id.frameLayout);
        //if (fragment instanceof AdminActivity)
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);//adds to stack but doesnot update bottom navigation
        fragmentTransaction.commit();

        /*if (fragment == districtAdo){
            System.out.println("Dimple in initialize fragment for ambal wala text");
            title_top.setText(districtAdo.for_title_top());
        }*/
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
    protected void onStart() {
       // mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        super.onStart();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        /*boolean disabled;
        if(!disabled)
        {
            if (item.getItemId() == android.R.id.home) {

                if (mDrawer.isDrawerOpen(mDrawer)) {
                    mDrawer.closeDrawer();
                } else {
                    mDrawerLayout.openDrawer(mDrawerLinearLayout);
                }
            }
        }**
        switch (item.getItemId()){
            case android.R.id.home:
                //Toast.makeText(this, "this is for hamburger menu", Toast.LENGTH_SHORT).show();
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

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

   /* @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        mDrawer.setDrawerLockMode(lockMode);
        //nvDrawer.setDrawerLockMode(lockMode);

        //ActionBarDrawerToggle toggle;
        //toggle.setDrawerIndicatorEnabled(enabled);
    }*/
}