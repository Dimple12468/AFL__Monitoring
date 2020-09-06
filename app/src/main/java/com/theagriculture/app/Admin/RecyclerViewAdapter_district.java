package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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
    public void onBindViewHolder(@NonNull final DistrictCustomViewHolder holder, final int position) {
        String x= String.valueOf(mDistrictNames.get(position).charAt(0));

        /*if(position%2==0)
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.recycler_color));
        else
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));*/
        holder.textView.setText(mDistrictNames.get(position));
        holder.tv.setText(x);
        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DistrictAdo_Activity.class);
                intent.putExtra("district", mDistrictNames.get(position));
                mContext.startActivity(intent);
            }
        });
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
        TextView textView,tv;
        RelativeLayout itemLayout;
        ImageButton ib;
        public DistrictCustomViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);//function to make onClick() valid
            textView = itemView.findViewById(R.id.dist);
            itemLayout = itemView.findViewById(R.id.dist_item);
            tv = itemView.findViewById(R.id.in_admin_ado);
            ib = itemView.findViewById(R.id.ado_next);
//            final int pos = this.getAdapterPosition();
//            ib.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext,DistrictAdo_Activity.class);
//                    intent.putExtra("district", mDistrictNames.get(this.getAdapterPosition()));
//                    mContext.startActivity(intent);
//                }
//            });


        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,DistrictAdo_Activity.class);
            intent.putExtra("district", mDistrictNames.get(this.getAdapterPosition()));
            mContext.startActivity(intent);
//            Bundle bundle = new Bundle();
//            bundle.putString("district", mDistrictNames.get(this.getAdapterPosition()));
//            DistrictAdo abc = new DistrictAdo();
//            abc.setArguments(bundle);
//            AppCompatActivity activity = (AppCompatActivity) v.getContext();
//            activity.getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,abc).addToBackStack(null).commit();
        }

    }
}
