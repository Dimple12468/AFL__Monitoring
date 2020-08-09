package com.theagriculture.app.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.theagriculture.app.PrivacyPolicy;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import dmax.dialog.SpotsDialog;

import static com.theagriculture.app.AppNotificationChannels.CHANNEL_2_ID;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class map_fragemnt extends Fragment implements OnMapReadyCallback {//OnMapReady() function gives a call back to this OnMap ReadyCallback

    GoogleMap ngoogleMap;
    MapView mMapView;
    View mView;
    private File csvFile;
    private AlertDialog uploadingDialog;
    private LinearLayout for_upload,for_location,for_email,for_cancel;
    private NotificationManagerCompat manager;
    private String token;

    private PrivacyPolicy privacyPolicy;
    private DrawerLayout mDrawer_map;
    private NavigationView nvDrawer_map;
//    BottomNavigationView b_nav_map;
    private map_fragemnt mapFragmnt;
    private location_fragment locationFragment;
    private ado_fragment adoFragment;
    private ddo_fragment ddoFragment;
    private DistrictStateFragment districtStateFragment;


    //bottom_nav bottom_nav_map;

    public map_fragemnt() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);
        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        searchItem.setVisible(false);
        /*final SearchView searchView = (SearchView) searchItem.getActionView();
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
                **if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();
                }
                adapter.getFilter().filter(newText);**
                return true;
            }
        });*/
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //Snackbar.make(mView,"Hamburger icon clicked",Snackbar.LENGTH_LONG).show();
                //Toast.makeText(getActivity(), "Hamburger icon clicked", Toast.LENGTH_SHORT).show();
                mDrawer_map.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Snackbar.make(mView,"Hamburger icon clicked",Snackbar.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Hamburger icon clicked", Toast.LENGTH_SHORT).show();
                System.out.println("dimple looking for hamburger icon");
                //mDrawer_map.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map_admin, container, false);
        //getContext().getTheme().applyStyle(R.style.AppTheme, true);

        //setHasOptionsMenu(true);

        //ImageView iv = mView.findViewById(R.id.ham);
        //iv.setVisibility(View.GONE);
        /*ImageView iv2 = mView.findViewById(R.id.se1);
        iv2.setVisibility(View.INVISIBLE);*/
        mDrawer_map = mView.findViewById(R.id.drawer_map);
        nvDrawer_map = mView.findViewById(R.id.navigation_view_map);
        privacyPolicy = new PrivacyPolicy();
//        b_nav_map = mView.findViewById(R.id.bottom_nav_for_map);

        mapFragmnt = new map_fragemnt();
        locationFragment = new location_fragment();
        adoFragment = new ado_fragment();
        ddoFragment = new ddo_fragment();
        districtStateFragment= new DistrictStateFragment();
        //bottom_nav_map = new bottom_nav();
        //bottom_nav_map.bottom_navigation_admin();

        Toolbar toolbar = (Toolbar) mView.findViewById(R.id.app__bar_map);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_artboard_1);
        setHasOptionsMenu(true);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Bottom_nav_adapter adapt = new Bottom_nav_adapter(getContext(),mapFragmnt,R.id.adminshome);


/*        b_nav_map.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.adminshome:
                        InitializeFragment(mapFragmnt);
                        return true;
                    case R.id.adminslocation:
                        // InitializeFragment(locationFragment);
                        Toast.makeText(getActivity(), "locations clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.adminsado:
                        Toast.makeText(getActivity(), "ado clicked", Toast.LENGTH_SHORT).show();
                        //InitializeFragment(adoFragment);
                        return true;
                    case R.id.adminsdda:
                        //InitializeFragment(ddoFragment);
                        Toast.makeText(getActivity(), "dda clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.adminsdistrict_state:
                        // InitializeFragment(districtStateFragment);
                        Toast.makeText(getActivity(), "stats clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        // title_top.setText("AFL Monitoring");
                        return false;
                }
            }
        });*/

        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");

        View header = nvDrawer_map.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
        String typeofuser = prefs.getString("typeOfUser","");
        String username = prefs.getString("Name","");
        textUsername.setText(username);
        textUser.setText(typeofuser);

        nvDrawer_map.setBackgroundColor(getResources().getColor(R.color.white));
        nvDrawer_map.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.logout_now:
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        mDrawer_map.closeDrawers();
                        Intent intent = new Intent(getActivity(), login_activity.class);
                        //Intent intent = new Intent(getApplicationContext(), login_activity.class);
                        startActivity(intent);
                        getActivity().finish();
                        return true;

                    case R.id.privacy:
                        mDrawer_map.closeDrawers();
                        InitializeFragment(privacyPolicy);
                        return true;

                    case R.id.terms:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "terms clicked", Toast.LENGTH_SHORT).show();
                        mDrawer_map.closeDrawers();
                        return true;

                    case R.id.help:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "help clicked", Toast.LENGTH_SHORT).show();
                        mDrawer_map.closeDrawers();
                        return true;

                    case R.id.advance_settings:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "settings clicked", Toast.LENGTH_SHORT).show();
                        mDrawer_map.closeDrawers();
                        return true;


                }
                return false;
            }
        });



        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        //todo fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(mView,"FAB OK HAI",Snackbar.LENGTH_LONG).show();

                final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.activity_fab_onclick, null);
                mBottomDialogNotificationAction.setContentView(sheetView);
                //mBottomDialogNotificationAction.setCancelable(false);
                mBottomDialogNotificationAction.show();

                // Remove default white color background
                FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheet.setBackground(null);

                for_upload = (LinearLayout) sheetView.findViewById(R.id.for_upload);
                for_location = (LinearLayout) sheetView.findViewById(R.id.for_location);
                for_email = (LinearLayout) sheetView.findViewById(R.id.for_email);
                for_cancel = (LinearLayout) sheetView.findViewById(R.id.for_cancel);

                final String url_location = "http://18.224.202.135/api/upload/locations/";
                final String url_bulk = "http://18.224.202.135/api/upload/mail/";


                //for_upload.setOnClickListener(new View.OnClickListener() {
                  //  @Override
                   // public void onClick(View v) {
                     //   Snackbar.make(mView,"Upload here",Snackbar.LENGTH_LONG).show();
                    //}
                //});



                for_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCsvPicker(url_location);
                        //Snackbar.make(mView,"for location",Snackbar.LENGTH_LONG).show();
                    }
                });

                for_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCsvPicker(url_bulk);
                        //Snackbar.make(mView,"email here",Snackbar.LENGTH_LONG).show();
                    }
                });
                manager = NotificationManagerCompat.from(getActivity());

                for_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Snackbar.make(mView,"Cancel here",Snackbar.LENGTH_LONG).show();
                        mBottomDialogNotificationAction.dismiss();
                    }
                });


            }
        });
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        ngoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //googleMap.getUiSettings().setMapToolbarEnabled(false);
        //change location of google map toolbar
        View toolbar = ((View) mMapView.findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("4"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        rlp.setMargins(0, 30, 30, 0);


        googleMap.addMarker(new MarkerOptions().position(new LatLng(20.5937, 78.9629)).title("India").snippet("My country"));
        CameraPosition India = CameraPosition.builder().target(new LatLng(20.5937, 78.9629)).zoom(10).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(India));
    }

    private void openCsvPicker(final String url) {
        File file = Environment.getExternalStorageDirectory();
        String start = file.getAbsolutePath();
        new ChooserDialog(getActivity())
                .withStartFile(start)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String s, File file) {
                        csvFile = file;
                        uploadingDialog = new SpotsDialog.Builder().setContext(getActivity())
                                .setMessage("Uploading Csv...")
                                .setCancelable(false)
                                .setTheme(R.style.CustomDialog)
                                .build();
                        uploadingDialog.show();
                        uploadCsv(url);
                    }
                })
                .withOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                })
                .build()
                .show();
    }

    private void uploadCsv(String url) {
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("Uploading Csv")
                .setContentText("0/100")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress((int) csvFile.length(), 0, false);
        manager.notify(2, notificationBuilder.build());
        AndroidNetworking.upload(url)
                .addHeaders("Authorization", "Token " + token)
                .addMultipartFile("location_csv", csvFile)
                .setTag("Upload Csv")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //Log.d(TAG, "onProgress: " + bytesUploaded);
                        notificationBuilder.setContentText(((int) (bytesUploaded / totalBytes) * 100) + "/100")
                                .setProgress((int) totalBytes, (int) ((bytesUploaded / totalBytes) * 100), false);
                        manager.notify(2, notificationBuilder.build());

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "onResponse: " + response);
                        String count;
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            count = rootObject.getString("count");
                            Toast.makeText(getActivity(), "Successfully Uploaded " + count + " locations", Toast.LENGTH_LONG).show();
                            uploadingDialog.dismiss();
                            notificationBuilder.setContentText("Upload Successful!")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false);
                            manager.notify(2, notificationBuilder.build());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            uploadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.d(TAG, "onError: " + anError.getErrorDetail() + " " + anError.getErrorBody() +" " + anError.getMessage() + " " + anError.getErrorCode());
                        Toast.makeText(getActivity(), "Sorry something went wrong, please try again!",
                                Toast.LENGTH_LONG).show();
                        notificationBuilder.setContentText("Upload Failed!")
                                .setProgress(0, 0, false)
                                .setOngoing(false);
                        manager.notify(2, notificationBuilder.build());
                        uploadingDialog.dismiss();
                    }
                });
    }

    public void InitializeFragment(Fragment fragment) {


        AppCompatActivity activity = (AppCompatActivity) fragment.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack(null).commit();


    }
}
