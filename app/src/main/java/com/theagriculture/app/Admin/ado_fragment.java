package com.theagriculture.app.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ado_fragment extends Fragment /*implements AdapterView.OnItemSelectedListener*/ {
    private ArrayList<String> username;
    private ArrayList<String> userinfo;
    private ArrayList<String> mUserId;
    private ArrayList<String> mPkList;
    private ArrayList<String> mDdoNames;
    private ArrayList<String> mDistrictNames;
    private ArrayList<String> mdistrictlist;
    boolean doubleBackToExitPressedOnce = false;

    private String district_list_url;

    private String token;
    private GridLayoutManager gridlayout;

    private View view;
    private final String TAG = "ado_fragment";
    private RecyclerView Rview;
    private AlertDialog dialog;

    private RecyclerView adolist;
    private RecyclerViewAdapter_district customadapter;
    ProgressBar spinner;

    MenuItem searchItem;
    MenuItem searchItem_filter;
    TextView title_top;

    public ado_fragment() {
    }

    //function for menu in toolbar
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
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();

                }
                customadapter.getFilter().filter(newText);
                return true;
            }
        });
        searchItem_filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                alert_filter_dialog();
                return true;
            }


        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //function for alert dialog of filter search
    private void alert_filter_dialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.activity_filter_search, null);

        //Create views for spinners here
        Spinner sp1 = promptsView.findViewById(R.id.status_filter_spinner);
        Spinner sp2 = promptsView.findViewById(R.id.date_filter_spinner);
        Spinner sp3 = promptsView.findViewById(R.id.state_filter_spinner);
        Spinner sp4 = promptsView.findViewById(R.id.district_filter_spinner);
        Spinner sp5 = promptsView.findViewById(R.id.village_filter_spinner);

        String[] status = getResources().getStringArray(R.array.status);
        String[] date = getResources().getStringArray(R.array.date);
        String[] state = {"Any"};
        String[] district = {"Any"};
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

        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, state);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3.setAdapter(state_adapter);
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, district);
        district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp4.setAdapter(district_adapter);
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (R.id.parent == R.id.district_filter_spinner) {
//                    //sp4.setOnItemSelectedListener(new OnSpinnerItemClicked());
//                    LayoutInflater li_dist = LayoutInflater.from(getActivity());
//                    final View dist = li_dist.inflate(R.layout.search_view, null);
//                    final android.app.AlertDialog.Builder alertDialogBuilder_dist = new android.app.AlertDialog.Builder(getActivity());
//                    alertDialogBuilder_dist.setTitle("Filter Search");
//                    alertDialogBuilder_dist.setPositiveButton("Select", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int which) {
//                            Toast.makeText(getActivity(), "Select is clicked", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    alertDialogBuilder_dist.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getActivity(), "Reset is clicked", Toast.LENGTH_LONG).show();
//                            //alertDialogBuilder.setCancelable(true);
//                        }
//                    });
//                    alertDialogBuilder_dist.setView(dist);
//                    alertDialogBuilder_dist.setCancelable(true);
//                    AlertDialog alertDialog_dist = alertDialogBuilder_dist.create();
//                    alertDialog_dist.show();
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> village_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, village);
        village_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp5.setAdapter(village_adapter);
        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ado_fragment, container, false);
        Log.d(TAG, "onCreateView: ");
        spinner = view.findViewById(R.id.ado_progress);
        spinner.setVisibility(View.VISIBLE);
        adolist = view.findViewById(R.id.adolist);

        district_list_url = Globals.districtUrl;                 //= "http://18.224.202.135/api/district/";
        Log.d(TAG,district_list_url);

        username = new ArrayList<>();
        userinfo = new ArrayList<>();
        mUserId = new ArrayList<>();
        mPkList = new ArrayList<>();
        mDdoNames = new ArrayList<>();
        mDistrictNames = new ArrayList<>();
        mdistrictlist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_ado);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        title_top = view.findViewById(R.id.app_name);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("ADO");
        }else {
            title_top.setText("AFL Monitoring");
        }

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("key", "");

        gridlayout = new GridLayoutManager(getActivity(), 1);
        adolist.setLayoutManager(gridlayout);
        getData();

        return view;
    }


    public void getData(){
        RequestQueue district_requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, district_list_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length()==0)
                    //todo add image
                 view.setBackground(getActivity().getResources().getDrawable(R.drawable.svg_nothing_toshow_1));

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject singleObject = response.getJSONObject(i);
                        if (singleObject.getString("district").equalsIgnoreCase("gurugram"))
                            continue;
                        mdistrictlist.add(singleObject.getString("district"));
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"You encountered an exception",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                Collections.sort(mdistrictlist);

                customadapter = new RecyclerViewAdapter_district(getActivity(), mdistrictlist);
                adolist.setAdapter(customadapter);
                spinner.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
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

    public void InitializeFragment(Fragment fragment) {


       AppCompatActivity activity = (AppCompatActivity) fragment.getContext();
       activity.getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,fragment,"a").addToBackStack("a").commit();


   }

}

