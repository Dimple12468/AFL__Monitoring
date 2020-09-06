package com.theagriculture.app.Dda;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.Admin.AdoDdoActivity.AdoDdo_Activity;
import com.theagriculture.app.Admin.DistrictAdoAdapter;
import com.theagriculture.app.R;

public class DistrictAdoAdapter_DDA extends RecyclerView.Adapter<DistrictAdoAdapter_DDA.ViewHolder> {

    Context mcontext;

    @NonNull
    @Override
    public DistrictAdoAdapter_DDA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mcontext).inflate(R.layout.district_ado_adapter,parent,false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictAdoAdapter_DDA.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv1;
        TextView tv2;
        RelativeLayout relativeLayout;
        TextView districtTextview;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv1= itemView.findViewById(R.id.tvuser);
            tv2= itemView.findViewById(R.id.tvinfo);
            relativeLayout = itemView.findViewById(R.id.relativeLayout2);
            districtTextview = itemView.findViewById(R.id.district_info);
            radioButton = itemView.findViewById(R.id.offer_select);

        }
        @Override
        public void onClick(View v) {

        }
    }

}
