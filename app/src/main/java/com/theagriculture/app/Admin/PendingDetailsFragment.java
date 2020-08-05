package com.theagriculture.app.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingDetailsFragment extends Fragment {

    private String ado_name;
    private String dda_name;
    private String address_top;
    private RelativeLayout parent;
    private TextView noDetails_text;
    private String id;
    private String urlado;
    private String urldda;
    private String ado_id;
    private String dda_id;

    private String name;
    private String number;

    private String email;
    private String address;
    private String TAG="Detail class";
    private String token;

    private TextView aname;
    private TextView aemail;
    private TextView anumber;
//    private TextView aaddress;

    private TextView bname;
    private TextView bemail;
    private TextView bnumber;
    private TextView baddress;
    private Button editbutton;
    private Button reportbutton;

    private boolean both = false;
    private boolean isPending = false;
    private boolean isOngoing = false;
    boolean doubleBackToExitPressedOnce=false;

    public PendingDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_details, container, false);
        //AppCompatActivity appCompatActivity = new AppCompatActivity();
        //appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_pending);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = this.getArguments();
        if(bundle!=null) {
            id = bundle.get("id").toString();
            ado_name = bundle.get("ado_name").toString().toUpperCase();
            dda_name = bundle.get("dda_name").toString().toUpperCase();
            address_top = bundle.get("address_big").toString().toUpperCase();
            ado_id = bundle.get("ado_pk").toString();
            dda_id = bundle.get("dda_pk").toString();
            isPending = bundle.getBoolean("isPending");
            isOngoing = bundle.getBoolean("isOngoing");
            //Toast.makeText(getActivity(),"value is "+isPending +"value of ongoing "+ isOngoing,Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(getActivity(),"bundle is null",Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(getActivity(),"got ado id "+ ado_id + "dda id "+ dda_id,Toast.LENGTH_LONG).show();

        aname= view.findViewById(R.id.adodetail_name2);
        aemail = view.findViewById(R.id.adodetail_email2);
        anumber = view.findViewById(R.id.adodetail_phone2);

//        aaddress = view.findViewById(R.id.addr2);
//        aaddress.setText(address_top);
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            title_top.setHorizontallyScrolling(true);
            title_top.setMinLines(1);
            title_top.setSingleLine(true);
          //  title_top.setMarqueeRepeatLimit(-1);
            title_top.setSelected(true);
            title_top.setText(address_top);
        }else {
            title_top.setText("AFL Monitoring");
        }

        bname = view.findViewById(R.id.ddadetail_name2);
        bemail = view.findViewById(R.id.ddadetail_email2);
        bnumber = view.findViewById(R.id.ddadetail_phone2);

        editbutton = view.findViewById(R.id.button2);
        reportbutton = view.findViewById(R.id.report_button);
        if(isOngoing){
            reportbutton.setVisibility(View.VISIBLE);
            reportbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    bundle.putString("review_address_top",address_top);
                    //bundle.putBoolean("isDdo", true);
                    //bundle.putBoolean("isAdmin", true);
                    //bundle.putBoolean("isComplete", true);

                    ongoingDetailsFragment abc = new ongoingDetailsFragment();
                    abc.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();
                }
            });
        }


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"You clicked",Toast.LENGTH_LONG).show();
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("token", "");

        urlado = "http://18.224.202.135/api/user/" + ado_id + "/";
        urldda = "http://18.224.202.135/api/user/" + dda_id + "/";


        if(ado_name.equals("NOT ASSIGNED") && dda_name.equals("NOT ASSIGNED")){
            both = false;
            view.setBackground(getActivity().getResources().getDrawable(R.drawable.nothing_clipboard));
            //Toast.makeText(getActivity(),"No data available",Toast.LENGTH_LONG).show();
            //parent.setVisibility(View.GONE);
            //noDetails_text.setVisibility(View.VISIBLE);
        }else if(ado_name.equals("NOT ASSIGNED") && !(dda_name.equals("NOT ASSIGNED"))){
            both = false;
            //Toast.makeText(getActivity(),"Ado not assigned",Toast.LENGTH_LONG).show();
            loadDetails(urlado,false,urldda,true);
        }else if(!(ado_name.equals("NOT ASSIGNED")) && dda_name.equals("NOT ASSIGNED")){
            both = false;
            //Toast.makeText(getActivity(),"DDA not assigned",Toast.LENGTH_LONG).show();
            loadDetails(urlado,true,urldda,false);
            //loadData(urlado,true);
        }else if(!(ado_name.equals("NOT ASSIGNED")) && !(dda_name.equals("NOT ASSIGNED"))){
            both = true;
            //Toast.makeText(getActivity(),"Both assigned",Toast.LENGTH_LONG).show();
            loadDetails(urlado,true,urldda,true);
            //loadData(urlado,true);
        }

        return view;

    }

    private void loadDetails(final String adourl, final boolean isado, final String urldda, final boolean isdda) {
        if(isado){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, adourl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            name = rootObject.getString("name");
                            number = rootObject.getString("number");
                            email = rootObject.getString("email");

                                if (!name.isEmpty())
                                    aname.setText(name);
                                else
                                    aname.setText("NONE");
                                if (!number.isEmpty())
                                    anumber.setText(number);
                                else
                                    anumber.setText("NONE");
                                if (!email.isEmpty())
                                    aemail.setText(email);
                                else
                                    aemail.setText("NONE");
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "an exception occured", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSON EXCEPTION: getDetails " + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Faced error while loading data", Toast.LENGTH_LONG).show();
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
                                    loadDetails(adourl,isado,urldda,isdda);
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
                        Log.d(TAG, "onErrorResponse: getDetails " + error);
                        /*
                        if (error instanceof ClientError) {
                            if (isado) {
                                Toast.makeText(getActivity(), "ado not assigned", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "dda not assigned", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }

                         */
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
        //requestFinished(requestQueue);
    }
        if(isdda){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urldda, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                name = rootObject.getString("name");
                                number = rootObject.getString("number");
                                email = rootObject.getString("email");
                                    if (!name.isEmpty())
                                        bname.setText(name);
                                    else
                                        bname.setText("NONE");
                                    if (!number.isEmpty())
                                        bnumber.setText(number);
                                    else
                                        bnumber.setText("NONE");
                                    if (!email.isEmpty())
                                        bemail.setText(email);
                                    else
                                        bemail.setText("NONE");

                                } catch (JSONException e) {
                                Toast.makeText(getActivity(), "an exception occured", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: JSON EXCEPTION: getDetails " + e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getActivity(), "Faced error while loading data", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "onErrorResponse: getDetails " + error);
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                //This indicates that the reuest has either time out or there is no connection
                               // Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();

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
                            /*
                        if (error instanceof ClientError) {
                            if (isado) {
                                Toast.makeText(getActivity(), "ado not assigned", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "dda not assigned", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }

                         */
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                /*
                if(both){
                    loadData(urldda,false);
                }

                 */
            }
        });
    }
}
