package com.theagriculture.app.Dda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.theagriculture.app.R;

public class DdaPendingFragment extends Fragment {

    TabLayout mtablayout;
    Toolbar mtoolbar;
    TabItem assigned;
    TabItem notassigned;
    TabPageDdaAdapter mpageAdapter;
    ViewPager page;
    private static final String TAG = "DdaPendingFragment";
    private boolean isRefresh;

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
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               /* if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();
                }
                adapter.getFilter().filter(newText);*/
                return true;
            }
        });
        searchItem_filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                alert_filter_dialog();
                return true;
            }


        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending,container,false);
        mtablayout = view.findViewById(R.id.tab);
        assigned = view.findViewById(R.id.assigned);
        notassigned = view.findViewById(R.id.notassigned);
        page = view.findViewById(R.id.viewPage);

        androidx.appcompat.widget.Toolbar toolbar =  view.findViewById(R.id.pending_dda_toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        //for title heading
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Pending");
        }else {
            title_top.setText("AFL Monitoring");
        }

        mpageAdapter = new TabPageDdaAdapter(getChildFragmentManager(),mtablayout.getTabCount());
        page.setAdapter(mpageAdapter);

        mtablayout.setupWithViewPager(page);

        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                page.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        page.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtablayout));
        return view;

    }
}
