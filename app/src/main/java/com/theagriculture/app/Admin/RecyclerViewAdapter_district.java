package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.R;
import com.theagriculture.app.RegistrationActivity;

import java.util.ArrayList;

public class RecyclerViewAdapter_district extends RecyclerView.Adapter<RecyclerViewAdapter_district.DistrictCustomViewHolder> {
    private Context mContext;
    private ArrayList<String> mDistrictNames;

    public RecyclerViewAdapter_district(Context mContext, ArrayList<String> mDistrictNames) {
        this.mContext = mContext;
        this.mDistrictNames = mDistrictNames;
    }

    @NonNull
    @Override
    public DistrictCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.district_recycler, parent, false);
        final DistrictCustomViewHolder viewHolder = new DistrictCustomViewHolder(view);
        /*
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, adoListofDistrict.class);
                intent.putExtra("district", mDistrictNames.get(viewHolder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });

         */
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DistrictCustomViewHolder holder, int position) {
        if(position%2==0)
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
            //holder.itemView.setBackgroundResource(R.color.district_background);
        else
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            //holder.itemView.setBackgroundResource(R.color.white);
        holder.textView.setText(mDistrictNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mDistrictNames.size();
    }
    //this function has been added to recycler view to call onBindViewHolder() function dynamically so that background color of items in recycler view is changed dynamically
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class DistrictCustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        RelativeLayout itemLayout;
        public DistrictCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);//function to make onClick() valid
            textView = itemView.findViewById(R.id.dist);
            itemLayout = itemView.findViewById(R.id.dist_item);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(mContext,"The position is " + this.getAdapterPosition(),Toast.LENGTH_LONG).show();
            /*
            Intent intent = new Intent(mContext, adoListofDistrict.class);
            intent.putExtra("district", mDistrictNames.get(this.getAdapterPosition()));
            //Intent intent = new Intent(mContext, RegistrationActivity.class);
            mContext.startActivity(intent);
            */
            Bundle bundle = new Bundle();
                bundle.putString("district", mDistrictNames.get(this.getAdapterPosition()));
                DistrictAdo abc = new DistrictAdo();
                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();
        }


    }
}
