package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.R;
import com.theagriculture.app.RegistrationActivity;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerViewAdapter_district extends RecyclerView.Adapter<RecyclerViewAdapter_district.DistrictCustomViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<String> mDistrictNames;

    private ArrayList<String> mDistrictNames_all;

    public RecyclerViewAdapter_district(Context mContext, ArrayList<String> mDistrictNames) {
        this.mContext = mContext;
        this.mDistrictNames = mDistrictNames;

        this.mDistrictNames_all = new ArrayList<>(mDistrictNames);
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
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.recycler_color));
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString();

            ArrayList<String> filtered_list_ado = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filtered_list_ado.addAll(mDistrictNames_all);
            } else {
                for (String address_ado : mDistrictNames_all) {
                    if (address_ado.toLowerCase().contains(constraint.toString().toLowerCase().trim())){
                        filtered_list_ado.add(address_ado);
                    }//todo add no reults found
                }
            }
            FilterResults filterResults_ado = new FilterResults();
            filterResults_ado.count = filtered_list_ado.size();
            filterResults_ado.values = filtered_list_ado;

            return filterResults_ado;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mDistrictNames.clear();
            mDistrictNames.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();

        }

    };


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
