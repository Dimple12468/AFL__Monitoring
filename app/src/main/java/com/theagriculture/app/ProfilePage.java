package com.theagriculture.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {
    String userId,completedCount,pendingCount;
    TextView userName,pCount,cCount,position,userAddress,userNumber,userMail;
    RecyclerView recyclerView;
    ProfilePageAdapter adapter;
    ArrayList<String> location, district;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        userName = findViewById(R.id.user_name);
        position = findViewById(R.id.position);
        userNumber = findViewById(R.id.user_phonenumber);
        userAddress = findViewById(R.id.user_address);
        userMail = findViewById(R.id.user_email);
        recyclerView = findViewById(R.id.status_recycler);

        location = new ArrayList<>();
        district = new ArrayList<>();
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        district.add("abc");
        district.add("abc");
        district.add("abc");
        district.add("abc");
        district.add("abc");

        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        userName.setText(preferences.getString("Name",""));
        position.setText(preferences.getString("typeOfUser","").toUpperCase());
        userNumber.setText(preferences.getString("PhoneNumber",""));
        userAddress.setText(preferences.getString("Address",""));
        userMail.setText(preferences.getString("Email",""));

        adapter = new ProfilePageAdapter(getApplicationContext(),location,district,false,false,false);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        //DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        //recyclerView.addItemDecoration(divider);

    }
}
