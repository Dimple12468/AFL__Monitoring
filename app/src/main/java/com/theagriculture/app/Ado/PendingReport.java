package com.theagriculture.app.Ado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.theagriculture.app.Dda.DdaselectAdo;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.theagriculture.app.AppNotificationChannels.CHANNEL_1_ID;
import static io.fabric.sdk.android.Fabric.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingReport extends Fragment {
    TextView textDummyHintUsername, textDummyHintFather, textDummyHintCode, textDummyHintName;
    EditText editUsername, editFather, editCode, editName, editRemarks, editIncidentReason;
    View view1, view2, view3a, view3b;
    TextInputLayout ti1, ti2, ti3a, ti3b;
    Spinner district_spinner, fireStatus_spinner, landUse_spinner;
    ConstraintLayout reportPage,imagesPage;

    String[] arr;
    String token;
    //ArrayList<String> DistrictName;//for items in spinner
    List DistrictName = new ArrayList<>();
    Button submit_btn;
    String lat, lon, id;
    //String district_list_url = "http://api.theagriculture.tk/api/district/";
    String district_list_url = Globals.districtUrl;
    String imageUploadUrl = Globals.adoPendingReportImageUpload;
    String reportSubmitUrl = Globals.adoPendingReport;

    //String reportSubmitUrl = "http://127.0.0.1:8000/api/report-ado/add/";
    //String imageUploadUrl = "http://api.theagriculture.tk/api/upload/images/";
    //String reportSubmitUrl = "http://api.theagriculture.tk/api/report-ado/add/";
    AlertDialog reportSubmitLoading;

    String reportId;//id required to send images

    //for photos
    Button  done,save;
    FloatingActionButton clickPhoto;
    boolean isFirstPic = true;
    private static String TAG = "CheckInActivity2";
    private static int IMAGE_CAPTURE_RC = 123;
    private ArrayList<File> mImages;
    private String imageFilePath;
    RecyclerView recyclerView;
    public ReportImageRecyAdapter adapter;
    private ArrayList<String> mImagesPath;
    int PhotosUploadedCount = 0;

    //for photo
    FloatingActionButton openGallery;
    /*

    boolean isFirstPic = true;
    private ArrayList<File> mImages;
    int IMAGE_CAPTURE_RC = 123;
    private String imageFilePath;
    private ReportImageRecyAdapter adapter;
    RecyclerView recyclerView;
    private ArrayList<String> mImagesPath;
    int photosUploadedCount = 0;
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder notificationBuilder;

     */

    public PendingReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_pending_report, container, false);

        Toolbar toolbar = view.findViewById(R.id.app__bar_report);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TextView title_top = view.findViewById(R.id.app_name);
        if (title_top.isEnabled()){
            title_top.setText("Report");
        }else {
            title_top.setText("AFL Monitoring");
        }
        // final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getArguments();
        id = bundle.get("id").toString();
        lat = bundle.get("lat").toString();
        lon = bundle.get("long").toString();
        //Toast.makeText(getActivity(),lat+" and "+lon,Toast.LENGTH_LONG).show();

        reportPage = view.findViewById(R.id.report_page);
        imagesPage = view.findViewById(R.id.images_page);

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

        openGallery = view.findViewById(R.id.camera);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"you clicked fab",Toast.LENGTH_LONG).show();
                reportPage.setVisibility(View.GONE);
                imagesPage.setVisibility(View.VISIBLE);
                title_top.setText("Image Upload");
                /*
                if (isFirstPic) {
                    showdialogbox("Attention", "Only 4 Photos are allowed to be taken", "Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    openCameraIntent();//this function is defined below
                                }
                            }, "", null, true);
                    isFirstPic = false;
                } else if (mImages.size() < 4)
                    openCameraIntent();
                else
                    Toast.makeText(getActivity(), "Max Photos Reached...images are "+mImagesPath, Toast.LENGTH_SHORT).show();

                 */
            }
        });

        /////////now
        recyclerView = view.findViewById(R.id.rvimages);
        mImagesPath = new ArrayList<>();
        mImages = new ArrayList<>();
        adapter = new ReportImageRecyAdapter(getActivity(), mImagesPath);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);

        clickPhoto = view.findViewById(R.id.click_photo);
        clickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadingPhotos();
                if (isFirstPic) {
                    openCameraIntent();
                    isFirstPic = false;
                } else if (mImages.size() < 4)
                    openCameraIntent();
                else
                    Toast.makeText(getActivity(), "Max Photos Reached...images are " + mImagesPath, Toast.LENGTH_SHORT).show();

            }
        });

        save = view.findViewById(R.id.save_images);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesPage.setVisibility(View.GONE);
                reportPage.setVisibility(View.VISIBLE);
                title_top.setText("Report");


            }
        });

        /////////till now

        submit_btn = view.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"You submitted " ,Toast.LENGTH_LONG).show();
                checkReport();
                //checkPOST();
            }
        });

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("key", "");
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

        //to get data to district spinner
        getDistrictData();
        district_spinner.setSelection(0, false);
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // String selected_district = district_spinner.getItemAtPosition(district_spinner.getSelectedItemPosition()).toString();
                //Toast.makeText(getActivity(),selected_district + "has been selected" ,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        //adapter for images



        return view;
    }

    //for district spinner
    public void getDistrictData() {
        DistrictName.add("District");
        RequestQueue district_requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, district_list_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject singleObject = response.getJSONObject(i);
                        DistrictName.add(singleObject.getString("district"));
                    }
                    //Toast.makeText(getActivity(),DistrictName.toString(),Toast.LENGTH_LONG).show();
                    //Log.d("spinner", DistrictName.toString());
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

    /*
    public void checkPOST(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("district","ammm");
            postParams.put("district_code","43");
            Toast.makeText(getActivity(), "Getting out of try1 wth postparamas "+postParams.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "An exception occured while doing post parameters", Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://api.theagriculture.tk/api/district/", postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(),"response is "+response.toString(),Toast.LENGTH_SHORT).show();
                        //Process os success response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getActivity(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                if (error instanceof NoConnectionError){
                    Toast.makeText(getActivity(), "1) Check your internet connection!", Toast.LENGTH_LONG).show();
                }
                else if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "2) Check your internet connection!", Toast.LENGTH_LONG).show();
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
            }
                })
                 {
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

     */

    public void checkReport() {
        showdialogbox("Sumbit Report", "Are you sure?", "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sFarmerName = editUsername.getText().toString().trim();
                        String sFatherName = editFather.getText().toString().trim();
                        String sVillageCode = editCode.getText().toString().trim();
                        String sVillageName = editName.getText().toString().trim();
                        String sDistrict = district_spinner.getItemAtPosition(district_spinner.getSelectedItemPosition()).toString();
                        String sFireStatus = fireStatus_spinner.getItemAtPosition(fireStatus_spinner.getSelectedItemPosition()).toString();
                        String sLandUse = landUse_spinner.getItemAtPosition(landUse_spinner.getSelectedItemPosition()).toString();
                        String sIncidentReason = editIncidentReason.getText().toString().trim();
                        String sRemarks = editRemarks.getText().toString().trim();

                        if (sFarmerName.length() == 0)
                            Toast.makeText(getActivity(), "Please enter farmer's name", Toast.LENGTH_LONG).show();

                        else if (sFatherName.length() == 0)
                            Toast.makeText(getActivity(), "Please enter farmer's father name", Toast.LENGTH_LONG).show();

                        else if (sVillageCode.length() == 0)
                            Toast.makeText(getActivity(), "Please enter village code", Toast.LENGTH_LONG).show();

                        else if (sVillageName.length() == 0)
                            Toast.makeText(getActivity(), "Please enter village name", Toast.LENGTH_LONG).show();

                        else if (district_spinner.getSelectedItemPosition() == 0)
                            Toast.makeText(getActivity(), "Please select a district", Toast.LENGTH_LONG).show();

                        else if (fireStatus_spinner.getSelectedItemPosition() == 0)
                            Toast.makeText(getActivity(), "Please select fire status", Toast.LENGTH_LONG).show();

                        else if (landUse_spinner.getSelectedItemPosition() == 0)
                            Toast.makeText(getActivity(), "Please select land use", Toast.LENGTH_LONG).show();

                        else if(mImagesPath.size()==0)
                            Toast.makeText(getActivity(),"Atleast upload one image",Toast.LENGTH_LONG).show();
                        else
                            submitReport();
                    }
                }, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, true);
    }


    public void submitReport() {

        //if(sFarmerName.length()!=0 && sFatherName.length()!=0 && sVillageCode.length()!=0 && sVillageName.length()!=0 &&
        //district_spinner.getSelectedItemPosition()!=0 && fireStatus_spinner.getSelectedItemPosition()!=0 && landUse_spinner.getSelectedItemPosition()!=0) {
        String sFarmerName = editUsername.getText().toString().trim();
        String sFatherName = editFather.getText().toString().trim();
        String sVillageCode = editCode.getText().toString().trim();
        String sVillageName = editName.getText().toString().trim();
        String sDistrict = district_spinner.getItemAtPosition(district_spinner.getSelectedItemPosition()).toString();
        //String sFireStatus = fireStatus_spinner.getItemAtPosition(fireStatus_spinner.getSelectedItemPosition()).toString();
        String sLandUse = landUse_spinner.getItemAtPosition(landUse_spinner.getSelectedItemPosition()).toString();
        String sIncidentReason = editIncidentReason.getText().toString().trim();
        String sRemarks = editRemarks.getText().toString().trim();
        String sFireStatus;
        if(fireStatus_spinner.getSelectedItemPosition()==1)
            sFireStatus = "nofire";
        else
            sFireStatus = "fire";



        reportSubmitLoading = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Submitting Report")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        reportSubmitLoading.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("village_code", sVillageCode);
            postParams.put("farmer_code", "13");
            postParams.put("farmer_name", sFarmerName);
            postParams.put("father_name", sFatherName);
            postParams.put("longitude", lon);
            postParams.put("latitude", lat);
            postParams.put("report_longitude", "13");//need to be changed to current location
            postParams.put("report_latitude", "13");
            postParams.put("kila_num", "31");
            postParams.put("murrabba_num", "31");
            postParams.put("incident_reason", sIncidentReason);
            postParams.put("remarks", sRemarks);
            postParams.put("amount", "5000");
            postParams.put("ownership", "Farmer Singh");
            postParams.put("action", "FIR");
            postParams.put("fir", true);
            postParams.put("challan", true);
            postParams.put("flag", "start");
            postParams.put("fire", sFireStatus);
            postParams.put("village", null);//village pk has to be passed
            postParams.put("location", Integer.parseInt(id));
            //Toast.makeText(getActivity(), postParams.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(getActivity(), "An exception occured while doing post parameters", Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(getActivity(),"making request object",Toast.LENGTH_LONG).show();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(reportSubmitUrl, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                        JSONObject singleObject;
                        try {
                            singleObject = new JSONObject(String.valueOf(response));
                            Toast.makeText(getActivity(),singleObject.toString(),Toast.LENGTH_LONG).show();
                            reportId = singleObject.getString("id");
                            Toast.makeText(getActivity(),"Report id is "+reportId,Toast.LENGTH_LONG).show();
                            uploadingPhotos();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(),"An exception occured while sending report "+e.getMessage(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            reportSubmitLoading.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "1) Check your internet connection!", Toast.LENGTH_LONG).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getActivity(), "2) Check your internet connection!", Toast.LENGTH_LONG).show();
                        }
                        else if(error instanceof ClientError) {
                            Toast.makeText(getActivity(), "This error is client error", Toast.LENGTH_LONG).show();
                        }   else if (error instanceof AuthFailureError) {
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
                        //reportSubmitLoading.dismiss();
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


        // }

    }

    //for displaying dialog boxes
    public AlertDialog showdialogbox(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick, String negativeLabel, DialogInterface.OnClickListener negativeOnclick, boolean isCancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();//defined below
            } catch (IOException e) {
                Toast.makeText(getActivity(),"An exception occured while performing camera action",Toast.LENGTH_LONG).show();
                Log.d(TAG, "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pictureIntent, IMAGE_CAPTURE_RC);//value is defined above
        }


    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE_RC) {
            if (resultCode == RESULT_OK) {
                File file = new File(imageFilePath);
                try {

                    mImagesPath.add(imageFilePath);
                    File compressedFile = new Compressor(getActivity()).compressToFile(file);
                    mImages.add(compressedFile);
                    adapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Unable to load Image, please try again!", Toast.LENGTH_SHORT).show();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void uploadingPhotos(){
        Toast.makeText(getActivity(),"Eneterd uploading photos function",Toast.LENGTH_LONG).show();
        AndroidNetworking.upload(imageUploadUrl)
                .addHeaders("Authorization", "Token " + token)
                .addMultipartParameter("report",reportId)
                .addMultipartFile("image", mImages.get(PhotosUploadedCount))
                .setTag("Upload Images")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //if (bytesUploaded == totalBytes) {
                        //PhotosUploadedCount++;
                        //}
                        Toast.makeText(getActivity(),"uploading image "+PhotosUploadedCount+"bytesUploaded are "+String.valueOf(bytesUploaded)+"total bytes are "+String.valueOf(totalBytes),Toast.LENGTH_LONG).show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         Log.d(TAG, "onResponse: " + response);
                                         PhotosUploadedCount++;
                                         Toast.makeText(getActivity(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                                         if(PhotosUploadedCount==mImages.size()) {
                                             reportSubmitLoading.dismiss();
                                             submit_btn.setText("Submitted");
                                         }
                                     }

                                     @Override
                                     public void onError(ANError anError) {
                                         Log.d(TAG, "onError: " + anError.getErrorBody());
                                         Toast.makeText(getActivity(), "Photos Upload failed, please try again "+ anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                                         reportSubmitLoading.dismiss();
                                     }

                                 }
                );
    }
}


