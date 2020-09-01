package com.theagriculture.app.Dda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.android.volley.Request;
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
import com.theagriculture.app.Admin.Section;
import com.theagriculture.app.Admin.SectionAdapter;
import com.theagriculture.app.Ado.SectionAdapter_ado;
import com.theagriculture.app.Ado.Section_ado;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class assignedfragment extends Fragment {

    private static final String TAG = "assignedfragment";
    private ArrayList<String> Id;
    private ArrayList<String> Name;
    private ArrayList<String> Address;
    private ArrayList<String> mAdoIds;
    private ArrayList<String> mDate;
    private DdapendingassignedAdapter ddaassignedAdapter;
    ArrayList<Section_DDA> sections = new ArrayList<>();
//    ArrayList<Section_DDA> sections_adoid = new ArrayList<>();
    private SectionAdapter_DDA recyclerViewAdater;
    private String urlget = Globals.assignedLocationsDDA;                           //"http://18.224.202.135/api/locations/dda/assigned";
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String state;
    private String nextUrl;
    private boolean isNextBusy = false;
    private boolean isReferesh;
    private View view;
    private int length_of_arrray;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView review;
    LinearLayoutManager layoutManager;
    ProgressBar spinner,progressBar;
    boolean doubleBackToExitPressedOnce = false;


    public assignedfragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView: ");
        view = inflater.inflate(R.layout.fragment_ongoing,container,false);
        Id = new ArrayList<String>();
        Name = new ArrayList<String>();
        Address = new ArrayList<String>();
        mAdoIds = new ArrayList<>();
        mDate = new ArrayList<>();
        isReferesh = false;
        swipeRefreshLayout = view.findViewById(R.id.refreshpull_dda);
        review = view.findViewById(R.id.recyclerViewongoing);
        spinner = view.findViewById(R.id.progressbar_dda);
        progressBar = view.findViewById(R.id.locations_loading_dda);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.ongoing_dda_toolbar);
        toolbar.setVisibility(View.GONE);

        review.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                //swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sections=new ArrayList<>();
                getFragmentManager().beginTransaction().detach(assignedfragment.this).attach(assignedfragment.this).commit();
            }
        });


        getData(urlget);
        Log.d(TAG,"URL: " + urlget);


//        recyclerViewAdater = new SectionAdapter(getActivity(), sections,true);
//        review.setAdapter(recyclerViewAdater);
//        recyclerViewAdater.notifyDataSetChanged();


//        ddaassignedAdapter = new DdapendingassignedAdapter(getActivity(), Id, Name, Address, mAdoIds,mDate);
//        review.setAdapter(ddaassignedAdapter);
//        ddaassignedAdapter.notifyDataSetChanged();

        layoutManager = new LinearLayoutManager(getActivity());
        review.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(review.getContext(), layoutManager.getOrientation());
        review.addItemDecoration(divider);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);

        Log.d(TAG, "onCreateView: inflated fragment_ongoing");

        recyclerViewAdater = new SectionAdapter_DDA(getActivity(),sections);
        review.setAdapter(recyclerViewAdater);
        recyclerViewAdater.notifyDataSetChanged();

/*        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlget, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
//                    String date = jsonObject.getString("created_on");
                    length_of_arrray = jsonArray.length();
                    if(length_of_arrray==0){
//                        ddaassignedAdapter.showassignedshimmer = false;
                        ddaassignedAdapter.notifyDataSetChanged();
//                        recyclerViewAdater.notifyDataSetChanged();
                        //todo image
                        view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                    }
                    for(int i=0;i<jsonArray.length();i++){
//                        sections = new ArrayList<>();
//                        Id = new ArrayList<String>();
//                        Name = new ArrayList<String>();
//                        Address = new ArrayList<String>();
//                        mAdoIds = new ArrayList<>();
//                        mDate = new ArrayList<>();
                        JSONObject singleObject = jsonArray.getJSONObject(i);
                        String date = singleObject.getString("created_on");
                        System.out.println(date);
                        Id.add(singleObject.getString("id"));
                        mDate.add(singleObject.getString("acq_date"));
                        villagename = singleObject.getString("village_name");
                        blockname = singleObject.getString("block_name");
                        district = singleObject.getString("district");
                        state = singleObject.getString("state");
                        Address.add(villagename.toUpperCase() + ", " + blockname.toUpperCase() +
                                ", " + district.toUpperCase());
                        JSONObject adoObject = singleObject.getJSONObject("ado");
                        Name.add(adoObject.getString("name"));
                        String adoId = adoObject.getString("id");
                        mAdoIds.add(adoId);
//                        sections.add(new Section(date,Id,villagename,blockname,district,state,Address,Name,mAdoIds,true));
//                        recyclerViewAdater.notifyDataSetChanged();
//                        System.out.println("see here for size"+sections.size());
//                        System.out.println(sections);

                    }
//                    ddaassignedAdapter.showassignedshimmer = false;
                    ddaassignedAdapter.notifyDataSetChanged();
//                    recyclerViewAdater.notifyDataSetChanged();

                }catch (JSONException e){
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error );
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
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
        review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/
        return view;
    }

//    public void getData(String URL){
//        sections=new ArrayList<>();
//        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        isNextBusy = true;
//        final JsonObjectRequest jsonObjectRequest = new
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        isReferesh = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        isReferesh = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if(isReferesh)
        {
            getFragmentManager().beginTransaction().detach(assignedfragment.this)
                    .attach(assignedfragment.this).commit();
            Log.d(TAG, "onResume: REFRESH");
            isReferesh = false;
        }
}

    private void getData(final String url) {
//        sections=new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            //Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            if(resultsArray.length()== 0){
                                //adoListAdapter.mshowshimmer = false;
                                String[][] arr = new String[0][0];
                                recyclerViewAdater.notifyDataSetChanged();
                                nothing_toshow_fragment no_data = new nothing_toshow_fragment();
                                AppCompatActivity activity = (AppCompatActivity)getActivity();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.assigned_dda, no_data).commit();
                                return;
//                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                            }

                            String[][] arr = new String[7][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
//                                mAdoIds = new ArrayList<>();
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name");
                                String dlocation_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                JSONObject adoObject = singleObject.getJSONObject("ado");
                                String ado_id = adoObject.getString("id");
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                                arr[6][i]=ado_id;
                                System.out.println("dimple1" + "arr[6][" + i + "]: " + arr[6][i]);
                            }
                            String inter,one_adoid;
                            for(int i=0;i<resultsArray.length()-1;i++){
                                for(int j=0;j<resultsArray.length()-i-1;j++){
                                    String idate = arr[0][j];
                                    String ndate = arr[0][j+1];

                                    String iadoid = arr[6][j];
                                    String nadoid = arr[6][j+1];

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
                                        for(int k=0;k<7;k++) {              //k<6
                                            inter = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = inter;
                                        }
                                    }
                                    if (iadoid.equals(nadoid)){
                                        for(int k=0;k<7;k++) {              //k<6
                                            one_adoid = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = one_adoid;
                                        }
                                    }
                                }
                            }
                            ArrayList<String> mDid = new ArrayList<>();
                            ArrayList<String> mDlocation_name = new ArrayList<>();
                            ArrayList<String> mDlocation_address = new ArrayList<>();
                            ArrayList<String> mlatitude= new ArrayList<>();
                            ArrayList<String> mlongitude= new ArrayList<>();
                            ArrayList<String> m_ado_id= new ArrayList<>();
                            String predate=arr[0][0];
                            String preadoid=arr[6][0];
                            m_ado_id.add(arr[6][0]);
                            //String predate = null;
                            for(int i=0;i<resultsArray.length();i++){
                                String idate = arr[0][i];
                                String iadoid = arr[6][i];

                                if (!preadoid.equals(iadoid)){
                                    m_ado_id.add(arr[6][i]);
                                    System.out.println("in if m_ado_id: " + m_ado_id);
                                }
                                if(predate.equals(idate)){
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);

                                    //predate=idate;
                                }
                                else{
                                    System.out.println("in else m_ado_id: " + m_ado_id);
                                    System.out.println("in else preadoid: " + preadoid);
//                                    new Section_DDA(m_ado_id);
                                    sections.add(new Section_DDA(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,m_ado_id,true,false,false));
                                    mDid = new ArrayList<>();
                                    mDlocation_name = new ArrayList<>();
                                    mDlocation_address = new ArrayList<>();
                                    mlatitude = new ArrayList<>();
                                    mlongitude = new ArrayList<>();
//                                    m_ado_id = new ArrayList<>();
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
//                                    m_ado_id.add(arr[6][i]);
                                    //date.equals(idate);

                                }
                                //predate.equals(idate);
                                predate=idate;
                                preadoid=iadoid;
                            }
                            System.out.println("end pe chk ado id: "+ m_ado_id);
                            System.out.println("end pe chk ado id ek hai?: "+ preadoid);
//                            new Section_DDA(m_ado_id);
                            sections.add(new Section_DDA(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,m_ado_id,true,false,false));
                            //adoListAdapter.mshowshimmer = false;
                            recyclerViewAdater.notifyDataSetChanged();
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            spinner.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
//                            spinner.setVisibility(View.GONE);
                            //Fragment fragment = getFragmentManager().findFragmentById(R.id.rootView);
                            //fragment.getView().setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
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
                                    //   spinner.setVisibility(View.VISIBLE);
                                    getNextData(url);
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
                        } else if (error instanceof AuthFailureError) {
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
                        progressBar.setVisibility(View.GONE);
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
        review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy) {
                            progressBar.setVisibility(View.VISIBLE);
                            getNextData(nextUrl);
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    private void getNextData(final String url) {
        Log.d(TAG,"locations corresponding to next URL: "+url);
//        sections=new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            //Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            if(resultsArray.length()== 0){
                                //adoListAdapter.mshowshimmer = false;
                                String[][] arr = new String[0][0];
                                recyclerViewAdater.notifyDataSetChanged();
//                                nothing_toshow_fragment no_data = new nothing_toshow_fragment();
//                                AppCompatActivity activity = (AppCompatActivity)getActivity();
//                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.assigned_dda, no_data).commit();
                                return;
//                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }

                            String[][] arr = new String[7][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name");
                                String dlocation_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                JSONObject adoObject = singleObject.getJSONObject("ado");
                                String ado_id = adoObject.getString("id");
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                                arr[6][i]=ado_id;
                                System.out.println("dimple1 in next fn" + "arr[6][" + i + "]: " + arr[6][i]);

                            }
                            String inter,one_adoid;
                            for(int i=0;i<resultsArray.length()-1;i++){
                                for(int j=0;j<resultsArray.length()-i-1;j++){
                                    String idate = arr[0][j];
                                    String ndate = arr[0][j+1];

                                    String iadoid = arr[6][j];
                                    String nadoid = arr[6][j+1];

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
                                        for(int k=0;k<7;k++) {          //k<6
                                            inter = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = inter;
                                        }
                                    }
                                    if (iadoid.equals(nadoid)){
                                        for(int k=0;k<7;k++) {              //k<6
                                            one_adoid = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = one_adoid;
                                        }
                                    }
                                }
                            }
                            ArrayList<String> mDid = new ArrayList<>();
                            ArrayList<String> mDlocation_name = new ArrayList<>();
                            ArrayList<String> mDlocation_address = new ArrayList<>();
                            ArrayList<String> mlatitude= new ArrayList<>();
                            ArrayList<String> mlongitude= new ArrayList<>();
                            ArrayList<String> m_ado_id= new ArrayList<>();
                            String predate=arr[0][0];
                            String preadoid=arr[6][0];
                            m_ado_id.add(arr[6][0]);
                            //String predate = null;
                            for(int i=0;i<resultsArray.length();i++){
                                String idate = arr[0][i];
                                String iadoid = arr[6][i];

                                if (!preadoid.equals(iadoid)){
                                    m_ado_id.add(arr[6][i]);
                                    System.out.println("in if m_ado_id: " + m_ado_id);
                                }
                                if(predate.equals(idate)){
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
                                    m_ado_id.add(arr[6][i]);
                                    System.out.println("in if of next fn m_ado_id: " + m_ado_id);

                                    //predate=idate;
                                }
                                else{
//                                    new Section_DDA(m_ado_id);
                                    sections.add(new Section_DDA(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,m_ado_id,true,false,false));
                                    mDid = new ArrayList<>();
                                    mDlocation_name = new ArrayList<>();
                                    mDlocation_address = new ArrayList<>();
                                    mlatitude = new ArrayList<>();
                                    mlongitude = new ArrayList<>();
//                                    m_ado_id = new ArrayList<>();
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
//                                    m_ado_id.add(arr[6][i]);
                                    //date.equals(idate);
//                                    System.out.println("in else of next fn m_ado_id: " + m_ado_id);

                                }
                                //predate.equals(idate);
                                predate=idate;
                            }
//                            new Section_DDA(m_ado_id);
                            sections.add(new Section_DDA(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,m_ado_id,true,false,false));
                            //adoListAdapter.mshowshimmer = false;
                            recyclerViewAdater.notifyDataSetChanged();
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
//                            spinner.setVisibility(View.GONE);
                            //Fragment fragment = getFragmentManager().findFragmentById(R.id.rootView);
                            //fragment.getView().setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
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
                                    //   spinner.setVisibility(View.VISIBLE);
                                    getNextData(url);
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
                        } else if (error instanceof AuthFailureError) {
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
                        progressBar.setVisibility(View.GONE);
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
        review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextData(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void getNextLocations() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, nextUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
                    length_of_arrray=jsonArray.length();
                    if(length_of_arrray==0){
//                        ddaassignedAdapter.showassignedshimmer = false;
                        ddaassignedAdapter.notifyDataSetChanged();
//                        recyclerViewAdater.notifyDataSetChanged();
                        //todo image
                        view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
//                        sections = new ArrayList<>();
//                        Id = new ArrayList<String>();
//                        Name = new ArrayList<String>();
//                        Address = new ArrayList<String>();
//                        mAdoIds = new ArrayList<>();
//                        mDate = new ArrayList<>();
                        JSONObject singleObject = jsonArray.getJSONObject(i);
                        String date = singleObject.getString("created_on");
                        mDate.add(singleObject.getString("acq_date"));
                        Id.add(singleObject.getString("id"));
                        villagename = singleObject.getString("village_name");
                        blockname = singleObject.getString("block_name");
                        district = singleObject.getString("district");
                        state = singleObject.getString("state");
                        Address.add(villagename.toUpperCase() + ", " + blockname.toUpperCase() +
                                ", " + district.toUpperCase());
                        JSONObject adoObject = singleObject.getJSONObject("ado");
                        Name.add(adoObject.getString("name"));
                        String adoId = adoObject.getString("id");
                        mAdoIds.add(adoId);
//                        sections.add(new Section(date,Id,villagename,blockname,district,state,Address,Name,mAdoIds,true));
//                        recyclerViewAdater.notifyDataSetChanged();
//                        System.out.println(sections.size());
//                        System.out.println(sections);
                    }
                    isNextBusy = false;
//                    ddaassignedAdapter.showassignedshimmer = false;
                    ddaassignedAdapter.notifyDataSetChanged();
//                    recyclerViewAdater.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                isNextBusy = false;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
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
    }
}
