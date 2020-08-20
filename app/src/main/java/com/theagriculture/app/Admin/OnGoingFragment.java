package com.theagriculture.app.Admin;


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
import com.theagriculture.app.R;
import com.theagriculture.app.adapter.PendingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnGoingFragment extends Fragment {

    //private String ongoingUrl = "http://18.224.202.135/api/locations/ongoing";
    private String ongoingUrl = "http://18.224.202.135/api/locationsDatewise/ongoing";

    private ArrayList<String> mDdaName;
    private ArrayList<String> mAdaName;
    private ArrayList<String> mAddress;
    private ArrayList<String> mpkado;
    private ArrayList<String> mpkdda;
    private ArrayList<String> mId;
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String aid;
    final ArrayList<Section> sections = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar spinner;
    int no_of_visits = 0;
    boolean doubleBackToExitPressedOnce=false;
    private final String TAG = "ongoing_fragment";
    private boolean isRefresh;

    //private AdminLocationAdapter adapter;
    //private PendingAdapter adapter;
    private SectionAdapter recyclerViewAdater;
    private LinearLayoutManager layoutManager;
    private String next_ongoing_url = "null";
    private ProgressBar progressBar;
    private boolean isNextBusy = false;
    private  View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OnGoingFragment() {
        // Required empty public constructor
    }

    /*
    @Override
    public void setUserVisibleHint(boolean isVisibletoUser){
        super.setUserVisibleHint(isVisibletoUser);
        if(isVisibletoUser && no_of_visits==0) {
            //recyclerView.setAdapter(adapter);
            getData();
        }
    }

     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        isRefresh = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.fragment_on_going, container, false);
        recyclerView = view.findViewById(R.id.ongoing_recyclerview);
        progressBar = view.findViewById(R.id.locations_loading_ongoing);
        spinner = view.findViewById(R.id.on_going_progress);
        spinner.setVisibility(View.VISIBLE);
        /*
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        */

        //for complete scroll for recycler view (from bottom to up(top))
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
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

        isRefresh = false;
        swipeRefreshLayout = view.findViewById(R.id.refreshpull3);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(OnGoingFragment.this).attach(OnGoingFragment.this).commit();
            }
        });
        /*

        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        mDDaNames = new ArrayList<>();
        mAdoNames = new ArrayList<>();
        mAddresses = new ArrayList<>();
        mIds = new ArrayList<>();
        mdate = new ArrayList<>();
        //adapter = new AdminLocationAdapter(getActivity(), mDDaNames, mAdoNames, mAddresses, mIds, true,mdate);
        adapter = new PendingAdapter(getActivity(), mDDaNames, mAdoNames, mAddresses, mIds, true,mdate);
        recyclerView.setAdapter(adapter);

         */
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");


//        getData();
        recyclerViewAdater = new SectionAdapter(getActivity(),sections);
        recyclerView.setAdapter(recyclerViewAdater);
        //spinner.setVisibility(View.GONE);
        recyclerViewAdater.notifyDataSetChanged();
        Log.d(TAG, "onCreateView: " + token);

        return view;
    }

    public void getData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ongoingUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            next_ongoing_url = jsonObject.getString("next");
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            if(jsonArray.length()==0) {
                              //  nothing_toshow_fragment no_data = new nothing_toshow_fragment();
                              //  AppCompatActivity activity = (AppCompatActivity) getActivity();
                              //  activity.getSupportFragmentManager().beginTransaction().replace(R.id.change_when_nodata, no_data).addToBackStack(null).commit();
                                 view.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_emptyartboard_1));//todo image bg
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //Toast.makeText(getActivity(),"Enterd i",Toast.LENGTH_LONG).show();
                                //itemArrayList = new ArrayList<>();
                                mDdaName = new ArrayList<>();
                                mAdaName = new ArrayList<>();
                                mAddress = new ArrayList<>();
                                mpkado = new ArrayList<>();
                                mpkdda = new ArrayList<>();
                                mId = new ArrayList<>();
                                JSONObject cd = jsonArray.getJSONObject(i);
                                //Toast.makeText(getActivity(),cd.toString(),Toast.LENGTH_LONG).show();
                                String mdate = cd.getString("date");
                                //
                                JSONArray jsonArray_locations = cd.getJSONArray("locations");
                                //Toast.makeText(getActivity(),"Got location array",Toast.LENGTH_LONG).show();//could not reach here
                                for (int j = 0; j < jsonArray_locations.length(); j++) {
                                    //Toast.makeText(getActivity(),"Enterd j",Toast.LENGTH_LONG).show();
                                    JSONObject c = jsonArray_locations.getJSONObject(j);
                                    try{
                                        aid = c.getString("id");
                                        mId.add(aid);
                                    }
                                    catch(JSONException e){
                                        mId.add("null");
                                    }
                                    try {
                                        JSONObject adoobj = c.getJSONObject("ado");
                                        JSONObject authado = adoobj.getJSONObject("auth_user");
                                        mpkado.add(authado.getString("pk"));

                                    } catch (JSONException e) {
                                        mpkado.add("null");
                                    }
                                    try {
                                        JSONObject ddaobj = c.getJSONObject("dda");
                                        JSONObject authddo = ddaobj.getJSONObject("auth_user");
                                        mpkdda.add(authddo.getString("pk"));
                                    } catch (JSONException e) {
                                        mpkdda.add("null");
                                    }
                                    villagename = c.getString("village_name");
                                    blockname = c.getString("block_name");
                                    district = c.getString("district");
                                    try {
                                        JSONObject mDdaObject = c.getJSONObject("dda");
                                        String ddaName = mDdaObject.getString("name");
                                        mDdaName.add(ddaName);
                                    } catch (JSONException e) {
                                        mDdaName.add("Not Assigned");
                                    }
                                    try {
                                        JSONObject mAdoObject = c.getJSONObject("ado");
                                        String adoName = mAdoObject.getString("name");
                                        mAdaName.add(adoName);
                                    } catch (JSONException e) {
                                        mAdaName.add("Not Assigned");
                                    }
                                    mAddress.add(villagename.toUpperCase() + ", " +
                                            blockname.toUpperCase() + ", " + district.toUpperCase());
                                }
                                sections.add(new Section(mdate,mDdaName, mAdaName, mAddress,mId,mpkado,mpkdda,false,true,false));
                            }
                            //recyclerViewAdater = new SectionAdapter(getActivity(),sections);
                            //recyclerView.setAdapter(recyclerViewAdater);
                            spinner.setVisibility(View.GONE);
                            //recyclerViewAdater.notifyDataSetChanged();
                            no_of_visits++;
                            /*
                            adapter.mShowShimmer = false;
                            adapter.notifyDataSetChanged();

                             */
                        } catch (JSONException e) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            //Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
                            final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                            mBottomDialogNotificationAction.setContentView(sheetView);
                            mBottomDialogNotificationAction.setCanceledOnTouchOutside(false);
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
                                    getData();
                                }
                            });

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!doubleBackToExitPressedOnce) {
                                        doubleBackToExitPressedOnce = true;
                                        Toast toast = Toast.makeText(getActivity(),"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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

                        }else
                            Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
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
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount && (pastItemCount >= 0) && (totalCount >= PAGE_SIZE)) {
                        if (!next_ongoing_url.equals("null") /*&& !isNextBusy*/)
                            get_Ongoing();
                    }
                }
                //super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void get_Ongoing() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, next_ongoing_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            next_ongoing_url = jsonObject.getString("next");
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            if(jsonArray.length()==0)
                                view.setBackground(getActivity().getResources().getDrawable(R.drawable.nothing_clipboard));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //Toast.makeText(getActivity(),"Enterd i",Toast.LENGTH_LONG).show();
                                //itemArrayList = new ArrayList<>();
                                mDdaName = new ArrayList<>();
                                mAdaName = new ArrayList<>();
                                mAddress = new ArrayList<>();
                                mpkado = new ArrayList<>();
                                mpkdda = new ArrayList<>();
                                mId = new ArrayList<>();
                                JSONObject cd = jsonArray.getJSONObject(i);
                                //Toast.makeText(getActivity(),cd.toString(),Toast.LENGTH_LONG).show();
                                String mdate = cd.getString("date");
                                //
                                JSONArray jsonArray_locations = cd.getJSONArray("locations");
                                //Toast.makeText(getActivity(),"Got location array",Toast.LENGTH_LONG).show();//could not reach here
                                for (int j = 0; j < jsonArray_locations.length(); j++) {
                                    //Toast.makeText(getActivity(),"Enterd j",Toast.LENGTH_LONG).show();
                                    JSONObject c = jsonArray_locations.getJSONObject(j);
                                    try{
                                        aid = c.getString("id");
                                        mId.add(aid);
                                    }
                                    catch(JSONException e){
                                        mId.add("null");
                                    }
                                    try {
                                        JSONObject adoobj = c.getJSONObject("ado");
                                        JSONObject authado = adoobj.getJSONObject("auth_user");
                                        mpkado.add(authado.getString("pk"));

                                    } catch (JSONException e) {
                                        mpkado.add("null");
                                    }
                                    try {
                                        JSONObject ddaobj = c.getJSONObject("dda");
                                        JSONObject authddo = ddaobj.getJSONObject("auth_user");
                                        mpkdda.add(authddo.getString("pk"));
                                    } catch (JSONException e) {
                                        mpkdda.add("null");
                                    }
                                    villagename = c.getString("village_name");
                                    blockname = c.getString("block_name");
                                    district = c.getString("district");
                                    try {
                                        JSONObject mDdaObject = c.getJSONObject("dda");
                                        String ddaName = mDdaObject.getString("name");
                                        mDdaName.add(ddaName);
                                    } catch (JSONException e) {
                                        mDdaName.add("Not Assigned");
                                    }
                                    try {
                                        JSONObject mAdoObject = c.getJSONObject("ado");
                                        String adoName = mAdoObject.getString("name");
                                        mAdaName.add(adoName);
                                    } catch (JSONException e) {
                                        mAdaName.add("Not Assigned");
                                    }
                                    //Toast.makeText(getActivity(),"cleared all try catch",Toast.LENGTH_LONG).show();
                                    mAddress.add(villagename.toUpperCase() + ", " +
                                            blockname.toUpperCase() + ", " + district.toUpperCase());
                                }
                                sections.add(new Section(mdate,mDdaName, mAdaName, mAddress,mId,mpkado,mpkdda,false,true,false));
                                //Toast.makeText(getActivity(),sections.get(0).toString(),Toast.LENGTH_LONG).show();
                            }
                            //recyclerViewAdater = new SectionAdapter(getActivity(),sections);
                            //recyclerView.setAdapter(recyclerViewAdater);
                            spinner.setVisibility(View.GONE);
                            //recyclerViewAdater.notifyDataSetChanged();
                            no_of_visits++;
                            /*
                            adapter.mShowShimmer = false;
                            adapter.notifyDataSetChanged();

                             */
                        } catch (JSONException e) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            //Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
                            final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                            mBottomDialogNotificationAction.setContentView(sheetView);
                            mBottomDialogNotificationAction.setCanceledOnTouchOutside(false);
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
                                    get_Ongoing();
                                }
                            });

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!doubleBackToExitPressedOnce) {
                                        doubleBackToExitPressedOnce = true;
                                        Toast toast = Toast.makeText(getActivity(),"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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

                        }else
                            Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
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
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount && (pastItemCount >= 0) && (totalCount >= PAGE_SIZE)) {
                        if (!next_ongoing_url.equals("null") /*&& !isNextBusy*/)
                            get_Ongoing();
                    }
                }
                //super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart: ");
//        spinner = view.findViewById(R.id.ddo_progressbar);
//        spinner.setVisibility(View.VISIBLE);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        //spinner.setVisibility(View.GONE);
//        if (isRefresh) {
//            getFragmentManager().beginTransaction().detach(OnGoingFragment.this)
//                    .attach(OnGoingFragment.this).commit();
//            Log.d(TAG, "onResume: REFRESH");
//            isRefresh = false;
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
//        isRefresh = true;
    }
}
