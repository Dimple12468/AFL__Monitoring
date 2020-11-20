package com.theagriculture.app.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.theagriculture.app.Dda.SectionAdapter_DDA;
import com.theagriculture.app.Dda.Section_DDA;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;
import com.theagriculture.app.adapter.PendingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;


public class pending_fragment extends Fragment {


    private ArrayList<String> mAddress;
    private ArrayList<String> mpkado;
    private ArrayList<String> mpkdda;
    private ArrayList<String> mId;
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String aid;
    boolean doubleBackToExitPressedOnce = false;

    private static final String TAG = "pending_fragment";
    private String pendingUrl = Globals.pendingDatewiseList;
    ArrayList<Section> sections = new ArrayList<>();
//    ArrayList<Section_DDA> sections_dda = new ArrayList<>();

    private String nextPendingUrl = "null";
    private LinearLayoutManager layoutManager;

    //private AdminLocationAdapter recyclerViewAdater;
    //public PendingAdapter recyclerViewAdater;
    private ItemAdapter item_adapter;
    private SectionAdapter recyclerViewAdater;
//    private SectionAdapter_DDA recyclerViewAdater_dda;

    private ProgressBar progressBar;
    private boolean isNextBusy = false;
    private boolean isSendingNotifications = false;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressBar spinner;
    private boolean isRefresh;
    int count_entry = 0;
    //ProgressDialog pDialog;
    String last_date,last_date_next;

    ArrayList<String> mDid = new ArrayList<>();
    ArrayList<String> mDlocation_name = new ArrayList<>();
    ArrayList<String> mDlocation_address = new ArrayList<>();
    ArrayList<String> mAdaName= new ArrayList<>();
    ArrayList<String> mDdaName= new ArrayList<>();
    ArrayList<String> mpk_ado = new ArrayList<>();
    ArrayList<String> mpk_dda = new ArrayList<>();

    // Required empty public constructor
    public pending_fragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        isRefresh = false;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.pending_fragment, container, false);

        swipeRefreshLayout = view.findViewById(R.id.refreshpull4);
        recyclerView = view.findViewById(R.id.recyclerViewpending);
        progressBar = view.findViewById(R.id.locations_loading);
        spinner = view.findViewById(R.id.pending_progress);
//        spinner.setVisibility(View.VISIBLE);
        Log.d(TAG,"URL: " + pendingUrl);

        new statusDetailsTab().passComponents(recyclerView,spinner);
        new statusDetailsTab().getData(pendingUrl,getContext());

        //for complete scroll for recycler view (from bottom to up(top))
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                //swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        isRefresh = false;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                spinner.setVisibility(View.GONE);
                Log.d(TAG, "onRefresh called from pending_fragment in locations");
                getFragmentManager().beginTransaction().detach(pending_fragment.this).attach(pending_fragment.this).commit();
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

//        final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
//        token = preferences.getString("key", "");
//        Log.d(TAG, "onCreateView: " + token);
//        Log.d(TAG, "onCreateView: inflated fragment_ongoing");

        // spinner.setVisibility(View.VISIBLE);

        new statusDetailsTab().getAdapter(getContext(),R.id.recyclerViewpending);

//        recyclerViewAdater = new SectionAdapter(getActivity(), sections);
//        recyclerView.setAdapter(recyclerViewAdater);
//        recyclerViewAdater.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart: ");
    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
}