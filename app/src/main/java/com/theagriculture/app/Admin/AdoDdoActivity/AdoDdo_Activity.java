package com.theagriculture.app.Admin.AdoDdoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.theagriculture.app.R;

public class AdoDdo_Activity extends AppCompatActivity {

    private AdoDdoActivityPagerAdapter adapter;
    private boolean isDdo;
    private String TAG = "ADoDdo_Activity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ado_ddo_activity);
        Log.d(TAG,"in onCreate: ");

        Bundle bundle = getIntent().getExtras();
        String id = bundle.get("Id").toString();
        isDdo = bundle.getBoolean("isDdo");
        String title = bundle.get("name").toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_in_dda);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(title);

        TextView title_top = findViewById(R.id.app_name);
//        if (view.isEnabled()){
            title_top.setText(title);
//        }else {
//            title_top.setText("AFL Monitoring");
//        }
//
        Log.d("ddoId", "onCreate: " + id);

        TabLayout tabLayout = findViewById(R.id.ddo_activity_tablayout);
        tabLayout.addTab(tabLayout.newTab());
        if (isDdo)
            tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        ViewPager viewPager = findViewById(R.id.ddo_activity_viewpager);

        int tabCount;
        if (isDdo)
            tabCount = 3;
        else
            tabCount = 2;


        Log.d("before fragments" , id);


        adapter = new AdoDdoActivityPagerAdapter(getSupportFragmentManager(), tabCount);
//        Intent intent = new Intent(this,AdoDdoPending_Activity.class);
//        adapter.addActivity(new AdoDdoPending_Activity(id, isDdo));
//        if (isDdo)
//            adapter.addActivity(new AdoDdoOngoing_activity(id, isDdo));
//        adapter.addActivity(new AdoDdoCompleted_activity(id, isDdo));


        adapter.addFragment(new AdoDdoPending(id, isDdo));
        if (isDdo)
            adapter.addFragment(new AdoDdoOngoing(id, isDdo));
        adapter.addFragment(new AdoDdoCompleted(id, isDdo));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //for back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
