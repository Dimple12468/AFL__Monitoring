package com.theagriculture.app.Admin.AdoDdoActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.theagriculture.app.Admin.onBackPressed;
import com.theagriculture.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoDdoActivityFragment extends Fragment implements onBackPressed {

    private AdoDdoActivityPagerAdapter adapter;
    private boolean isDdo;

    public AdoDdoActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);
        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search something");
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
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();

                }
                //recyclerViewAdater.getFilter().filter(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


   /* @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ado_ddo_activity, container, false);

        //for toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_in_dda);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        String id = bundle.get("Id").toString();
        isDdo = bundle.getBoolean("isDdo");
        String title = bundle.get("name").toString();

        //toolbar's title
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText(title);
        }else {
            title_top.setText("AFL Monitoring");
        }

        Log.d("ddoId", "onCreate: " + id);


        TabLayout tabLayout = view.findViewById(R.id.ddo_activity_tablayout);
        tabLayout.addTab(tabLayout.newTab());
        if (isDdo)
            tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        ViewPager viewPager = view.findViewById(R.id.ddo_activity_viewpager);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
