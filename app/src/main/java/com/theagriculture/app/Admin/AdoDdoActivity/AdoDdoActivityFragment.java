package com.theagriculture.app.Admin.AdoDdoActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.theagriculture.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoDdoActivityFragment extends Fragment {

    private AdoDdoActivityPagerAdapter adapter;
    private boolean isDdo;

    public AdoDdoActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ado_ddo_activity, container, false);

        Bundle bundle = this.getArguments();
        String id = bundle.get("Id").toString();
        isDdo = bundle.getBoolean("isDdo");
        String title = bundle.get("name").toString();

        //Toast.makeText(getActivity(),"Received id "+id + isDdo,Toast.LENGTH_LONG).show();

        Log.d("ddoId", "onCreate: " + id);


        TabLayout tabLayout = view.findViewById(R.id.ddo_activity_tablayout);
        tabLayout.addTab(tabLayout.newTab());
        if (isDdo)
            tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        ViewPager viewPager = view.findViewById(R.id.ddo_activity_viewpager);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setTitle(title);
        int tabCount;
        if (isDdo)
            tabCount = 3;
        else
            tabCount = 2;

        //adapter = new AdoDdoActivityPagerAdapter(getActivity().getSupportFragmentManager(), tabCount);
        adapter = new AdoDdoActivityPagerAdapter(getChildFragmentManager(), tabCount);
        adapter.addFragment(new AdoDdoPending(id, isDdo));
        if (isDdo)
            adapter.addFragment(new AdoDdoOngoing(id, isDdo));
        adapter.addFragment(new AdoDdoCompleted(id, isDdo));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
