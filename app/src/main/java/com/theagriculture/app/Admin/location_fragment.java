package com.theagriculture.app.Admin;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.theagriculture.app.R;

import java.util.Collections;

public class location_fragment extends Fragment {

    TabLayout mtablayout;
    Toolbar mtoolbar;
    TabItem pending;
    TabItem completed;
    TabPageAdapter mpageAdapter;
    ViewPager pager;
    TextView first_letter,image_letter;

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
                alert_filter_dialog();
                return true;
            }


        });
        super.onCreateOptionsMenu(menu, inflater);
    }

   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.search_in_title:
                searchItem_filter.setVisible(true);
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void alert_filter_dialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.activity_filter_search, null);

        //Create views for spinners here
        Spinner sp1 = promptsView.findViewById(R.id.status_filter_spinner);
        Spinner sp2 = promptsView.findViewById(R.id.date_filter_spinner);
        Spinner sp3 = promptsView.findViewById(R.id.state_filter_spinner);
        Spinner sp4 = promptsView.findViewById(R.id.district_filter_spinner);
        Spinner sp5 = promptsView.findViewById(R.id.village_filter_spinner);

        String[] status = getResources().getStringArray(R.array.status);
        String[] date = getResources().getStringArray(R.array.date);
        String state =  "Any" ;
        String district =  "Any" ;
        String village =  "Any" ;

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

        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, Collections.singletonList(state));
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

        ArrayAdapter<String> district_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, Collections.singletonList(district));
        district_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp4.setAdapter(district_adapter);
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> village_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, Collections.singletonList(village));
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


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment,container,false);
        setHasOptionsMenu(true);


        mtablayout = view.findViewById(R.id.tabLayout);
        pending = view.findViewById(R.id.pending);
        completed = view.findViewById(R.id.completed);
        pager = view.findViewById(R.id.viewPager);
        mpageAdapter = new TabPageAdapter(getChildFragmentManager(),mtablayout.getTabCount());
        pager.setAdapter(mpageAdapter);

        //first_letter = view.findViewById(R.id.address);
        //image_letter = view.findViewById(R.id.my_letter);
        //image_letter.setText(String.valueOf(first_letter.getText().toString().charAt(0)));

        //SearchView searchView ;
        //SearchView search = (SearchView) item.getActionView();
        //search.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));

        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtablayout));
        pager.setOffscreenPageLimit(3);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}

