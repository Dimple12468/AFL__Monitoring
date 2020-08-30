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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theagriculture.app.Admin.EditActivity;
import com.theagriculture.app.Admin.ViewPagerAdapter;
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

    private ado_map_fragment ado_map_fragment;
    private map_fragemnt_dda map_fragemnt_dda;
    private DdaPendingFragment DdaPendingFragment;
    private DdaOngoingFragment DdaOngoingFragment;
    private DdaCompletedFragment DdaCompletedFragment;
    private adounderddo adounderddo;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dda);

        frameLayout = findViewById(R.id.frameLayout_dda);
        navigation = findViewById(R.id.navigation_cmnd);
        viewPager2 = findViewById(R.id.viewpager2_dda);

        ado_map_fragment = new ado_map_fragment();
        map_fragemnt_dda = new map_fragemnt_dda();
        DdaPendingFragment = new DdaPendingFragment();
        DdaOngoingFragment = new DdaOngoingFragment();
        DdaCompletedFragment = new DdaCompletedFragment();
        adounderddo = new adounderddo();

        //Get the name and type of user from the shared preferences file to display in header view of the drawer layout
        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("typeOfUser", "");
        String username = preferences.getString("Name", "");

        viewPager2.setOffscreenPageLimit(5);

        if(getPermission()){
//            InitializeFragment(map_fragemnt_dda);
            //InitializeFragment(ado_map_fragment);
            viewPager2.setCurrentItem(0,false);
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.home_dda:
                            viewPager2.setCurrentItem(0,false);
//                            InitializeFragment(map_fragemnt_dda);
                            //InitializeFragment(map_fragemnt_dda);
                            return true;
                        case R.id.pending_dda:
                            viewPager2.setCurrentItem(1,false);
//                            InitializeFragment(DdaPendingFragment);
                            return true;
                        case R.id.ongoing_dda:
                            viewPager2.setCurrentItem(2,false);
//                            InitializeFragment(DdaOngoingFragment);
                            return true;
                        case R.id.completed_dda:
                            viewPager2.setCurrentItem(3,false);
//                            InitializeFragment(DdaCompletedFragment);
                            return true;
                        case R.id.ado_dda:
                            viewPager2.setCurrentItem(4,false);
//                            InitializeFragment(adounderddo);
                            return true;
                        default:
                            return false;
                    }
                }
            });


            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                    switch (position) {
                        case 0:
                            navigation.getMenu().findItem(R.id.home_dda).setChecked(true);
                            break;
                        case 1:
                            navigation.getMenu().findItem(R.id.pending_dda).setChecked(true);
                            break;
                        case 2:
                            navigation.getMenu().findItem(R.id.ongoing_dda).setChecked(true);
                            break;
                        case 3:
                            navigation.getMenu().findItem(R.id.completed_dda).setChecked(true);
                            break;
                        case 4:
                            navigation.getMenu().findItem(R.id.ado_dda).setChecked(true);
                            break;
                    }
                }
            });

            setupViewPager(viewPager2);

        }

    }


    private void setupViewPager(ViewPager2 viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());


        adapter.addFragment(map_fragemnt_dda);
        adapter.addFragment(DdaPendingFragment);
        adapter.addFragment(DdaOngoingFragment);
        adapter.addFragment(DdaCompletedFragment);
        adapter.addFragment(adounderddo);

        viewPager.setAdapter(adapter);
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
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt_dda()).commit();

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







//package com.theagriculture.app.Dda;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.NavigationView;
//import com.theagriculture.app.Admin.EditActivity;
//import com.theagriculture.app.R;
//import com.theagriculture.app.login_activity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class DdaActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {
//    private TextView textView;
//    private NavigationView navigationView;
//    private ImageView imageView;
//    private boolean doubleBackToExitPressedOnce = false;
//    private final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    private final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
//    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//    private final int RESULT_CODE = 786;
//    BottomNavigationView b_nav_in_dda;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dda);
//
////        Toolbar toolbar = findViewById(R.id.home_dda_toolbar);
////        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        b_nav_in_dda = findViewById(R.id.b_nav_dda);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_dda);
//        navigationView = findViewById(R.id.navofdda);
//
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
////        drawer.addDrawerListener(toggle);
////        toggle.syncState();
//    //    navigationView.setNavigationItemSelectedListener(this);
//        boolean isAssignedLocation = false;
//        try {
//            Intent intent = getIntent();
//            isAssignedLocation = intent.getBooleanExtra("isAssignedLocation", false);
//            Log.d("DDAACTIVITY", "onCreate: " + isAssignedLocation);
//            if (isAssignedLocation) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, new adounderddo()).commit();
//                navigationView.getMenu().getItem(1).setChecked(true);
//                getSupportActionBar().setTitle("ADO LIST");
//            }
//        } catch (Exception error) {
//            Log.d("DDAACTIVITY", "onCreate: INTENT ERROR" + error);
//        }
//
//        //setting header dynamically
////        View header = navigationView.getHeaderView(0);
////        textView = (TextView) header.findViewById(R.id.nameOfUserLoggedIn);
////        imageView = (ImageView) header.findViewById(R.id.profile_pic);
////        imageView.setImageResource(R.mipmap.white_logo);
//
//        final SharedPreferences preferences = getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
//        final String nameOfUser = preferences.getString("Name","");
////        textView.setText(nameOfUser.toUpperCase());
//        //close
//        if (!isAssignedLocation)
//        if (getPermission()) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt_dda()).commit();
//            navigationView.getMenu().getItem(0).setChecked(true);
//            //getSupportActionBar().setTitle("HOME");
//        }
//
//        b_nav_in_dda.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.dda_home:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt_dda()).commit();
////                        navigationView.getMenu().getItem(0).setChecked(true);
////                        Toast.makeText(DdaActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.dda_pending:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaPendingFragment()).commit();
////                        navigationView.setCheckedItem(R.id.pending_item);
////                        Toast.makeText(DdaActivity.this, "pending clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.dda_ongoing:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaOngoingFragment()).commit();
////                        navigationView.setCheckedItem(R.id.ongoing_item);
////                        Toast.makeText(DdaActivity.this, "ongoing clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.dda_completed:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaCompletedFragment()).commit();
////                        navigationView.setCheckedItem(R.id.completed_item);
////                        Toast.makeText(DdaActivity.this, "completed clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.dda_user:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new adounderddo()).commit();
////                        navigationView.getMenu().getItem(1).setChecked(true);
////                        Toast.makeText(DdaActivity.this, "user clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//    }
//
//
//    /*@Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int id = menuItem.getItemId();
//
//
//        if (id == R.id.nav_home) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt_dda()).commit();
//            navigationView.getMenu().getItem(0).setChecked(true);
//            getSupportActionBar().setTitle("HOME");
//
//        } else if(id==R.id.pending_item){
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaPendingFragment()).commit();
//            navigationView.setCheckedItem(R.id.pending_item);
//            getSupportActionBar().setTitle("Pending Locations");
//
//        }else if(id==R.id.ongoing_item){
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaOngoingFragment()).commit();
//            navigationView.setCheckedItem(R.id.ongoing_item);
//            getSupportActionBar().setTitle("Ongoing Locations");
//
//        }else if(id==R.id.completed_item){
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new DdaCompletedFragment()).commit();
//            navigationView.setCheckedItem(R.id.completed_item);
//            getSupportActionBar().setTitle("Completed Locations");
//
//        }else if(id == R.id.nav_ado){
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,new adounderddo()).commit();
//            navigationView.getMenu().getItem(1).setChecked(true);
//            getSupportActionBar().setTitle("ADO LIST");
//
//        }
//
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_dda);
//        drawerLayout.closeDrawer(GravityCompat.START);
//
//        return false;
//    }*/
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.main,menu);
////
////        // Associate searchable configuration with the SearchView
//////        SearchManager searchManager =
//////                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//////        SearchView searchView =
//////                (SearchView) menu.findItem(R.id.search).getActionView();
//////        searchView.setSearchableInfo(
//////                searchManager.getSearchableInfo(getComponentName()));
////
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_logout) {
////            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
////            editor.clear();
////            editor.commit();
////            Intent intent = new Intent(DdaActivity.this, login_activity.class);
////            startActivity(intent);
////            finish();
////            return true;
////        }
////
////        if (id == R.id.edit_profile){
////
////
////            SharedPreferences preferences = this.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
////            String pk= preferences.getString("pk","");
////            Intent intent = new Intent(this, EditActivity.class);
////            intent.putExtra("id",pk);
////            startActivity(intent);
////
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_dda);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (!doubleBackToExitPressedOnce) {
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(getApplicationContext(), "Please click BACK again to exit.", Toast.LENGTH_LONG).show();
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 3600);
//        } else {
//            super.onBackPressed();
//            }
//        }
//
//    private boolean getPermission() {
//        List<String> Permission = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            Permission.add(ACCESS_FINE_LOCATION);
//        }
//
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            Permission.add(ACCESS_COARSE_LOCATION);
//        }
//
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Permission.add(READ_EXTERNAL_STORAGE);
//        }
//
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Permission.add(WRITE_EXTERNAL_STORAGE);
//        }
//
//        if (!Permission.isEmpty()) {
//            String[] permissions = Permission.toArray(new String[Permission.size()]);
//            ActivityCompat.requestPermissions(this, permissions, RESULT_CODE);
//            return false;
//        } else
//            return true;
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == RESULT_CODE) {
//            HashMap<String, Integer> permissionResults = new HashMap<>();
//            int deniedCount = 0;
//
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    permissionResults.put(permissions[i], grantResults[i]);
//                    deniedCount++;
//                }
//            }
//
//            if (deniedCount == 0) {
//                navigationView.setCheckedItem(R.id.nav_home);
//                getSupportActionBar().setTitle("HOME");
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, new map_fragemnt_dda()).commit();
//
//            } else {
//                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
//                    String permName = entry.getKey();
//                    int permResult = entry.getValue();
//
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
//                        showDialog("", "This app needs location and files permissions to work without any problems.",
//                                "Yes, Grant permissions",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        getPermission();
//                                    }
//                                },
//                                "No, Exit app",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        finish();
//                                    }
//                                }, false);
//                    } else {
//                        showDialog("",
//                                "You have denied some permissions. Allow all the permissions at [Setting] > [Permissions]",
//                                "Go to Settings",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                                Uri.fromParts("package", getPackageName(), null));
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }, "No, Exit App",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        finish();
//                                    }
//                                }, false);
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private AlertDialog showDialog(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
//                                   String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
//                                   boolean isCancelable) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
//        builder.setMessage(msg);
//        builder.setPositiveButton(positiveLabel, positiveOnclick);
//        builder.setNegativeButton(negativeLabel, negativeOnclick);
//        builder.setCancelable(isCancelable);
//        AlertDialog alert = builder.create();
//        alert.show();
//        return alert;
//    }
//}