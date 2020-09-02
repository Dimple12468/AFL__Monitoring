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
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.clustering.ClusterManager;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.theagriculture.app.Globals;
import com.theagriculture.app.PrivacyPolicy;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
import static com.theagriculture.app.AppNotificationChannels.CHANNEL_2_ID;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class map_fragemnt extends Fragment /*implements OnMapReadyCallback*/ {//OnMapReady() function gives a call back to this OnMap ReadyCallback

    GoogleMap ngoogleMap;
    MapView mMapView;
    View mView;
    private File csvFile;
    private AlertDialog uploadingDialog;
    private LinearLayout for_upload,for_location,for_email,for_cancel;
    private NotificationManagerCompat manager;
    private String token;
    private SupportMapFragment mapFragment_admin;

    private PrivacyPolicy privacyPolicy;
    private DrawerLayout mDrawer_map;
    private NavigationView nvDrawer_map;
//    BottomNavigationView b_nav_map;
    private map_fragemnt mapFragmnt;
    private location_fragment locationFragment;
    private ado_fragment adoFragment;
    private ddo_fragment ddoFragment;
    private DistrictStateFragment districtStateFragment;
    private final String TAG = "map_fragment admin";

    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private ArrayList<String> villname;

    private String url_unassigned;          //="http://api.theagriculture.tk/api/locations/unassigned";
    private String url_assigned;            //="http://api.theagriculture.tk/api/locations/assigned";
    private String url_count;               //="http://api.theagriculture.tk/api/count-reports/";
    private String next;
    public GoogleMap map = null;
    private AlertDialog dialog;
    private RequestQueue requestQueue;
    private int count = 0 ;
    private ClusterManager<MyItem> mClusterManager;

    private TextView pendingView;
    private TextView ongoingView;
    private TextView completedView;

    private CardView statsCardview;


    //bottom_nav bottom_nav_map;

    public map_fragemnt() {
        // Required empty public constructor
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        mMapView = (MapView) mView.findViewById(R.id.map);
//        if (mMapView != null) {
//            mMapView.onCreate(null);
//            mMapView.onResume();
//            mMapView.getMapAsync(this);
//
//        }
//    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);
        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        searchItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer_map.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //on createView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_map_admin, container, false);
        mapFragment_admin = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_admin));
//        setHasOptionsMenu(true);
       // getContext().getTheme().applyStyle(R.style.AppTheme, true);

        statsCardview = mView.findViewById(R.id.stats_cardview_admin);
        statsCardview.setVisibility(View.GONE);
//        Button fab = mView.findViewById(R.id.fab);

        url_unassigned = Globals.map_Unassigned_Admin;
        url_assigned = Globals.map_Assigned_Admin;
        url_count = Globals.map_Count_Admin;
        Log.d(TAG,url_unassigned);
        Log.d(TAG,url_assigned);
        Log.d(TAG,url_count);

        TextView title_top = mView.findViewById(R.id.app_name);
        if (mView.isEnabled()){
            title_top.setText("Home");
        }else {
            title_top.setText("AFL Monitoring");
        }


        mDrawer_map = getActivity().findViewById(R.id.drawer_view);                         //mView.findViewById(R.id.drawer_map);
        nvDrawer_map = getActivity().findViewById(R.id.navigation_view);                    //mView.findViewById(R.id.navigation_view_map);
        mDrawer_map.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),mDrawer_map,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                //super.onDrawerClosed(drawerView);
                mDrawer_map.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                //super.onDrawerOpened(drawerView);
                mDrawer_map.setDrawerLockMode(LOCK_MODE_UNLOCKED);
            }
        };
        mDrawer_map.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        privacyPolicy = new PrivacyPolicy();
        pendingView = mView.findViewById(R.id.pending_count_admin);
        ongoingView = mView.findViewById(R.id.ongoing_count_admin);
        completedView = mView.findViewById(R.id.completed_count_admin);
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



        final SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        villname = new ArrayList<>();

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Loading locations...")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false).build();
        dialog.show();
        next = url_assigned;

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
                        Toast.makeText(getActivity(), "privacy clicked", Toast.LENGTH_SHORT).show();
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

        mapFragment_admin.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                Log.d(TAG, "onMapReady:value if map is : " + map);

                LatLng one = new LatLng(7.798000, 68.14712);
                LatLng two = new LatLng(37.090000, 97.34466);

                LatLng shimala = new LatLng(31.104815,77.173401);
                LatLng jaipur = new LatLng(26.912434,75.787270);

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

               // mMapView = (MapView) mView.findViewById(R.id.map_admin);
                View toolbar = ((View) mapFragment_admin.getView().findViewById(Integer.parseInt("1")).
                        getParent()).findViewById(Integer.parseInt("4"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
                // position on right bottom
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                rlp.setMargins(0, 30, 30, 0);

            }
        });

        Log.d(TAG, "onCreateView: look me here " + mapFragment_admin);

        getCount(url_count);
        getMarkers(next);

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

                final String url_location = Globals.url_Location_Admin;         //="http://api.theagriculture.tk/api/upload/locations/";
                final String url_bulk = Globals.url_Bulk_Admin;                 //="http://api.theagriculture.tk/api/upload/mail/";


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
                    }
                });

                for_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCsvPicker(url_bulk);
                    }
                });
                manager = NotificationManagerCompat.from(getActivity());

                for_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomDialogNotificationAction.dismiss();
                    }
                });


            }
        });
        return mView;
    }

    void getCount(String urlcount){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, urlcount, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pendingView.setText(response.getString("pending_count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    ongoingView.setText(response.getString("ongoing_count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    completedView.setText(response.getString("completed_count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
//                pbar.setVisibility(View.GONE);
                dialog.dismiss();
                if(error instanceof NoConnectionError){
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        jsonObjectRequest2.setTag("MAP REQUEEST");
        requestQueue.add(jsonObjectRequest2);
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


    }

    void getMarkers(String url) {
        requestQueue = Volley.newRequestQueue(getContext());
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

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
//                pbar.setVisibility(View.GONE);
                dialog.dismiss();
                if(error instanceof NoConnectionError){
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
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
        jsonObjectRequest2.setTag("MAP REQUEST");
        requestQueue.add(jsonObjectRequest2);
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
        requestFinished(requestQueue);
    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(TAG, "onRequestFinished: here too");
                if(count == 0)nextRequest();
                else if(count == 1) marklocation();

            }
        });

    }

    void nextRequest(){
        count = 1;
        next= url_unassigned;
        getMarkers(next);


    }

    private void marklocation() {
        Log.d(TAG, "marklocation: SIZE" + latitude.size());
        mClusterManager = new ClusterManager<MyItem>(getActivity(), map);
        addmarkers();
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
        dialog.dismiss();
    }

    private void addmarkers() {

        for(int i = 0 ;i<latitude.size();i++){
            double lat = latitude.get(i);
            double lon = longitude.get(i);
            String title = villname.get(i);
            MyItem item = new MyItem(lat,lon,title);
            mClusterManager.addItem(item);
        }
        mClusterManager.cluster();
        Log.d(TAG, "addmarkers: CLUSTER SIZE" + mClusterManager.getRenderer());

    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        MapsInitializer.initialize(getContext());
//        ngoogleMap = googleMap;
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        //googleMap.getUiSettings().setMapToolbarEnabled(false);
//        //change location of google map toolbar
//        View toolbar = ((View) mMapView.findViewById(Integer.parseInt("1")).
//                getParent()).findViewById(Integer.parseInt("4"));
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
//        // position on right bottom
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
//        rlp.setMargins(0, 30, 30, 0);
//
//
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(20.5937, 78.9629)).title("India").snippet("My country"));
//        CameraPosition India = CameraPosition.builder().target(new LatLng(20.5937, 78.9629)).zoom(10).bearing(0).tilt(45).build();
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(India));
//    }

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
