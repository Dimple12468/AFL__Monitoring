package com.theagriculture.app.Ado;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class ado_pending_fragment extends Fragment {

    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private RecyclerView recyclerView;
    //private AdoListAdapter adoListAdapter;
    private ArrayList<String> longitude;
    private ArrayList<String> latitude;
    private ArrayList<String> idList;
    private String url = "http://18.224.202.135/api/locations/ado/pending";
    private String nextUrl;
    private boolean isNextBusy = false;
    View view;
    private boolean isRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SectionAdapter_ado adoListAdapter;
    ArrayList<Section_ado> sections = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ProgressBar spinner;

    MenuItem searchItem;
    MenuItem searchItem_filter;

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
                //customadapter.getFilter().filter(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /*
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();

                }
                //customadapter.getFilter().filter(newText);

                 */
                return true;
            }
        });
        searchItem_filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // alert_filter_dialog();
                return true;
            }


        });


        // MenuItemCompat.setOn
        /*searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //searchItem_filter.setVisible(false);
               // if (getSupportActionBar().setDisplayHomeAsUpEnabled(true))


                searchItem_filter.setVisible(false);
                dialog.invalidateOptionsMenu();
                return false;
            }
        });*/

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ado_pending_fragment,container,false);
        mtextview1 = new ArrayList<>();
        mtextview2 = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();
        idList = new ArrayList<>();
        isRefresh = false;


        Toolbar toolbar = view.findViewById(R.id.app__bar_ado_pending);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        final ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);//to display search icon

        recyclerView = view.findViewById(R.id.ado_pending_rv);

        spinner = view.findViewById(R.id.ado_pending_progress);
        spinner.setVisibility(View.VISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull9);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(ado_pending_fragment.this).attach(ado_pending_fragment.this).commit();
            }
        });

        Log.d("pending", "onCreateView: ");
        //add data in the array with load data
        /*
        getData(url);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.ado_pending_rv);
        adoListAdapter = new AdoListAdapter(getContext(), mtextview1, mtextview2, idList);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adoListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

         */
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();


        getData(url);





        return view;
    }


    private void getData(String url) {
        sections=new ArrayList<>();
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
                                adoListAdapter.notifyDataSetChanged();

                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }
                            /*
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String id = singleObject.getString("id");
                                idList.add(id);
                                String location_name = singleObject.getString("village_name");
                                String location_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String slongitude = singleObject.getString("longitude");
                                String slatitude = singleObject.getString("latitude");
                                mtextview1.add(location_name.toUpperCase());
                                mtextview2.add(location_address.toUpperCase());
                                longitude.add(slongitude);
                                latitude.add(slatitude);
                                adoListAdapter.sendPostion(longitude,latitude);
                            }

                             */

                            String[][] arr = new String[6][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name");
                                String dlocation_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                            }
                            String inter;
                            for(int i=0;i<resultsArray.length()-1;i++){
                                for(int j=0;j<resultsArray.length()-i-1;j++){
                                    String idate = arr[0][j];
                                    String ndate = arr[0][j+1];
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
                                        for(int k=0;k<6;k++) {
                                            inter = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = inter;
                                        }
                                    }
                                }
                            }
                            ArrayList<String> mDid = new ArrayList<>();
                            ArrayList<String> mDlocation_name = new ArrayList<>();
                            ArrayList<String> mDlocation_address = new ArrayList<>();
                            ArrayList<String> mlatitude= new ArrayList<>();
                            ArrayList<String> mlongitude= new ArrayList<>();
                            String predate=arr[0][0];
                            //String predate = null;
                            for(int i=0;i<resultsArray.length();i++){
                                String idate = arr[0][i];
                                if(predate.equals(idate)){
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);

                                    //predate=idate;
                                }
                                else{
                                    sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,true,false));
                                    mDid = new ArrayList<>();
                                    mDlocation_name = new ArrayList<>();
                                    mDlocation_address = new ArrayList<>();
                                    mlatitude = new ArrayList<>();
                                    mlongitude = new ArrayList<>();
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
                                    //date.equals(idate);
                                }
                                //predate.equals(idate);
                                predate=idate;
                            }
                            sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,true,false));
                            //adoListAdapter.mshowshimmer = false;
                            adoListAdapter.notifyDataSetChanged();
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.GONE);
                            //Fragment fragment = getFragmentManager().findFragmentById(R.id.rootView);
                            //fragment.getView().setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError || error instanceof TimeoutError)
                            Toast.makeText(getActivity(), "Please Check your internet connection",
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), "Something went wrong, please try again",
                                    Toast.LENGTH_LONG).show();

                        isNextBusy = false;
                        Log.d(TAG, "onErrorResponse: " + error);
                        spinner.setVisibility(View.GONE);
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = linearLayoutManager.getItemCount();
                    pastItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = linearLayoutManager.getChildCount();
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

    //Not working
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_top_bar , menu);
        //return true;

        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search something");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //MenuItem action_done = menu.findItem(R.id.ic_file_upload);
        //menuIconColor(action_done, R.drawable.tab_color);
        return super.onCreateOptionsMenu(menu);
    }

     */


    /*

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        isRefresh = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        isRefresh = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (isRefresh) {
            getFragmentManager().beginTransaction().detach(ado_pending_fragment.this)
                    .attach(ado_pending_fragment.this).commit();
            Log.d(TAG, "onResume: REFRESH");
            isRefresh = false;
        }
    }

     */
}
