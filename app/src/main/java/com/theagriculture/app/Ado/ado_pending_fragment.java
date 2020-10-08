package com.theagriculture.app.Ado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Admin.AdoDdoActivity.nothing_toshow_fragment;
import com.theagriculture.app.Admin.SectionAdapter;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

import static com.android.volley.VolleyLog.TAG;

public class ado_pending_fragment extends Fragment {

    public static Location userLocation;
    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private RecyclerView recyclerView;
    //private AdoListAdapter adoListAdapter;
    private ArrayList<String> longitude;
    private ArrayList<String> latitude;
    private ArrayList<String> idList;
    private String url = Globals.adoPending;                        //"http://api.theagriculture.tk/api/locations/ado/pending";
    private String nextUrl;
    private boolean isNextBusy = false;
    View view;
    private boolean isRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SectionAdapter_ado adoListAdapter;
    ArrayList<Section_ado> sections = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ProgressBar spinner,nextPageSpinner;
    boolean doubleBackToExitPressedOnce = false;

    MenuItem searchItem;
    MenuItem searchItem_filter;

    //public Location userLocation;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);



        searchItem = menu.findItem(R.id.search_in_title);
        searchItem_filter = menu.findItem(R.id.filter);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search something");

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchItem_filter.setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchItem_filter.setVisible(false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                //customadapter.getFilter().filter(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();

                }
                //customadapter.getFilter().filter(newText);

                 */
                return true;
            }
        });
        searchItem_filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // alert_filter_dialog();
                return true;
            }


        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ado_pending_fragment,container,false);
        mtextview1 = new ArrayList<>();
        mtextview2 = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();
        idList = new ArrayList<>();
        isRefresh = false;

        if(!checkIfLocationEnabled()) {
            Toast.makeText(getActivity(), "You need to enable locations to further use the app", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        else
            getUserLocation();



        Toolbar toolbar = view.findViewById(R.id.app__bar_ado_pending);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Pending");
        }else {
            title_top.setText("AFL Monitoring");
        }

        final ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);//to display search icon

        recyclerView = view.findViewById(R.id.ado_pending_rv);

        spinner = view.findViewById(R.id.ado_pending_progress);
        spinner.setVisibility(View.VISIBLE);
        nextPageSpinner = view.findViewById(R.id.ado_pending_nextpage);
        nextPageSpinner.setVisibility(View.GONE);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull9);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(ado_pending_fragment.this).attach(ado_pending_fragment.this).commit();
            }
        });

        Log.d("pending", "onCreateView: ");
        //add data in the array with load data
        /*
        getData(url);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.ado_pending_rv);
        adoListAdapter = new AdoListAdapter(getContext(), mtextview1, mtextview2, idList);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adoListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

         */

        /*ye theek hai
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();
        getData(url);

         */
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        /////////////////////////////
        getData(url);
        Log.d(TAG, "onResponse retured from get data ");
        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);

        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();


        return view;
    }


    private void getData(final String url) {
        sections=new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        Log.d(TAG, "onResponse0: ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();

                        try {
                            Log.d(TAG, "onResponse1: ");
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            //Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            Log.d(TAG, "onResponse2: ");
                            if(resultsArray.length()== 0){
                                Log.d(TAG, "onResponse3: ");
                                //adoListAdapter.mshowshimmer = false;
                                adoListAdapter.notifyDataSetChanged();
                                nothing_toshow_fragment abc = new nothing_toshow_fragment();
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nothing,abc).commit();

                                //view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }
                            Log.d(TAG, "onResponse4: ");
                            String[][] arr = new String[6][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                //,(comma) is necessary for empty string out of range exception
                                String dlocation_name = singleObject.getString("village_name") + ", "+singleObject.getString("block");
                                String dlocation_address = singleObject.getString("district") + ", " + singleObject.getString("state") ;
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                //done to check dda.......all null
                                /*
                                try {
                                    Toast.makeText(getActivity(), singleObject.getJSONObject("dda").toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),"exce[tion "+i,Toast.LENGTH_LONG).show();
                                }

                                 */
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                            }
                            String inter;
                            for(int i=0;i<resultsArray.length()-1;i++){
                                for(int j=0;j<resultsArray.length()-i-1;j++){
                                    String idate = arr[0][j];
                                    String ndate = arr[0][j+1];
                                    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                                    // Get the two dates to be compared
                                    Date d1 = null;
                                    try {
                                        d1 = sdfo.parse(idate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date d2 = null;
                                    try {
                                        d2 = sdfo.parse(ndate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (d1.compareTo(d2) < 0) {
                                        for(int k=0;k<6;k++) {
                                            inter = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = inter;
                                        }
                                    }
                                }
                            }
                            ArrayList<String> mDid = new ArrayList<>();
                            ArrayList<String> mDlocation_name = new ArrayList<>();
                            ArrayList<String> mDlocation_address = new ArrayList<>();
                            ArrayList<String> mlatitude= new ArrayList<>();
                            ArrayList<String> mlongitude= new ArrayList<>();
                            String predate=arr[0][0];
                            //String predate = null;
                            for(int i=0;i<resultsArray.length();i++){
                                String idate = arr[0][i];
                                if(predate.equals(idate)){
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);

                                    //predate=idate;
                                }
                                else{
                                    sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,true,false));
                                    mDid = new ArrayList<>();
                                    mDlocation_name = new ArrayList<>();
                                    mDlocation_address = new ArrayList<>();
                                    mlatitude = new ArrayList<>();
                                    mlongitude = new ArrayList<>();
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
                                    //date.equals(idate);
                                }
                                //predate.equals(idate);
                                predate=idate;
                            }
                            Log.d(TAG, "onResponse8a: ");
                            sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,true,false));
                            Log.d(TAG, "onResponse8b: ");
                            //Toast.makeText(getActivity(),arr.toString(),Toast.LENGTH_LONG).show();
                            //adoListAdapter.mshowshimmer = false;
                            adoListAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse8c: ");
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);
                            nextPageSpinner.setVisibility(View.GONE);
                            Log.d(TAG, "onResponse8d: ");
                            //Toast.makeText(getActivity(),"visibiliy gone",Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.GONE);
                            nextPageSpinner.setVisibility(View.GONE);
                            //Fragment fragment = getFragmentManager().findFragmentById(R.id.rootView);
                            //fragment.getView().setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ///// Toast.makeText(getActivity(),"error is "+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            // Toast.makeText(getActivity(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                            final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                            mBottomDialogNotificationAction.setContentView(sheetView);
                            mBottomDialogNotificationAction.setCancelable(false);
                            mBottomDialogNotificationAction.show();
                            // Remove default white color background

                            FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                            bottomSheet.setBackground(null);


                            TextView close = sheetView.findViewById(R.id.close);
                            Button retry = sheetView.findViewById(R.id.retry);
                            retry.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mBottomDialogNotificationAction.dismiss();
                                    spinner.setVisibility(View.VISIBLE);
                                    getFragmentManager().beginTransaction().detach(ado_pending_fragment.this).attach(ado_pending_fragment.this).commit();
                                    //getData(url);
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!doubleBackToExitPressedOnce) {
                                        doubleBackToExitPressedOnce = true;
                                        Toast toast = Toast.makeText(getActivity(), "Tap on Close App again to exit app", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                doubleBackToExitPressedOnce = false;
                                            }
                                        }, 3600);
                                    } else {
                                        mBottomDialogNotificationAction.dismiss();
                                        Intent a = new Intent(Intent.ACTION_MAIN);//will exit app
                                        a.addCategory(Intent.CATEGORY_HOME);
                                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(a);
                                    }
                                }

                            });
                        }
                        else
                            Toast.makeText(getActivity(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();

                        isNextBusy = false;
                        Log.d(TAG, "onErrorResponse: " + error);
                        spinner.setVisibility(View.GONE);
                        nextPageSpinner.setVisibility(View.GONE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "onResponse9: ");
        ///////////

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = linearLayoutManager.getItemCount();
                    pastItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy) {
                            nextPageSpinner.setVisibility(View.VISIBLE);
                            getData(nextUrl);
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // ///////

    }

    public void getUserLocation(){
        SmartLocation.with(getActivity()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                userLocation = location;
            }
        });
    }

    public boolean checkIfLocationEnabled(){
        //Toast.makeText(getApplicationContext(),"enterd",Toast.LENGTH_LONG).show();
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(getActivity(),"An exception occurred while checking GPS Location ",Toast.LENGTH_SHORT);
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(getActivity(),"An exception occurred while checking Network Location ",Toast.LENGTH_SHORT);
        }

//        Toast.makeText(getApplicationContext(),gps_enabled+" and "+network_enabled,Toast.LENGTH_LONG).show();
        if(!gps_enabled && !network_enabled) {
            //Toast.makeText(getApplicationContext(),"Enable your location",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            //Toast.makeText(getApplicationContext(),"Location is enabled",Toast.LENGTH_SHORT).show();
            return true;

        }
    }

    //Not working
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
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

     */


    /*

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        isRefresh = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        isRefresh = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (isRefresh) {
            getFragmentManager().beginTransaction().detach(ado_pending_fragment.this)
                    .attach(ado_pending_fragment.this).commit();
            Log.d(TAG, "onResume: REFRESH");
            isRefresh = false;
        }
    }

     */
}