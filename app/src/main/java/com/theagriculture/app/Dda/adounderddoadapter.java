package com.theagriculture.app.Dda;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.theagriculture.app.Admin.ongoingDetailsFragment;
import com.theagriculture.app.R;
import com.theagriculture.app.adapter.PendingAdapter;

import java.util.ArrayList;

public class adounderddoadapter extends RecyclerView.Adapter<adounderddoadapter.Holder> {

    Context context;
    ArrayList<String> ado_namelist;
    //public boolean showShimmer = true;
    private ArrayList<String> mAdoIds;
    private String mDistrictId;
    private ArrayList<ArrayList<Integer>> villagesMap;
    private ArrayList<String> adoPhone;
    //private int shimmerCount = 7;

    public adounderddoadapter(Context context, ArrayList<String> ado_name, ArrayList<String> mAdoIds,
                              ArrayList<ArrayList<Integer>> villagesMap, ArrayList<String> adoPhone) {
        this.context = context;
        this.ado_namelist = ado_name;
        this.mAdoIds = mAdoIds;
        this.villagesMap = villagesMap;
        this.adoPhone = adoPhone;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adounderddo,parent,false);
        return new Holder(view);

        /*
        final Holder holder = new Holder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!showShimmer) {
                    Intent intent = new Intent(context, villagenameActivity.class);
                    intent.putExtra("districtId", mDistrictId);
                    intent.putExtra("adoId", mAdoIds.get(holder.getAdapterPosition()));
                    Log.d("ADOUNDERDDA", "onClick: " + villagesMap.get(holder.getAdapterPosition()) + " dfsdfsdf" + mAdoIds.size());
                    intent.putExtra("currentVillages", villagesMap.get(holder.getAdapterPosition()));
                    Log.d("ADAPTER", "onClick: " + mDistrictId);
                    context.startActivity(intent);
               // }
            }
        });
        return holder;

         */
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        /*
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {


            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

         */
        holder.ado_name.setBackground(null);
        holder.ado_phone.setBackground(null);
        holder.ado_name.setText(ado_namelist.get(position));
        holder.ado_phone.setText(adoPhone.get(position));
        //}
    }

    @Override
    public int getItemCount() {
        //return showShimmer ? shimmerCount : ado_namelist.size();
        return ado_namelist.size();
    }

    public void sendDistrictId(String districtId) {
        mDistrictId = districtId;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ado_name;
        TextView ado_phone;
        //ShimmerFrameLayout shimmerFrameLayout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ado_name = itemView.findViewById(R.id.textViewAdo);
            ado_phone = itemView.findViewById(R.id.ado_phone);
            //shimmerFrameLayout = itemView.findViewById(R.id.ado_list_shimmer);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context,"You clicked",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context,adounderddointent.class);
            intent.putExtra("districtId", mDistrictId);
            intent.putExtra("adoId", mAdoIds.get(this.getAdapterPosition()));
            intent.putExtra("currentVillages", villagesMap.get(this.getAdapterPosition()));
            context.startActivity(intent);

            /*
            Intent intent = new Intent(context, villagenameActivity.class);
            intent.putExtra("districtId", mDistrictId);
            intent.putExtra("adoId", mAdoIds.get(this.getAdapterPosition()));
            //Log.d("ADOUNDERDDA", "onClick: " + villagesMap.get(this.getAdapterPosition()) + " dfsdfsdf" + mAdoIds.size());
            intent.putExtra("currentVillages", villagesMap.get(this.getAdapterPosition()));
            //Log.d("ADAPTER", "onClick: " + mDistrictId);
            context.startActivity(intent);

             */
            /*
            Bundle bundle = new Bundle();
            bundle.putString("districtId", mDistrictId);
            bundle.putString("adoId", mAdoIds.get(this.getAdapterPosition()));
            bundle.putIntegerArrayList("currentVillages", villagesMap.get(this.getAdapterPosition()));
            //checkListMainFragment abc= new checkListMainFragment();
            villageNameFragment abc= new villageNameFragment();
            abc.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_dda,abc).addToBackStack(null).commit();

             */




            //Toast.makeText(context,mDistrictId + " and "+ mAdoIds.get(this.getAdapterPosition()) + " and "+villagesMap.get(this.getAdapterPosition()),Toast.LENGTH_LONG).show();
        }
    }
}









//package com.theagriculture.app.Dda;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.facebook.shimmer.ShimmerFrameLayout;
//import com.theagriculture.app.R;
//
//import java.util.ArrayList;
//
//public class adounderddoadapter extends RecyclerView.Adapter<adounderddoadapter.Holder> {
//
//    Context context;
//    ArrayList<String> ado_namelist;
//    public boolean showShimmer = true;
//    private ArrayList<String> mAdoIds;
//    private String mDistrictId;
//    private ArrayList<ArrayList<Integer>> villagesMap;
//    private ArrayList<String> adoPhone;
//    private int shimmerCount = 7;
//
//    public adounderddoadapter(Context context, ArrayList<String> ado_name, ArrayList<String> mAdoIds,
//                              ArrayList<ArrayList<Integer>> villagesMap, ArrayList<String> adoPhone) {
//        this.context = context;
//        this.ado_namelist = ado_name;
//        this.mAdoIds = mAdoIds;
//        this.villagesMap = villagesMap;
//        this.adoPhone = adoPhone;
//    }
//
//
//    @NonNull
//    @Override
//    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.adounderddo,parent,false);
//        final Holder holder = new Holder(view);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!showShimmer) {
//                    Intent intent = new Intent(context, villagenameActivity.class);
//                    intent.putExtra("districtId", mDistrictId);
//                    intent.putExtra("adoId", mAdoIds.get(holder.getAdapterPosition()));
//                    Log.d("ADOUNDERDDA", "onClick: " + villagesMap.get(holder.getAdapterPosition()) + " dfsdfsdf" + mAdoIds.size());
//                    intent.putExtra("currentVillages", villagesMap.get(holder.getAdapterPosition()));
//                    Log.d("ADAPTER", "onClick: " + mDistrictId);
//                    context.startActivity(intent);
//                }
//            }
//        });
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Holder holder, int position) {
//        if (showShimmer) {
//            holder.shimmerFrameLayout.startShimmer();
//        } else {
//            holder.shimmerFrameLayout.stopShimmer();
//            holder.shimmerFrameLayout.setShimmer(null);
//            holder.ado_name.setBackground(null);
//            holder.ado_phone.setBackground(null);
//            holder.ado_name.setText(ado_namelist.get(position));
//            holder.ado_phone.setText(adoPhone.get(position));
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return showShimmer ? shimmerCount : ado_namelist.size();
//    }
//
//    public void sendDistrictId(String districtId) {
//        mDistrictId = districtId;
//    }
//
//    public class Holder extends RecyclerView.ViewHolder{
//
//        TextView ado_name;
//        TextView ado_phone;
//        ShimmerFrameLayout shimmerFrameLayout;
//        public Holder(@NonNull View itemView) {
//            super(itemView);
//            ado_name = itemView.findViewById(R.id.textViewAdo);
//            ado_phone = itemView.findViewById(R.id.ado_phone);
//            shimmerFrameLayout = itemView.findViewById(R.id.ado_list_shimmer);
//        }
//    }
//}
