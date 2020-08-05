package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistrictStateFragment extends Fragment {
    String token;
    String name;
    String typeOfUser;
    String pk;
    GraphView graphView;
    int primary;
    DataPoint[] values;
    boolean flag = true;
    Button btndate;
    private String date;
    private String start_date;
    private String end_date;

    private String start_date_set ;
    private String end_date_set ;
    private int points;
    ProgressBar spinner;

    private ImageView image_in_spinner;
    private TextView totalPendingTextView;
    private TextView totalOngoingTextView;
    private TextView totalCompletedTextView;
    private RecyclerView pierecycler;
    private ArrayList<String> distlist;
    private ArrayList<Integer> pending;
    private ArrayList<Integer> ongoing;
    private ArrayList<Integer> completed;
    boolean doubleBackToExitPressedOnce = false;

    private String URL_initial;
    private int points_initial;
    private String start_date_initial;
    private String end_date_initial;

    int pending_total;
    int completed_total;
    int ongoing_total;


    //final String burl="http://18.224.202.135/api/count-reports/?date=";
    private String URL;
    private Count_Adapter adapter;
    LineGraphSeries<DataPoint> series;
    LineChart lineChart;
    //ArrayList<Integer> count=new ArrayList<Integer>();
    //ArrayList<String> start_date= new ArrayList<String>();
    String mURL;

    Spinner spin;
    Legend legend;
    ImageView iv1,iv2;

    int Color_arr[] = {Color.parseColor("#1F78B4"),Color.parseColor("#7B26C6"),Color.parseColor("#B2DF8A")};
    /*int Color_arr[] = {Color.argb(1,31, 120, 180),
            Color.argb(1,123, 38, 198),
            Color.argb(1,178, 223, 138)};*/
    String[] legend_name = {"Pending" , "Ongoing" ,"Completed"};

    public DistrictStateFragment() {
        // Required empty public constructor
    }


   /* @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);
        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        searchItem.setVisible(Invi)
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
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();
                }
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getContext().getTheme().applyStyle(R.style.calendar_theme, true);


       // MyFragment myFragment = (MyFragment)getFragmentManager().findFragmentByTag("MY_FRAGMENT");
       // Context ctx = new ContextThemeWrapper(getActivity(), R.style.calendar_theme);
        //LayoutInflater li = inflater.cloneInContext(ctx);
        //ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(),R.style.calendar_theme);
        //LayoutInflater li =   getActivity().getLayoutInflater().cloneInContext(contextThemeWrapper);
        View view = inflater.inflate(R.layout.fragment_district_state, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_stat);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        //setHasOptionsMenu(true);

     //   iv1 = view.findViewById(R.id.ham);
        //iv2 = view.findViewById(R.id.se1);
       // iv1.setVisibility(View.GONE);
        //iv2.setVisibility(View.GONE);*/
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Stats");
        }else {
            title_top.setText("AFL Monitoring");
        }


        mURL = "http://18.224.202.135/api/countReportBtwDates/?start_date=2019-10-10&end_date=2019-11-20&points=8";
        totalPendingTextView = view.findViewById(R.id.total_pending);
        totalOngoingTextView = view.findViewById(R.id.total_ongoing);
        totalCompletedTextView = view.findViewById(R.id.total_completed);
        btndate = view.findViewById(R.id.show_btn);
        spinner = view.findViewById(R.id.graph_loading);
        //image_in_spinner = view.findViewById(R.id.spin_highlight);
        lineChart = (LineChart)view.findViewById(R.id.lineChart);
        pierecycler = view.findViewById(R.id.pierecycler);
        pierecycler.setHasFixedSize(true);
        lineChart.getDescription().setEnabled(false);

        legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLACK);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(10);
        lineChart.invalidate();

        LegendEntry[] legendEntry = new LegendEntry[3];
        for (int i=0;i<legendEntry.length;i++)
        {
            LegendEntry entry = new LegendEntry();
            entry.formColor = Color_arr[i];
            entry.label = String.valueOf(legend_name[i]);
            legendEntry[i] = entry;
        }
        legend.setCustom(legendEntry);


        Calendar c = Calendar.getInstance();

        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);

        final int day1 = c.get(Calendar.DAY_OF_MONTH);
        final int month1 = c.get(Calendar.MONTH);
        final int year1 = c.get(Calendar.YEAR);

        start_date_set = year + "-" + (month ) + "-" + (day);
        end_date_set = year1 + "-" + (month1 + 1) + "-" + (day1);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        /*
        String pk = preferences.getString("pk","");
         primary=Integer.valueOf(pk);
         String typeofuser = preferences.getString("typeOfUser","");
         String username = preferences.getString("Name","");

          */
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        pierecycler.setLayoutManager(linearLayoutManager);
        //pierecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        //adapter=new Count_Adapter(getActivity(),distlist,pending,ongoing,completed);
        //pierecycler.setAdapter(adapter);

        //getGraph(mURL,"All");

        //code for drop-down
        String[] states = { "All", "Pending", "Ongoing", "Completed"};
        spin = (Spinner) view.findViewById(R.id.spinner1);
        int[] images = {R.drawable.for_pending, R.drawable.for_pending,
                R.drawable.for_ongoing,R.drawable.for_completed};

        Drawable[] drawable = {getResources().getDrawable(R.drawable.for_pending),
                getResources().getDrawable(R.drawable.for_pending),
                getResources().getDrawable(R.drawable.for_ongoing),
                getResources().getDrawable(R.drawable.for_completed)};
        CustomSpinnerAdapter adapt = new CustomSpinnerAdapter(getActivity(),states,images,drawable);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapt);
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);*/
        spinner.setVisibility(View.VISIBLE);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"position is "+ position + "date is "+ start_date_set.toString()+end_date_set.toString(),Toast.LENGTH_LONG).show();
                String spin_url = "http://18.224.202.135/api/countReportBtwDates/?start_date="+start_date_set.toString()+"&end_date="+ end_date_set.toString()+"&points=8";
                String state = null;
                if(position==0) {
                    state = "All";
                    //image_in_spinner.setImageResource(R.drawable.for_pending);
                }if(position==1) {
                    state = "Pending";
                    //image_in_spinner.setImageResource(R.drawable.for_pending);
                }if(position==2) {
                    state = "Ongoing";
                    //image_in_spinner.setImageResource(R.drawable.for_ongoing);
                }if(position==3) {
                    state = "Completed";
                   // image_in_spinner.setImageResource(R.drawable.for_completed);
                }
                String currDateFormat = "yyyy-MM-dd";
                String currdatestart = start_date_set;
                String currdateend = end_date_set;
                String Format = "MMM dd";
                DateFormat FromDF = new SimpleDateFormat(currDateFormat);
                FromDF.setLenient(false);  // this is important!
                Date FromDate = null;
                Date ToDate = null;
                try {
                    FromDate = FromDF.parse(currdatestart);
                    ToDate = FromDF.parse(currdateend);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dateStringresultstart = new SimpleDateFormat(Format).format(FromDate);
                String dateStringresultend = new SimpleDateFormat(Format).format(ToDate);
                //Toast.makeText(getActivity(),dateStringresultstart+" and "+dateStringresultend,Toast.LENGTH_LONG).show();
                btndate.setText(dateStringresultstart+" - "+dateStringresultend);



                getGraph(spin_url,state);
                //Log.v("item", (String) parent.getItemAtPosition(position));
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //plotting graph

        // Setting date
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateResolver();
            }
        });

        return view;
    }
    public void dateResolver(){
        // Material date picker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
       // builder.setTheme(R.style.dist_stat_Theme);
        final MaterialDatePicker materialDatePicker = builder.build();
        //builder.setTheme(R.style.calendar_theme);
        //final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
        materialDatePicker.show(getFragmentManager(), "date picker");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                btndate.setText(materialDatePicker.getHeaderText());
                Long s_date = selection.first;
                Long e_date = selection.second;
                Date date1 = new Date(s_date);
                Date date2 = new Date(e_date);
                //Toast.makeText(getActivity(),date1.toString()+ date2.toString(),Toast.LENGTH_LONG).show();
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                start_date_set = df2.format(date1).toString();
                end_date_set = df2.format(date2).toString();
                System.out.println(start_date_set);
                System.out.println(end_date_set);
                String btn_url="http://18.224.202.135/api/countReportBtwDates/?start_date=" + start_date_set + "&end_date=" + end_date_set + "&points=8";
                String state = String.valueOf(spin.getSelectedItem());
                getGraph(btn_url,state);
                //System.out.println(URL);
                //Toast.makeText(getActivity(),"You clicked "+ start_date_set+ "and "+ end_date_set + "spin has "+ String.valueOf(spin.getSelectedItem()),Toast.LENGTH_LONG).show();
                //getData(URL,start_date_set,end_date_set);
            }
        });

    }
    public void getGraph(final String url, final String state){
        final ArrayList<String> xAxis = new ArrayList<>();//for x-label
        final ArrayList<Entry> yAxis1 = new ArrayList<>();//pending entry
        final ArrayList<Entry> yAxis2 = new ArrayList<>();//completed entry
        final ArrayList<Entry> yAxis3 = new ArrayList<>();//ongoing entry

        //for recycler view
        distlist = new ArrayList<>();
        pending = new ArrayList<>();
        ongoing = new ArrayList<>();
        completed = new ArrayList<>();

        //for total count
        pending_total = 0;
        completed_total = 0;
        ongoing_total = 0;

        //get Data
        RequestQueue district_requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray pending_array = response.getJSONArray("pending_count");
                    for (int i = 0; i < pending_array.length(); i++) {
                        JSONObject pendingObject = pending_array.getJSONObject(i);
                        String start = pendingObject.getString("start");
                        Integer data = pendingObject.getInt("data");
                        yAxis1.add(new Entry(i,data));
                        xAxis.add(start);
                    }

                    JSONArray complete_array = response.getJSONArray("completed_count");
                    for (int i = 0; i < complete_array.length(); i++) {
                        JSONObject completedObject = complete_array.getJSONObject(i);
                        String start = completedObject.getString("start");
                        Integer data = completedObject.getInt("data");
                        yAxis2.add(new Entry(i,data));
                    }

                    JSONArray ongoing_array = response.getJSONArray("ongoing_count");
                    for (int i = 0; i < ongoing_array.length(); i++) {
                        JSONObject ongoingObject = ongoing_array.getJSONObject(i);
                        String start = ongoingObject.getString("start");
                        Integer data = ongoingObject.getInt("data");
                        yAxis3.add(new Entry(i,data));
                    }


                    JSONObject resultsObject = response.getJSONObject("results");
                    Iterator<String> itr = resultsObject.keys();
                    while(itr.hasNext())
                    {
                        String place = itr.next();
                        Object districtObject = resultsObject.get(place);
                        Log.d("Logs", "onResponse: place" + place + "object " + districtObject);
                        //Toast.makeText(getActivity(),place+" and "+districtObject.toString(),Toast.LENGTH_LONG).show();
                        int pendingCount = ((JSONObject)districtObject).getInt("pending");
                        int ongoingCount = ((JSONObject)districtObject).getInt("ongoing");
                        int completedCount = ((JSONObject)districtObject).getInt("completed");
                        distlist.add(place);
                        pending.add(pendingCount);
                        ongoing.add(ongoingCount);
                        completed.add(completedCount);
                        pending_total = pending_total + pendingCount;
                        ongoing_total = ongoing_total + ongoingCount;
                        completed_total = completed_total + completedCount;
                    }

                    totalPendingTextView.setText(String.valueOf(pending_total));
                    totalOngoingTextView.setText(String.valueOf(ongoing_total));
                    totalCompletedTextView.setText(String.valueOf(completed_total));
                    /*
                    //to show number of districts with counts
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    pierecycler.setLayoutManager(linearLayoutManager);
                    pierecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

                    */
                    adapter=new Count_Adapter(getActivity(),distlist,pending,ongoing,completed);
                    pierecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();






                    final String[] xaxis = new String[xAxis.size()];
                    for(int i=0;i<xAxis.size();i++) {
                        //xaxis[i] = xAxis.get(i);
                        String fromDateFormat = "yyyy-MM-dd";
                        String fromdate = xAxis.get(i);
                        String CheckFormat = "dd MMM";
                        String dateStringFrom;
                        DateFormat FromDF = new SimpleDateFormat(fromDateFormat);
                        FromDF.setLenient(false);  // this is important!
                        Date FromDate = null;
                        try {
                            FromDate = FromDF.parse(fromdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dateStringFrom = new SimpleDateFormat(CheckFormat).format(FromDate);
                        xaxis[i] = dateStringFrom.toString();
                    }

                    final XAxis xAxes;
                    YAxis yAxisRight;

                    xAxes = lineChart.getXAxis();
                    xAxes.setPosition(XAxis.XAxisPosition.BOTTOM);

                    yAxisRight = lineChart.getAxisRight();
                    yAxisRight.setEnabled(false);
                    xAxes.setValueFormatter(new IndexAxisValueFormatter(xaxis));

                    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                    LineDataSet lineDataSet1 = new LineDataSet(yAxis1,"Pending");
                    lineDataSet1.setDrawCircles(false);
                    lineDataSet1.setColor(Color.argb(1,31, 120, 180));
                    lineDataSet1.setDrawFilled(true);
                    lineDataSet1.setFillColor(Color.argb(1,31, 120, 180));

                    LineDataSet lineDataSet2 = new LineDataSet(yAxis2,"Completed");
                    lineDataSet2.setDrawCircles(false);
                    lineDataSet2.setColor(Color.argb(1,178, 223, 138));
                    lineDataSet2.setDrawFilled(true);
                    lineDataSet2.setFillColor(Color.argb(1,178, 223, 138));

                    LineDataSet lineDataSet3 = new LineDataSet(yAxis3,"Ongoing");
                    lineDataSet3.setDrawCircles(false);
                    lineDataSet3.setColor(Color.argb(1,123, 38, 198));
                    lineDataSet3.setDrawFilled(true);
                    lineDataSet3.setFillColor(Color.argb(1,123, 38, 198));

                    if(state.equals("All") || state.equals("Pending"))
                        lineDataSets.add(lineDataSet1);


                    if(state.equals("All") || state.equals("Ongoing"))
                        lineDataSets.add(lineDataSet3);

                    if(state.equals("All") || state.equals("Completed"))
                        lineDataSets.add(lineDataSet2);


                    LineData data = new LineData(lineDataSets);
                    lineChart.setData(data);
                    lineChart.invalidate();
                    lineChart.setVisibleXRangeMaximum(20);
                    spinner.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Exception", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    spinner.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //spinner.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    //Toast.makeText(getActivity(), "Not connected to internet", Toast.LENGTH_LONG).show();
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
                            getGraph(url,state);
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
                    Toast.makeText(getActivity(), "This is a server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                spinner.setVisibility(View.GONE);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        district_requestQueue.add(jsonObjectRequest);
        pierecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d(TAG, "onScrolled: out DX " + dx + " DY " + dy);
            }
        });


    }
}