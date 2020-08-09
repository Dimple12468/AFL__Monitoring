package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.theagriculture.app.Ado.ReviewTableRecycleAdapter;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteDetailsFragment extends Fragment {
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

    Button startButton;
    Button editButton;
    Button images;
    Button forfeit;

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

    private String farmerId;
    private String id;
    private String review_address_big;
    private boolean isDdo;
    boolean doubleBackToExitPressedOnce=false;


    private String urlfarmer = "https://agriharyana.org/api/farmer";

    private RecyclerView tableRecyclerView;
    private ReviewTableRecycleAdapter reviewTableRecycleAdapter;

    private LinearLayout actionRow;
    private TextView actionLeft;
    private TextView actionRight;
    private LinearLayout amountRow;
    private TextView amountLeft;
    private TextView amountRight;

    public CompleteDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ongoing_details, container, false);
        View view = inflater.inflate(R.layout.fragment_complete_details, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_completed);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = this.getArguments();
        id = bundle.get("id").toString();
        review_address_big = bundle.get("review_address_top").toString();//address of the incident
        //Toast.makeText(getActivity(),"Got id="+id + "address="+ review_address_big,Toast.LENGTH_LONG).show();


        review_address_top = view.findViewById(R.id.review_address_top);

        villCodeRight = view.findViewById(R.id.villCodeRight);
        villCodeLeft = view.findViewById(R.id.villCodeLeft);

        villnameleft = view.findViewById(R.id.villnameLeft);
        villnameright = view.findViewById(R.id.villnameRight);

        districtleft = view.findViewById(R.id.districtLeft);
        districtright = view.findViewById(R.id.districtRight);

        nameLeft = view.findViewById(R.id.nameLeft);
        nameRight = view.findViewById(R.id.nameRight);


        fatherNameRight = view.findViewById(R.id.fatherNameRight);
        fatherNameLeft = view.findViewById(R.id.fatherNameLeft);

        ownLeaseLeft = view.findViewById(R.id.own_lease_Left);
        ownLeaseRight = view.findViewById(R.id.own_lease_Right);

        fireLeft = view.findViewById(R.id.fire_Left);
        fireRight = view.findViewById(R.id.fire_Right);

        remarksLeft = view.findViewById(R.id.remarksLeft);
        remarksRight = view.findViewById(R.id.remarksRight);

        reasonLeft = view.findViewById(R.id.reasonLeft);
        reasonRight = view.findViewById(R.id.reasonRight);

        mImagesUrl = new ArrayList<>();
        schemedata = new ArrayList<>();
        programNamedata = new ArrayList<>();
        financialYearNamedata = new ArrayList<>();
        dateOfBenefitdata = new ArrayList<>();


        editButton = view.findViewById(R.id.edit_button);
        images = view.findViewById(R.id.images);
        forfeit = view.findViewById(R.id.forfeit_button);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"You clicked edit " ,Toast.LENGTH_LONG).show();
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"You clicked forefit " ,Toast.LENGTH_LONG).show();
                if(mImagesUrl.toString().equals("[]")) {
                    //Toast.makeText(getActivity(), "No images uploaded ", Toast.LENGTH_LONG).show();
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

        forfeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"You clicked forfeit " ,Toast.LENGTH_LONG).show();
            }
        });


        review_address_top.setText(review_address_big);

        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Location Details");
            /*title_top.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            title_top.setHorizontallyScrolling(true);
            title_top.setMinLines(1);
            title_top.setMarqueeRepeatLimit(-1); //for infinite
            title_top.setText(review_address_big);*/
        }else {
            title_top.setText("AFL Monitoring");
        }

        mUrl = "http://18.224.202.135/api/report-ado/" + id + "/";
        //Log.d("click","reached herw"+id);
        // mUrl = "https://jsonplaceholder.typicode.com/todos/1";
        getData();


        return view;
    }
    /*
    @Override
    public void onBackPressed() {
        if(some condition) {
            // do something
        } else {
            super.onBackPressed();
        }

     */
    /*
    public void onBackPressed(){
        getActivity().finish();//will pop previous activity from stack
    }

     */
    public void getData(){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //String abcd= response.toString();
                //Toast.makeText(getActivity(),"This response" + abcd,Toast.LENGTH_LONG).show();
                //villCodeLeft.setText(abcd);
                try {
                    JSONObject rootObject = new JSONObject(valueOf(response));
                    String villCode = rootObject.getString("village_code");
                    farmerId = rootObject.getString("farmer_code");
                    Log.d(TAG, "onResponse: farmerid"+farmerId);
                    String name = rootObject.getString("farmer_name");
                    String fatherName = rootObject.getString("father_name");
                    String ownership = rootObject.getString("ownership");

                    String remarks = rootObject.getString("remarks");
                    String reason = rootObject.getString("incident_reason");
                    JSONObject location = rootObject.getJSONObject("location");
                    //Toast.makeText(getActivity(),location.toString(),Toast.LENGTH_LONG).show();
                    String village_name = location.getString("village_name");
                    String district = location.getString("district");
                    String fire = rootObject.getString("fire");

                    JSONArray imagesArray = rootObject.getJSONArray("images");
                    try {
                        for (int i = 0; i < imagesArray.length(); i++) {
                            JSONObject imageObject = imagesArray.getJSONObject(i);
                            String imageUrl = imageObject.getString("image");
                            mImagesUrl.add(imageUrl);
                            //adapter.notifyDataSetChanged();
                        }
                    }
                    catch (Exception e){

                    }
                    //review_address_top.setText(village_name.toUpperCase()+","+ district.toUpperCase());
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
                    Toast.makeText(getActivity(),"This exception is "+ e ,Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            //This indicates that the reuest has either time out or there is no connection
                            //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
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

                        } else if (error instanceof AuthFailureError) {
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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = preferences.getString("token", "");
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

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity(),R.style.AlertDialog);
        builder.setMessage(str);
        androidx.appcompat.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
    }
}
