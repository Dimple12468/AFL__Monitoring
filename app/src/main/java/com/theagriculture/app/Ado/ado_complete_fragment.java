package com.theagriculture.app.Ado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Admin.AdoDdoActivity.nothing_toshow_fragment;
import com.theagriculture.app.Admin.CustomDialog;
import com.theagriculture.app.Admin.RecyclerViewAdapter_district;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;
import com.theagriculture.app.ShowFilterDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ado_complete_fragment extends Fragment {

    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private RecyclerView recyclerView;
    private SectionAdapter_ado adoListAdapter;
    private ArrayList<String> longitude;
    private ArrayList<String> latitude;
    //private String url = "http://api.theagriculture.tk/api/locations/ado/completed";
    private String url = Globals.adoCompleted;
    private String nextUrl;
    private boolean isNextBusy = false;
    private View view;
    private ArrayList<String> mId;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Section_ado> sections = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ProgressBar spinner, nextPageSpinner;
    boolean doubleBackToExitPressedOnce = false;

    MenuItem searchItem;
    MenuItem searchItem_filter;

    //tag
    private final String TAG = "ado_complete_fragmnt";

    public ado_complete_fragment(){

    }

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
                 //alert_filter_dialog();works on uncommentig inder
                new ShowFilterDialog().alert_filter_dialog(getContext());

                return true;
            }


        });


        // MenuItemCompat.setOn
        /*searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //searchItem_filter.setVisible(false);
               // if (getSupportActionBar().setDisplayHomeAsUpEnabled(true))


                searchItem_filter.setVisible(false);
                dialog.invalidateOptionsMenu();
                return false;
            }
        });*/

        super.onCreateOptionsMenu(menu, inflater);
    }

    ////abhi code for lert dialog
    /*
    //function for alert dialog of filter search
    private void alert_filter_dialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.filter_dialog, null);

        //Create views for spinners here
        Spinner sp1 = promptsView.findViewById(R.id.status_filter_spinner);
        Spinner sp2 = promptsView.findViewById(R.id.date_filter_spinner);
        Button sp3 = promptsView.findViewById(R.id.state_filter_button);
        Button sp4 = promptsView.findViewById(R.id.district_filter_button);
        Button sp5 = promptsView.findViewById(R.id.village_filter_button);

        String[] status = getResources().getStringArray(R.array.status);
        String[] date = getResources().getStringArray(R.array.date);
        String[] district = {"Any","Multiple"};
        String[] village =  {"Any"} ;

        ArrayAdapter<String> status_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(status_adapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> date_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,date);
        date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(date_adapter);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog();
                //Toast.makeText(getActivity(),"You clicked button",Toast.LENGTH_LONG).show();
            }
        });

        sp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog();
                //Toast.makeText(getActivity(),"You clicked button",Toast.LENGTH_LONG).show();
            }
        });

        sp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog();
                //Toast.makeText(getActivity(),"You clicked button",Toast.LENGTH_LONG).show();
            }
        });

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Filter Search");
        alertDialogBuilder.setPositiveButton("Apply",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int which) {
                Toast.makeText(getActivity(), "Apply is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getActivity(),"cancel is clicked",Toast.LENGTH_LONG).show();
                alertDialogBuilder.setCancelable(true);
            }
        });
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
//        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void showSelectionDialog(){
        CustomDialog customDialog;

        ArrayList<String> items = new ArrayList<>();
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");

        RecyclerViewAdapter_district dataAdapter = new RecyclerViewAdapter_district(getActivity(),items);
        customDialog = new CustomDialog(getActivity(), dataAdapter);

        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);

    }
    */
        ///////yaha tak /////////


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ado_complete_fragment, container, false);
        mtextview1 = new ArrayList<>();
        mtextview2 = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();
        mId = new ArrayList<>();
        //add data in the array with load data

        Toolbar toolbar = view.findViewById(R.id.app__bar_ado_completed);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Completed");
        }else {
            title_top.setText("AFL Monitoring");
        }

        final ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);//to display search icon

        Log.d(TAG, "onCreateView: inside onCreate");


        recyclerView = view.findViewById(R.id.ado_completed_rv);
        spinner= view.findViewById(R.id.ado_complete_progress);
        nextPageSpinner = view.findViewById(R.id.ado_pending_nextpage);
        nextPageSpinner.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull8);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(ado_complete_fragment.this).attach(ado_complete_fragment.this).commit();
            }
        });


        /*//////////////// working but second time
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();
        getData(url);

         */
        /*
        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        getData(url);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);



        recyclerView.setAdapter(adoListAdapter);
        recyclerView.setHasFixedSize(true);
        adoListAdapter.notifyDataSetChanged();
        //adoListAdapter.notifyDataSetChanged();

         */

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        /////////////////////////////
        getData(url);
        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();

        //getData(url);
        /*
        sections = new ArrayList<>();
        Log.d(TAG, "getData: inside getdata");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            Log.d(TAG, "onResponse1: ");
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            Log.d(TAG, "onResponse2: ");
                            if(resultsArray.length()== 0){
                                //adoListAdapter.mshowshimmer = false;
                                adoListAdapter.notifyDataSetChanged();
                                Log.d(TAG, "onResponse3: ");
                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }


                            String[][] arr = new String[6][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name");
                                String dlocation_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                            }
                            Log.d(TAG, "onResponse4: ");
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
                            Log.d(TAG, "onResponse5: ");
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
                                    sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude));
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
                            Log.d(TAG, "onResponse6: ");
                            sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude));
                            Log.d(TAG, "onResponse7: ");
                            adoListAdapter.notifyDataSetChanged();
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);
                            Log.d(TAG, "onResponse8: ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: inside the evception" + e);
                            spinner.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                        if(error instanceof NoConnectionError || error instanceof TimeoutError){
                            Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
                        }
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.GONE);
                        isNextBusy = false;
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        Log.d(TAG, "onResponse9: ");
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "onResponse10: ");
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
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getData(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        Log.d(TAG, "onResponse after getDate: ");
        */


        /*

        adoListAdapter = new AdoListAdapter(getContext(), mtextview1, mtextview2, true, mId);
        recyclerView.setAdapter(adoListAdapter);

         */


        return view;
    }


    private void getData(String url) {
        spinner.setVisibility(View.VISIBLE);
        sections = new ArrayList<>();
        Log.d(TAG, "getData: inside getdata");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();

                        try {
                            Log.d(TAG, "onResponse1: ");
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            //Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            Log.d(TAG, "onResponse2: ");
                            if(resultsArray.length()== 0){
                                //adoListAdapter.mshowshimmer = false;
                                adoListAdapter.notifyDataSetChanged();
                                Log.d(TAG, "onResponse3: ");
                                nothing_toshow_fragment abc = new nothing_toshow_fragment();
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nothing,abc).commit();
                                //view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }



                            String[][] arr = new String[6][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name") + ", "+singleObject.getString("block");
                                String dlocation_address = singleObject.getString("district") + ", " + singleObject.getString("state") ;
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                //done to check dda...all are null
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
                            Log.d(TAG, "onResponse4: ");
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
                            Log.d(TAG, "onResponse5: ");
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
                                    sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,false,true));
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
                            Log.d(TAG, "onResponse6: ");
                            sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,false,true));
                            Log.d(TAG, "onResponse7: ");
                            //Toast.makeText(getActivity(),arr.toString(),Toast.LENGTH_LONG).show();

                            adoListAdapter.notifyDataSetChanged();
                            isNextBusy = false;
                            nextPageSpinner.setVisibility(View.GONE);
                            spinner.setVisibility(View.GONE);
                            Log.d(TAG, "onResponse8: ");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: inside the evception" + e);
                            spinner.setVisibility(View.GONE);
                            nextPageSpinner.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                        if(error instanceof NoConnectionError || error instanceof TimeoutError){
                            //Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
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
                                    getFragmentManager().beginTransaction().detach(ado_complete_fragment.this).attach(ado_complete_fragment.this).commit();
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
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.GONE);
                        nextPageSpinner.setVisibility(View.GONE);
                        isNextBusy = false;
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
        Log.d(TAG, "onResponse9: ");
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "onResponse10: ");
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
        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int totalCount, pastItemCount, visibleItemCount;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: out DX " + dx + " DY " + dy);
            }
        });

         */
    }
}