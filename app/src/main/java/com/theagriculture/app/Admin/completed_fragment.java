package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.theagriculture.app.R;
import com.theagriculture.app.adapter.PendingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class completed_fragment extends Fragment {
    private RecyclerView recyclerView;
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
    private SectionAdapter recyclerViewAdater;
    ProgressBar spinner;
    int no_of_visits = 0;

    //private AdminLocationAdapter adapter;
    //private PendingAdapter adapter;

    private LinearLayoutManager layoutManager;
    //private String completedUrl = "http://18.224.202.135/api/locations/completed";
    private String completedUrl = "http://18.224.202.135/api/locationsDatewise/completed";
    private String nextUrl = "null";
    private ProgressBar progressBar;
    private boolean isNextBusy;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean doubleBackToExitPressedOnce = false;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.completed_fragment,container,false);
        recyclerView = view.findViewById(R.id.completed_recyclerview);
        progressBar = view.findViewById(R.id.locations_loading_completed);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull1);
        spinner = view.findViewById(R.id.completed_progress);
        spinner.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(completed_fragment.this).attach(completed_fragment.this).commit();
            }
        });
        /*
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        mDdaNames = new ArrayList<>();
        mAdoNames = new ArrayList<>();
        mAddresses = new ArrayList<>();
        mIds = new ArrayList<>();
        mdate = new ArrayList<>();
        //adapter = new AdminLocationAdapter(getActivity(), mDdaNames, mAdoNames, mAddresses, true, mIds,mdate);
        adapter = new PendingAdapter(getActivity(), mDdaNames, mAdoNames, mAddresses, true, mIds,mdate);
        recyclerView.setAdapter(adapter);

         */
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        getData();
        recyclerViewAdater = new SectionAdapter(getActivity(),sections);
        recyclerView.setAdapter(recyclerViewAdater);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        if (!isNextBusy && nextUrl.equals("null"))
                            getNextLocations();
                    }
                }
                //super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(completedUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            nextUrl = jsonObject.getString("next");
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
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
                                sections.add(new Section(mdate,mDdaName, mAdaName, mAddress,mId,mpkado,mpkdda,false,false,true));
                                //Toast.makeText(getActivity(),sections.get(0).toString(),Toast.LENGTH_LONG).show();
                            }
                            //recyclerViewAdater = new SectionAdapter(getActivity(),sections);
                            //recyclerView.setAdapter(recyclerViewAdater);
                            spinner.setVisibility(View.GONE);
                            no_of_visits++;
                            /*
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            if(resultsArray.length()== 0){
                                adapter.mShowShimmer = false;
                                adapter.notifyDataSetChanged();

                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                JSONObject ddaObject = singleObject.getJSONObject("dda");
                                String date = singleObject.getString("acq_date");
                                mDdaNames.add(ddaObject.getString("name"));
                                String id = singleObject.getString("id");
                                mIds.add(id);
                                mdate.add(date);
                                JSONObject adoObject = singleObject.getJSONObject("ado");
                                String adoName = adoObject.getString("name");
                                if (adoName.equals("null"))
                                    mAdoNames.add("Not Assigned");
                                else
                                    mAdoNames.add(adoName);
                                String location = singleObject.getString("village_name").toUpperCase()
                                        + ", " + singleObject.getString("block_name").toUpperCase() + ", "
                                        + singleObject.getString("district").toUpperCase();
                                mAddresses.add(location);

                            }
                            adapter.mShowShimmer = false;
                            adapter.notifyDataSetChanged();

                             */
                        } catch (JSONException e) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "An exception occured", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
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
                            Toast.makeText(getActivity(), "This error is case6", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getActivity(), "Check Your Internt Connection Please!", Toast.LENGTH_SHORT).show();
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
    }

    private void getNextLocations() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(nextUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            nextUrl = jsonObject.getString("next");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Toast.makeText(getActivity(),"entered i",Toast.LENGTH_LONG).show();

                                //itemArrayList = new ArrayList<>();
                                mDdaName = new ArrayList<>();
                                mAdaName = new ArrayList<>();
                                mAddress = new ArrayList<>();
                                mpkado = new ArrayList<>();
                                mpkdda = new ArrayList<>();
                                mId = new ArrayList<>();

                                JSONObject cd = jsonArray.getJSONObject(i);
                                String date = cd.getString("date");
                                JSONArray jsonArray_locations = cd.getJSONArray("locations");
                                for (int j = 0; j < jsonArray_locations.length(); j++) {
                                    Toast.makeText(getActivity(),"entered j",Toast.LENGTH_LONG).show();
                                    JSONObject c = jsonArray_locations.getJSONObject(j);
                                    try{
                                        String aid= c.getString("id");
                                        mId.add(aid);
                                    }
                                    catch (JSONException e){
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
                                    mAddress.add(villagename.toUpperCase() + ", " + blockname.toUpperCase() + ", " + district.toUpperCase());
                                    Toast.makeText(getActivity(),"Added mAddress",Toast.LENGTH_LONG).show();
                                }
                                sections.add(new Section("date",mDdaName, mAdaName, mAddress,mId,mpkado,mpkdda,false,false,true));
                            /*
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                mDdaNames.add(singleObject.getString("dda"));
                                String date = singleObject.getString("acq_date");
                                String id = singleObject.getString("id");
                                mIds.add(id);
                                mdate.add(date);
                                String adoName = singleObject.getString("ado");
                                if (adoName.isEmpty())
                                    mAdoNames.add("Not Assigned");
                                else
                                    mAdoNames.add(adoName);
                                String location = singleObject.getString("village_name").toUpperCase()
                                        + ", " + singleObject.getString("block_name").toUpperCase() + ", "
                                        + singleObject.getString("district").toUpperCase();
                                mAddresses.add(location);

                             */
                            }
                            //adapter.notifyDataSetChanged();
                            recyclerViewAdater.notifyDataSetChanged();
                            isNextBusy = false;

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
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
                                    //spinner.setVisibility(View.VISIBLE);
                                    getNextLocations();
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
                            //Toast.makeText(getActivity(), "This error is case6", Toast.LENGTH_LONG).show();
                        }
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
        requestQueue.add(jsonObjectRequest);
        requestFinished(requestQueue);
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
    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
