package com.theagriculture.app.Ado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ado_map_fragment extends Fragment implements OnMapReadyCallback {//OnMapReady() function gives a call back to this OnMap ReadyCallback

    GoogleMap ngoogleMap;
    MapView mMapView;
    String lat_string,long_string;
    View mView;
    private double longitude;
    private double latitude;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;



    public ado_map_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mMapView = (MapView) mView.findViewById(R.id.map_ado);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.ado_map_fragment, container, false);


        //display app bar
        Toolbar toolbar = mView.findViewById(R.id.app__bar_ado_home);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //display hamburger icon
        final ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_artboard_1);
        setHasOptionsMenu(true);//tomake hamburger responsive

        mDrawer = mView.findViewById(R.id.drawer_view_ado);
        nvDrawer = mView.findViewById(R.id.navigation_view_ado);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", MODE_PRIVATE);
        String typeofuser = preferences.getString("typeOfUser","");
        String username = preferences.getString("Name","");

        View header = nvDrawer.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
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



        //Toast.makeText(getActivity(),lat_string + " and "+long_string,Toast.LENGTH_LONG).show();
        lat_string = "20.59";
        long_string="78.234";
        longitude = Double.parseDouble(long_string);
        latitude = Double.parseDouble(lat_string);

        /*

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab_ado);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mView,"FAB OK HAI",Snackbar.LENGTH_LONG).show();
            }
        });

         */
        return mView;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent intent;
        switch (item.getItemId()) {

            case android.R.id.home:
                //Toast.makeText(getActivity(),"You clicked",Toast.LENGTH_LONG).show();
                mDrawer.openDrawer(GravityCompat.START);
                break;

        }
        return true;
    }


    /*did not work
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

     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        ngoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Location"));//snippet("My country"));
        CameraPosition India = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(10).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(India));
    }

}
