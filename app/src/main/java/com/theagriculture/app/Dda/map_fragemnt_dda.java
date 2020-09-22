package com.theagriculture.app.Dda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.clustering.ClusterManager;
import com.theagriculture.app.Admin.MyItem;
import com.theagriculture.app.ProfilePage;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.Context.MODE_PRIVATE;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED;

public class map_fragemnt_dda extends Fragment {
    private final String TAG = "map fragment";

    public GoogleMap map = null;
    //private String url_pending = "http://18.224.202.135/api/locations/dda/assigned";
    // private String url_unassigned = "http://18.224.202.135/api/locations/dda/unassigned";

    private String url_pending = "http://api.theagriculture.tk/api/locations/dda/assigned";
    private String url_unassigned = "http://api.theagriculture.tk/api/locations/dda/unassigned";
    private String token;
    private String next;
    private SupportMapFragment mapFragment;
    private int count = 0;
    //    private ProgressBar pbar;
    private AlertDialog dialog;

    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private ArrayList<String> villname;
    private ClusterManager<MyItem> mClusterManager;
    private CardView statsCardview;

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    BottomNavigationView navBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragemnt, container, false);

        //display app bar
        Toolbar toolbar = view.findViewById(R.id.app__bar_dda_home);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_artboard_1);

        //for title heading
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Home");
        }else {
            title_top.setText("AFL Monitoring");
        }

        //display hamburger icon
        final ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_artboard_1);
        setHasOptionsMenu(true);//tomake hamburger responsive//onOptionsItemSelected() function is defined later for the function of hamburger icon


        ////////finding bottom nav and drawer from main activity
        navBar = getActivity().findViewById(R.id.navigation_cmnd);
        mDrawer = getActivity().findViewById(R.id.drawer_layout_cmnd );
        nvDrawer = getActivity().findViewById(R.id.navigation_view_cmnd);
        mDrawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),mDrawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        {

            @Override
            public void onDrawerClosed(View drawerView)
            {
                //Toast.makeText(getActivity(),"Drwaer closed",Toast.LENGTH_SHORT).show();
                //super.onDrawerClosed(drawerView);navigation_view_cmnd
                mDrawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                //Toast.makeText(getActivity(),"Drawer open",Toast.LENGTH_SHORT).show();
                //super.onDrawerOpened(drawerView);
                mDrawer.setDrawerLockMode(LOCK_MODE_UNLOCKED);
            }


        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //get user name and type from shared prefernces file
        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", MODE_PRIVATE);
        String typeofuser = preferences.getString("typeOfUser","");
        String username = preferences.getString("Name","");
        token = preferences.getString("token", "");


        View header = nvDrawer.getHeaderView(0);
        //nvDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
        //from here
        ImageView userImage = header.findViewById(R.id.imageView);//for image click to profile
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawers();
                //Toast.makeText(getActivity(),"Synjnd",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(getActivity(), ProfilePage.class);
                startActivity(intent);
            }
        });
        //to here
        textUsername.setText(username);
        textUser.setText(typeofuser);
        //set the navigation options in drawer layout
        nvDrawer.setBackgroundColor(getResources().getColor(R.color.white));

        //set the items in drawer layout

        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout_now:
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(getActivity(), "privacy clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();

                        Intent intent = new Intent(getActivity(), login_activity.class);
                        //Intent intent = new Intent(getApplicationContext(), login_activity.class);
                        startActivity(intent);
                        getActivity().finish();
                        return true;

                    case R.id.privacy:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "privacy clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.terms:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "terms clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.help:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "help clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.advance_settings:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "settings clicked", Toast.LENGTH_SHORT).show();
                        mDrawer.closeDrawers();
                        return true;

                }
                return false;
            }
        });



        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        statsCardview = view.findViewById(R.id.stats_cardview);
        statsCardview.setVisibility(View.GONE);

        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        villname = new ArrayList<>();
//        pbar = view.findViewById(R.id.pbar);
        dialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Loading locations...")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false).build();
        dialog.show();


        next = url_pending;
        getMarkers(next);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                Log.d(TAG, "onMapReady:value if map is : " + map);

                LatLng one = new LatLng(7.798000, 68.14712);
                LatLng two = new LatLng(37.090000, 97.34466);

                LatLng shimala = new LatLng(31.104815, 77.173401);
                LatLng jaipur = new LatLng(26.912434, 75.787270);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLngBounds.Builder builder1 = new LatLngBounds.Builder();


                //add them to builder
                builder.include(one);
                builder.include(two);

                builder1.include(shimala);
                builder1.include(jaipur);

                LatLngBounds bounds = builder.build();
                LatLngBounds bounds1 = builder1.build();

                //get width and height to current display screen
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;

                // 20% padding
                int padding = (int) (width * 0.20);

                //set latlong bounds
                map.setLatLngBoundsForCameraTarget(bounds);

                //move camera to fill the bound to screen
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds1, width, height, padding));

                //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
                map.setMinZoomPreference(map.getCameraPosition().zoom);


            }
        });


        Log.d(TAG, "onCreateView: look me here " + mapFragment);
        return view;
    }

    //to open drawer when hamburger is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent intent;
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                // Toast.makeText(getActivity(),"Drawer open",Toast.LENGTH_LONG).show();
                break;

        }
        return true;
    }

    void getMarkers(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        Double lat = Double.valueOf(c.getString("latitude"));
                        Double lon = Double.valueOf(c.getString("longitude"));
                        String vill = c.getString("village_name");
                        latitude.add(lat);
                        longitude.add(lon);
                        villname.add(vill);
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
//                pbar.setVisibility(View.GONE);
                dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(jsonObjectRequest2);
        requestFinished(requestQueue);
    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(TAG, "onRequestFinished: here too");
                if (count == 0) nextRequest();
                else if (count == 1)
                    marklocation();

            }
        });

    }

    private void marklocation() {
        /*dialog.dismiss();
        for (int i = 0; i < latitude.size(); i++) {
            MarkerOptions Dlocation = new MarkerOptions().position(new LatLng(latitude.get(i), longitude.get(i))).title(villname.get(i)).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_action_name));
            map.addMarker(Dlocation);*/
           /* if (i == 0) {
//                pbar.setVisibility(View.GONE);

            }*/
        mClusterManager = new ClusterManager<MyItem>(getActivity(), map);

        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        addmarkers();

        dialog.dismiss();
    }

    private void addmarkers() {

        Log.d(TAG, "addmarkers: inside ");

        for (int i = 0; i < latitude.size(); i++) {
            double lat = latitude.get(i);
            double lon = longitude.get(i);
            String title = villname.get(i);

            MyItem item = new MyItem(lat, lon, title);

            mClusterManager.addItem(item);


        }

        mClusterManager.cluster();

    }

    void nextRequest() {
        count = 1;
        next= url_unassigned;
        getMarkers(next);
    }

    BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}









//package com.theagriculture.app.Dda;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.cardview.widget.CardView;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.material.navigation.NavigationView;
//import com.google.maps.android.clustering.ClusterManager;
//import com.theagriculture.app.Admin.MyItem;
//import com.theagriculture.app.Globals;
//import com.theagriculture.app.R;
//import com.theagriculture.app.login_activity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.microedition.khronos.opengles.GL;
//
//import dmax.dialog.SpotsDialog;
//
//import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
//import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
//
//public class map_fragemnt_dda extends Fragment {
//    private final String TAG = "map fragment";
//
//    public GoogleMap map = null;
//    private String url_pending = Globals.assignedLocationsDDA;                  //"http://api.theagriculture.tk/api/locations/dda/assigned";
//    private String url_unassigned = Globals.unassignedLocationsDDA;             //"http://api.theagriculture.tk/api/locations/dda/unassigned";
//    private String token;
//    private String next;
//    private SupportMapFragment mapFragment;
//    private int count = 0;
//    //    private ProgressBar pbar;
//    private AlertDialog dialog;
//
//    private ArrayList<Double> latitude;
//    private ArrayList<Double> longitude;
//    private ArrayList<String> villname;
//    private ClusterManager<MyItem> mClusterManager;
//    private CardView statsCardview;
//
//    private DrawerLayout mDrawer_dda;
//    private NavigationView nvDrawer_dda;
//
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                mDrawer_dda.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.map_fragemnt, container, false);
//
//        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
//        statsCardview = view.findViewById(R.id.stats_cardview);
//        statsCardview.setVisibility(View.GONE);
//
//        latitude = new ArrayList<>();
//        longitude = new ArrayList<>();
//        villname = new ArrayList<>();
////        pbar = view.findViewById(R.id.pbar);
//
//        mDrawer_dda = getActivity().findViewById(R.id.drawer_layout_dda);
//        nvDrawer_dda = getActivity().findViewById(R.id.navofdda);
//        mDrawer_dda.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
//
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
//                getActivity(),mDrawer_dda,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
//        {
//            @Override
//            public void onDrawerClosed(View drawerView)
//            {
//                //Toast.makeText(getActivity(),"Drwaer closed",Toast.LENGTH_SHORT).show();
//                //super.onDrawerClosed(drawerView);
//                mDrawer_dda.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView)
//            {
//                //Toast.makeText(getActivity(),"Drawer open",Toast.LENGTH_SHORT).show();
//                //super.onDrawerOpened(drawerView);
//                mDrawer_dda.setDrawerLockMode(LOCK_MODE_UNLOCKED);
//            }
//        };
//        mDrawer_dda.addDrawerListener(mDrawerToggle);
//        mDrawerToggle.syncState();
//
//        Toolbar toolbar = view.findViewById(R.id.map_dda_toolbar);
//        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
//        appCompatActivity.setSupportActionBar(toolbar);
//        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        setHasOptionsMenu(true);
//        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_artboard_1);
//        //for title heading
//        TextView title_top = view.findViewById(R.id.app_name);
//        if (view.isEnabled()){
//            title_top.setText("Home");
//        }else {
//            title_top.setText("AFL Monitoring");
//        }
//
//        dialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Loading locations...")
//                .setTheme(R.style.CustomDialog)
//                .setCancelable(false).build();
//        dialog.show();
//
//        next = url_pending;
//        getMarkers(next);
//
//        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
//        token = preferences.getString("token", "");
//
//        View header = nvDrawer_dda.getHeaderView(0);
//        TextView textUsername = header.findViewById(R.id.name);
//        TextView textUser = header.findViewById(R.id.type_of_user);
//        String typeofuser = preferences.getString("typeOfUser","");
//        String username = preferences.getString("Name","");
//        textUsername.setText(username);
//        textUser.setText(typeofuser);
//
//        nvDrawer_dda.setBackgroundColor(getResources().getColor(R.color.white));
//        nvDrawer_dda.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()){
//
//                    case R.id.logout_now:
//                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
//                        editor.clear();
//                        editor.commit();
//                        mDrawer_dda.closeDrawers();
//                        Intent intent = new Intent(getActivity(), login_activity.class);
//                        //Intent intent = new Intent(getApplicationContext(), login_activity.class);
//                        startActivity(intent);
//                        getActivity().finish();
//                        return true;
//
//                    case R.id.privacy:
//                        mDrawer_dda.closeDrawers();
//                        Toast.makeText(getActivity(), "privacy clicked", Toast.LENGTH_SHORT).show();
//                        return true;
//
//                    case R.id.terms:
//                        item.setChecked(true);
//                        Toast.makeText(getActivity(), "terms clicked", Toast.LENGTH_SHORT).show();
//                        mDrawer_dda.closeDrawers();
//                        return true;
//
//                    case R.id.help:
//                        item.setChecked(true);
//                        Toast.makeText(getActivity(), "help clicked", Toast.LENGTH_SHORT).show();
//                        mDrawer_dda.closeDrawers();
//                        return true;
//
//                    case R.id.advance_settings:
//                        item.setChecked(true);
//                        Toast.makeText(getActivity(), "settings clicked", Toast.LENGTH_SHORT).show();
//                        mDrawer_dda.closeDrawers();
//                        return true;
//
//
//                }
//                return false;
//            }
//        });
//
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                map = googleMap;
//                Log.d(TAG, "onMapReady:value if map is : " + map);
//
//                LatLng one = new LatLng(7.798000, 68.14712);
//                LatLng two = new LatLng(37.090000, 97.34466);
//
//                LatLng shimala = new LatLng(31.104815, 77.173401);
//                LatLng jaipur = new LatLng(26.912434, 75.787270);
//
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
//
//
//                //add them to builder
//                builder.include(one);
//                builder.include(two);
//
//                builder1.include(shimala);
//                builder1.include(jaipur);
//
//                LatLngBounds bounds = builder.build();
//                LatLngBounds bounds1 = builder1.build();
//
//                //get width and height to current display screen
//                int width = getResources().getDisplayMetrics().widthPixels;
//                int height = getResources().getDisplayMetrics().heightPixels;
//
//                // 20% padding
//                int padding = (int) (width * 0.20);
//
//                //set latlong bounds
//                map.setLatLngBoundsForCameraTarget(bounds);
//
//                //move camera to fill the bound to screen
//                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds1, width, height, padding));
//
//                //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
//                map.setMinZoomPreference(map.getCameraPosition().zoom);
//
//
//            }
//        });
//
//
//        Log.d(TAG, "onCreateView: look me here " + mapFragment);
//        return view;
//    }
//
//    void getMarkers(String url) {
//        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//
//        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                    JSONArray jsonArray = jsonObject.getJSONArray("results");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject c = jsonArray.getJSONObject(i);
//                        Double lat = Double.valueOf(c.getString("latitude"));
//                        Double lon = Double.valueOf(c.getString("longitude"));
//                        String vill = c.getString("village_name");
//                        latitude.add(lat);
//                        longitude.add(lon);
//                        villname.add(vill);
//                    }
//                    dialog.dismiss();
//                } catch (JSONException e) {
//                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
//                    e.printStackTrace();
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), "Please try again!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "onErrorResponse: " + error);
////                pbar.setVisibility(View.GONE);
//                dialog.dismiss();
//                if (error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
//                }
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("Authorization", "Token " + token);
//                return map;
//            }
//        };
//        jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        requestQueue.add(jsonObjectRequest2);
//        requestFinished(requestQueue);
//    }
//
//    private void requestFinished(RequestQueue queue) {
//
//        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//
//            @Override
//            public void onRequestFinished(Request<Object> request) {
//                Log.d(TAG, "onRequestFinished: here too");
//                if (count == 0) nextRequest();
//                else if (count == 1)
//                marklocation();
//
//            }
//        });
//
//    }
//
//    private void marklocation() {
//        /*dialog.dismiss();
//        for (int i = 0; i < latitude.size(); i++) {
//            MarkerOptions Dlocation = new MarkerOptions().position(new LatLng(latitude.get(i), longitude.get(i))).title(villname.get(i)).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_action_name));
//            map.addMarker(Dlocation);*/
//           /* if (i == 0) {
////                pbar.setVisibility(View.GONE);
//
//            }*/
//        mClusterManager = new ClusterManager<MyItem>(getActivity(), map);
//
//        map.setOnCameraIdleListener(mClusterManager);
//        map.setOnMarkerClickListener(mClusterManager);
//
//        addmarkers();
//
//        dialog.dismiss();
//        }
//
//    private void addmarkers() {
//
//        Log.d(TAG, "addmarkers: inside ");
//
//        for (int i = 0; i < latitude.size(); i++) {
//            double lat = latitude.get(i);
//            double lon = longitude.get(i);
//            String title = villname.get(i);
//
//            MyItem item = new MyItem(lat, lon, title);
//
//            mClusterManager.addItem(item);
//
//
//        }
//
//        mClusterManager.cluster();
//
//    }
//
//    void nextRequest() {
//        count = 1;
//      next= url_unassigned;
//      getMarkers(next);
//    }
//
//    BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
//
//}
