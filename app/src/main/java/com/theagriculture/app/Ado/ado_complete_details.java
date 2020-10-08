package com.theagriculture.app.Ado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Admin.Images;
import com.theagriculture.app.Admin.ongoing_details;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class ado_complete_details extends AppCompatActivity {

    private ImageButton back_button;

    private TextView review_address_top;

    private TextView villCodeLeft;
    private TextView villCodeRight;

    private TextView villnameleft;
    private TextView villnameright;

    private TextView districtleft;
    private TextView districtright;

    private TextView nameLeft;
    private TextView nameRight;

    private TextView fatherNameRight;
    private TextView fatherNameLeft;

    private TextView ownLeaseLeft;
    private TextView ownLeaseRight;

    private TextView fireLeft;
    private TextView fireRight;

    private TextView remarksLeft;
    private TextView remarksRight;

    private TextView reasonLeft;
    private TextView reasonRight;

    Button forfeitButton;
    Button images;
    Button editButton;

    private static String TAG = "ReviewReport";

    private String mUrl;
    private boolean isComplete;
    private boolean isAdmin;
    private boolean isOngoing;

    private ArrayList<String> mImagesUrl;
    private ArrayList<String> schemedata;
    private ArrayList<String> programNamedata;
    private ArrayList<String> financialYearNamedata;
    private ArrayList<String> dateOfBenefitdata;
    private TextView noSubsidiesTextView;
    boolean doubleBackToExitPressedOnce=false;
    private String farmerId;
    private String id;
    private String review_address_big;
    private boolean isDdo;


    private String urlfarmer = "https://agriharyana.org/api/farmer";

    private RecyclerView tableRecyclerView;
    private ReviewTableRecycleAdapter reviewTableRecycleAdapter;

    private LinearLayout actionRow;
    private TextView actionLeft;
    private TextView actionRight;
    private LinearLayout amountRow;
    private TextView amountLeft;
    private TextView amountRight;


    //for back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ongoing_details);
        Log.d(TAG,"in onCreate: ");

        Bundle bundle = getIntent().getExtras();
        id = bundle.get("id").toString();
        review_address_big = bundle.get("review_address_top").toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_ongoing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Report");
        TextView title_top = findViewById(R.id.app_name);
        title_top.setText("Report");

        review_address_top = findViewById(R.id.review_address_top);

        villCodeRight = findViewById(R.id.villCodeRight);
        villCodeLeft = findViewById(R.id.villCodeLeft);

        villnameleft = findViewById(R.id.villnameLeft);
        villnameright = findViewById(R.id.villnameRight);

        districtleft = findViewById(R.id.districtLeft);
        districtright = findViewById(R.id.districtRight);

        nameLeft = findViewById(R.id.nameLeft);
        nameRight = findViewById(R.id.nameRight);

        fatherNameRight = findViewById(R.id.fatherNameRight);
        fatherNameLeft = findViewById(R.id.fatherNameLeft);

        ownLeaseLeft = findViewById(R.id.own_lease_Left);
        ownLeaseRight = findViewById(R.id.own_lease_Right);

        fireLeft = findViewById(R.id.fire_Left);
        fireRight = findViewById(R.id.fire_Right);

        remarksLeft = findViewById(R.id.remarksLeft);
        remarksRight = findViewById(R.id.remarksRight);

        reasonLeft = findViewById(R.id.reasonLeft);
        reasonRight = findViewById(R.id.reasonRight);

        mImagesUrl = new ArrayList<>();
        schemedata = new ArrayList<>();
        programNamedata = new ArrayList<>();
        financialYearNamedata = new ArrayList<>();
        dateOfBenefitdata = new ArrayList<>();

        forfeitButton = findViewById(R.id.forfeit);
        images = findViewById(R.id.images);
        editButton = findViewById(R.id.edits_button);

        if(!isAdmin)
            images.setVisibility(View.GONE);

        forfeitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You clicked forefit " ,Toast.LENGTH_LONG).show();
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImagesUrl.toString().equals("[]")) {
                    displayDialog("No Images were Uploaded");
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    Images abc = new Images();
                    abc.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, abc).addToBackStack(null).commit();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You clicked edit " ,Toast.LENGTH_LONG).show();
            }
        });
        review_address_top.setText(review_address_big);

        mUrl = Globals.report_ado +  id + "/";                  //"http://18.224.202.135/api/report-ado/" + id + "/";
        Log.d(TAG,"Report-ADO URL: "+mUrl);
        getData();

    }

    public void getData(){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(valueOf(response));
                    Log.d(TAG,"on Response: "+response + "\n" + "root object" + rootObject);
//                    if (rootObject.getString("detail").equals("Not found.")){
//                        Log.d(TAG, "onResponse: Data not found.... " + view);
//                        nothing_toshow_fragment no_data = new nothing_toshow_fragment();
//                        AppCompatActivity activity = (AppCompatActivity)getContext();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_complete_details, no_data).commit();
//                    }
                    String villCode,name,fatherName,ownership,remarks,reason,village_name,district,fire;
                    try {
                        villCode=rootObject.getString("village_code");
                    } catch (JSONException e) {
                        villCode = "NONE";
                        e.printStackTrace();
                    }
                    try {
                        farmerId = rootObject.getString("farmer_code");
                    } catch (JSONException e) {
                        farmerId = "NONE";
                        e.printStackTrace();
                    }
                    try {
                        name = rootObject.getString("farmer_name");
                    } catch (JSONException e) {
                        name = "NONE";
                        e.printStackTrace();
                    }
                    try {
                        fatherName = rootObject.getString("father_name");
                    } catch (JSONException e) {
                        fatherName = "NONE";
                        e.printStackTrace();
                    }
                    try {
                        ownership = rootObject.getString("ownership");
                    } catch (JSONException e) {
                        ownership = "NONE";
                        e.printStackTrace();
                    }
                    try {
                        remarks = rootObject.getString("remarks");
                    } catch (JSONException e) {
                        remarks = "NONE";
                        e.printStackTrace();
                    }
                    try {
                        reason = rootObject.getString("incident_reason");
                    } catch (JSONException e) {
                        reason= "NONE";
                        e.printStackTrace();
                    }
                    JSONObject location = rootObject.getJSONObject("location");
                    try{
                        village_name = location.getString("village_name");

                    } catch (JSONException e) {
                        village_name = "NONE";
                        e.printStackTrace();
                    }
                    try{
                        district = location.getString("district");

                    } catch (JSONException e) {
                        district = "NONE";
                        e.printStackTrace();
                    }
                    try{
                        fire = rootObject.getString("fire");

                    } catch (JSONException e) {
                        fire = "NONE";
                        e.printStackTrace();
                    }
                    Log.d("error","reached here");
                    try{
                        JSONArray imagesArray = rootObject.getJSONArray("images");

                        for (int i = 0; i < imagesArray.length(); i++) {
                            JSONObject imageObject = imagesArray.getJSONObject(i);
                            String imageUrl = imageObject.getString("image");
                            mImagesUrl.add(imageUrl);
                            //adapter.notifyDataSetChanged();
                        }
                    }
                    catch (Exception e){

                    }

                    fireLeft.setText("Fire:");
                    fireRight.setText(capitalCase(fire));
                    villCodeLeft.setText("Village Code:");
                    villCodeRight.setText(capitalCase(villCode));
                    villnameleft.setText("Village Name:");
                    villnameright.setText(capitalCase(village_name));
                    districtleft.setText("District:");
                    districtright.setText(capitalCase(district));
                    nameLeft.setText("Farmer Name:");
                    nameRight.setText(capitalCase(name));
                    fatherNameLeft.setText("Father Name:");
                    fatherNameRight.setText(capitalCase(fatherName));
                    ownLeaseLeft.setText("Ownership/Lease:");
                    ownLeaseRight.setText(capitalCase(ownership));
                    remarksLeft.setText("Remarks:");
                    remarksRight.setText(capitalCase(remarks));
                    reasonLeft.setText("Incident Reason:");
                    reasonRight.setText(capitalCase(reason));

                }

                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"This exception is "+ e ,Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error","error is "+error.getMessage());


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
                            final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getApplicationContext());
//                            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                            LayoutInflater li = LayoutInflater.from(getApplicationContext());
                            View sheetView = li.inflate(R.layout.no_internet, null);
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
                                    getData();
                                }
                            });

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!doubleBackToExitPressedOnce) {
                                        doubleBackToExitPressedOnce = true;
                                        Toast toast = Toast.makeText(getApplicationContext(),"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is case3", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = preferences.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }

    private String capitalCase(String str)
    {
        String newStr = str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
        return newStr;
    }

    public final void displayDialog(String str){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext(),R.style.AlertDialog);
        builder.setMessage(str);
        androidx.appcompat.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
    }


}

