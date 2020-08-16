package com.theagriculture.app.Ado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static io.fabric.sdk.android.Fabric.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingReport extends Fragment {
    TextView textDummyHintUsername, textDummyHintFather, textDummyHintCode,textDummyHintName;
    EditText editUsername,editFather,editCode, editName, editRemarks,editIncidentReason;
    View view1,view2,view3a,view3b;
    TextInputLayout ti1,ti2,ti3a,ti3b;
    Spinner district_spinner,fireStatus_spinner,landUse_spinner;
    String[] arr;
    String token;
	//ArrayList<String> DistrictName;//for items in spinner
    List DistrictName = new ArrayList<>();
    Button submit_btn;
    String lat , lon , id;
    String reportSubmitUrl = "http://18.224.202.135/api/report-ado/add/";
    AlertDialog reportSubmitLoading;

    public PendingReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_pending_report, container, false);

        Bundle bundle = this.getArguments();
        id = bundle.get("id").toString();
        lat = bundle.get("lat").toString();
        lon = bundle.get("long").toString();
        Toast.makeText(getActivity(),lat+" and "+lon,Toast.LENGTH_LONG).show();

        textDummyHintUsername = view.findViewById(R.id.text_dummy_hint_username);
        editUsername = (EditText) view.findViewById(R.id.edit_username);
        ti1 = (TextInputLayout) view.findViewById(R.id.ti1);
        view1 = view.findViewById(R.id.view1);

        textDummyHintFather = (TextView) view.findViewById(R.id.text_dummy_hint_fathername);
        editFather = (EditText) view.findViewById(R.id.edit_fathername);
        ti2 = (TextInputLayout) view.findViewById(R.id.ti2);
        view2 = view.findViewById(R.id.view2);

        textDummyHintCode = (TextView) view.findViewById(R.id.text_dummy_hint_code);
        editCode = (EditText) view.findViewById(R.id.edit_code);
        ti3a = (TextInputLayout) view.findViewById(R.id.ti3a);
        view3a = view.findViewById(R.id.view3a);

        textDummyHintName = (TextView) view.findViewById(R.id.text_dummy_hint_name);
        editName = (EditText) view.findViewById(R.id.edit_name);
        ti3b = (TextInputLayout) view.findViewById(R.id.ti3b);
        view3b = view.findViewById(R.id.view3b);

        district_spinner = view.findViewById(R.id.spinner_district);
        fireStatus_spinner = view.findViewById(R.id.spinner1);
        landUse_spinner = view.findViewById(R.id.spinner2);

        editRemarks = view.findViewById(R.id.edit_remarks);
        editIncidentReason = view.findViewById(R.id.edit_reason);
        submit_btn = view.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"You submitted " ,Toast.LENGTH_LONG).show();
                checkReport();
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        //Toast.makeText(getActivity(),"token is "+token,Toast.LENGTH_LONG).show();

        // Username
        editUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintUsername.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view1.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editUsername.getText().length() > 0)
                        textDummyHintUsername.setVisibility(View.VISIBLE);
                    else
                        textDummyHintUsername.setVisibility(View.INVISIBLE);
                    view1.setBackgroundResource(R.drawable.rectanglebg);
                }

                //ti1.setError("You need to enter a name");
            }
        });

        // Father Name
        editFather.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintFather.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view2.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editFather.getText().length() > 0)
                        textDummyHintFather.setVisibility(View.VISIBLE);
                    else
                        textDummyHintFather.setVisibility(View.INVISIBLE);
                    view2.setBackgroundResource(R.drawable.rectanglebg);
                }
            }
        });

        editCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintCode.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view3a.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editCode.getText().length() > 0)
                        textDummyHintCode.setVisibility(View.VISIBLE);
                    else
                        textDummyHintCode.setVisibility(View.INVISIBLE);
                    view3a.setBackgroundResource(R.drawable.rectanglebg);
                }
            }
        });

        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintName.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view3b.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editName.getText().length() > 0)
                        textDummyHintName.setVisibility(View.VISIBLE);
                    else
                        textDummyHintName.setVisibility(View.INVISIBLE);
                    view3b.setBackgroundResource(R.drawable.rectanglebg);
                }
            }
        });



        /*
        getData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district_spinner.setAdapter(adapter);

         */

	getDistrictData();
    district_spinner.setSelection(0,false);
	district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         String selected_district=   district_spinner.getItemAtPosition(district_spinner.getSelectedItemPosition()).toString();
         //Toast.makeText(getActivity(),selected_district + "has been selected" ,Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // DO Nothing here
        }
    });


        return view;
    }

    //for district spinner
    public void getDistrictData(){
        DistrictName.add("District");
        RequestQueue district_requestQueue = Volley.newRequestQueue(getActivity());
        String district_list_url = "http://18.224.202.135/api/district/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, district_list_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                try{
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject singleObject = response.getJSONObject(i);
                        //Toast.makeText(getActivity(),singleObject.toString(),Toast.LENGTH_LONG).show();
                        //String idistrict = singleObject.getString("district");
                        //Toast.makeText(getActivity(),idistrict,Toast.LENGTH_LONG).show();
                        DistrictName.add(singleObject.getString("district"));
                    }
                    //Toast.makeText(getActivity(),DistrictName.toString(),Toast.LENGTH_LONG).show();
                    Log.d("spinner",DistrictName.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                district_spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, DistrictName));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
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

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        district_requestQueue.add(jsonArrayRequest);
    }

    public void checkReport(){
        String sFarmerName = editUsername.getText().toString().trim();
        if(sFarmerName.length()==0)
            Toast.makeText(getActivity(),"Please enter farmer's name",Toast.LENGTH_LONG).show();
        String sFatherName = editFather.getText().toString().trim();
        if(sFatherName.length()==0)
            Toast.makeText(getActivity(),"Please enter farmer's father name",Toast.LENGTH_LONG).show();
        String sVillageCode = editCode.getText().toString().trim();
        if(sVillageCode.length()==0)
            Toast.makeText(getActivity(),"Please enter village code",Toast.LENGTH_LONG).show();
        String sVillageName = editName.getText().toString().trim();
        if(sVillageName.length()==0)
            Toast.makeText(getActivity(),"Please enter village name",Toast.LENGTH_LONG).show();
        String sDistrict = district_spinner.getItemAtPosition(district_spinner.getSelectedItemPosition()).toString();
        if(district_spinner.getSelectedItemPosition()==0)
            Toast.makeText(getActivity(),"Please select a district",Toast.LENGTH_LONG).show();
        String sFireStatus = fireStatus_spinner.getItemAtPosition(fireStatus_spinner.getSelectedItemPosition()).toString();
        if(fireStatus_spinner.getSelectedItemPosition()==0)
            Toast.makeText(getActivity(),"Please select fire status",Toast.LENGTH_LONG).show();
        String sLandUse = landUse_spinner.getItemAtPosition(landUse_spinner.getSelectedItemPosition()).toString();
        if(landUse_spinner.getSelectedItemPosition()==0)
            Toast.makeText(getActivity(),"Please select land use",Toast.LENGTH_LONG).show();
        String sRemarks = editRemarks.getText().toString().trim();
        String sIncidentReason = editIncidentReason.getText().toString().trim();

        String sKilaNumber = "32";
        String sMurrabbaNumber = "23";
        String sAmount = "113";
        String sOwnership = "";
        String sAction ="NO Action";
        Boolean sFIR = false;
        Boolean sChalaan = false;
        if(sFarmerName.length()!=0 && sFatherName.length()!=0 && sVillageCode.length()!=0 && sVillageName.length()!=0 &&
                district_spinner.getSelectedItemPosition()!=0 && fireStatus_spinner.getSelectedItemPosition()!=0 && landUse_spinner.getSelectedItemPosition()!=0) {
            //Toast.makeText(getActivity(),"We are submitting your report.",Toast.LENGTH_LONG).show();

            reportSubmitLoading = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Submitting Report")
                    .setTheme(R.style.CustomDialog)
                    .setCancelable(false)
                    .build();
            reportSubmitLoading.show();
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            JSONObject postParams = new JSONObject();
            try {
                    postParams.put("village_code", sVillageCode);
                    postParams.put("farmer_code", "No Village Code was available");
                    postParams.put("farmer_name", sFarmerName);
                    postParams.put("father_name", sFatherName);
                    postParams.put("longitude", lon);
                    postParams.put("latitude", lat);
                    postParams.put("report_longitude",lon);//need to be changed to current location
                    postParams.put("report_latitude",lat);
                    postParams.put("kila_num", sKilaNumber);
                    postParams.put("murrabba_num", sMurrabbaNumber);
                    postParams.put("incident_reason", sIncidentReason);
                    postParams.put("remarks", sRemarks);
                    postParams.put("amount",sAmount);
                    postParams.put("ownership",sOwnership );
                    postParams.put("action",sAction);
                    postParams.put("fir",sFIR);
                    postParams.put("challan",sChalaan);
                    postParams.put("flag","yes");
                    postParams.put("fire", sFireStatus);
                    postParams.put("created_on_ado","2020-08-08");
                    postParams.put("village",sVillageName);
                    postParams.put("location",id);

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "An exception occured", Toast.LENGTH_LONG).show();
                reportSubmitLoading.dismiss();
                Log.d(TAG, "submitReport: " + e);
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reportSubmitUrl, postParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject singleObject = new JSONObject(String.valueOf(response));
                                String reportId = singleObject.getString("id");
                                Log.d(TAG, "onResponse: " + singleObject);
                                //isReportSubmitted = true;
                                //uploadPhotos();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                reportSubmitLoading.dismiss();
                                Log.d(TAG, "jsonexception: reportSubmitRequest " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError || error instanceof TimeoutError){
                                Toast.makeText(getActivity(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                            }else if (error instanceof AuthFailureError) {
                                // Error indicating that there was an Authentication Failure while performing the request
                                Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                //Indicates that the server responded with a error response
                                Toast.makeText(getActivity(), "This error is case3", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                //Indicates that there was network error while performing the request
                                Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                // Indicates that the server response could not be parsed
                                Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                            }
                            reportSubmitLoading.dismiss();
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


}
