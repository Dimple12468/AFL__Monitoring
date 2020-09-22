package com.theagriculture.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.theagriculture.app.Admin.Section;
import com.theagriculture.app.Admin.SectionAdapter;
import com.theagriculture.app.Ado.Section_ado;

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

public class ProfilePage extends AppCompatActivity {
    String userId,completedCount,pendingCount;
    TextView userName,pCount,cCount,position,userAddress,userNumber,userMail;
    RecyclerView recyclerView;
    ProfilePageAdapter adapter;
    ArrayList<String> location, district;
    LinearLayoutManager layoutManager;
    Spinner spin;
    private String nextUrl;
    private boolean isNextBusy = false;

    String typeOfUser;

    String adminPendingUrl = Globals.pendingList,adminOngoingUrl = Globals.ongoingList,adminCompletedUrl = Globals.completedList;
    String adoPendingUrl = Globals.adoPending,adoCompletedUrl = Globals.adoCompleted;
    String ddaAssignedUrl = Globals.assignedLocationsDDA, ddaUnassignedUrl = Globals.unassignedLocationsDDA,
            ddaOngoingUrl = Globals.ddaOngoing,ddaCompletedUrl = Globals.ddaCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);


        userName = findViewById(R.id.user_name);
        position = findViewById(R.id.position);
        userNumber = findViewById(R.id.user_phonenumber);
        userAddress = findViewById(R.id.user_address);
        userMail = findViewById(R.id.user_email);
        recyclerView = findViewById(R.id.status_recycler);
        spin = findViewById(R.id.set_status);

        location = new ArrayList<>();
        district = new ArrayList<>();


        /*
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        district.add("abc");
        district.add("abc");
        district.add("abc");
        district.add("abc");
        district.add("abc");

         */

        //for text from shared prefernces file
        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);

        typeOfUser = preferences.getString("typeOfUser","");

        userName.setText(preferences.getString("Name",""));
        position.setText(preferences.getString("typeOfUser","").toUpperCase());
        userNumber.setText(preferences.getString("PhoneNumber",""));
        userAddress.setText(preferences.getString("Address",""));
        userMail.setText(preferences.getString("Email",""));

        String[] adminSpinner={"Pending","Ongoing","Completed"};
        String[] adoSpinner={"Pending","Completed"};
        String[] ddaSpinner={"Assigned Pending","Unassigned Pending","Ongoing","Completed"};
        //Creating the ArrayAdapter instance having the list

        ArrayAdapter aa;
        if(typeOfUser.equals("admin")) {
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, adminSpinner);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
        }
        else if(typeOfUser.equals("ado")) {
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, adoSpinner);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
        }
        else if(typeOfUser.equals("dda"))  {
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ddaSpinner);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_status=   spin.getItemAtPosition(spin.getSelectedItemPosition()).toString();
                Toast.makeText(getApplicationContext(),selected_status + "has been selected" ,Toast.LENGTH_LONG).show();
                if(typeOfUser.equals("admin")){
                    if(spin.getSelectedItemPosition()==0)
                        getData(adminPendingUrl);
                    if(spin.getSelectedItemPosition()==1)
                        getData(adminOngoingUrl);
                    if(spin.getSelectedItemPosition()==2)
                        getData(adminCompletedUrl);
                }
                else if(typeOfUser.equals("ado")){
                    if(spin.getSelectedItemPosition()==0)
                        getData(adoPendingUrl);
                    if(spin.getSelectedItemPosition()==1)
                        getData(adoCompletedUrl);
                }
                if(typeOfUser.equals("dda")){
                    if(spin.getSelectedItemPosition()==0)
                        getData(ddaAssignedUrl);
                    if(spin.getSelectedItemPosition()==1)
                        getData(ddaUnassignedUrl);
                    if(spin.getSelectedItemPosition()==2)
                        getData(ddaOngoingUrl);
                    if(spin.getSelectedItemPosition()==3)
                        getData(ddaCompletedUrl);
                }

                //Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });





        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ////////getData(adoPendingUrl);

        adapter = new ProfilePageAdapter(getApplicationContext(),location,district,false,false,false);
        recyclerView.setAdapter(adapter);




        adapter.notifyDataSetChanged();
        //DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        //recyclerView.addItemDecoration(divider);

    }
    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            //Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            district.add("abc");
                            district.add("abc");
                            district.add("abc");
                            district.add("abc");
                            district.add("abc");
                            /*
                            nextUrl = rootObject.getString("next");

                            if(resultsArray.length()== 0){
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"Array is empty",Toast.LENGTH_LONG).show();

                            }
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                //String id = singleObject.getString("id");
                                //idList.add(id);
                                String location_name1 = singleObject.getString("village_name");
                                String location_name2 = singleObject.getString("block_name");
                                String district_name = singleObject.getString("district");

                                Toast.makeText(getApplicationContext(),location_name1+location_name2+district_name,Toast.LENGTH_LONG).show();
                                location.add(location_name1 + " " + location_name2);
                                district.add(district_name);

                            }

                             */

                            isNextBusy = false;
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"An exception occurred",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError)
                            Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again",
                                    Toast.LENGTH_LONG).show();

                        isNextBusy = false;
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            getData(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
}
