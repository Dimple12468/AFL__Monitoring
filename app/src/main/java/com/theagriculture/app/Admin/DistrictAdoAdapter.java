package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.theagriculture.app.Admin.AdoDdoActivity.AdoDdoActivity;
import com.theagriculture.app.Admin.AdoDdoActivity.AdoDdoActivityFragment;
import com.theagriculture.app.Ado.ReviewTableRecycleAdapter;
import com.theagriculture.app.R;
import com.theagriculture.app.adapter.PendingAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class DistrictAdoAdapter extends RecyclerView.Adapter<DistrictAdoAdapter.ViewHolder> implements Filterable {

    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;

    ArrayList<String> mtextview1_all;
    public boolean mShowShimmer = true;
    Context mcontext;
    private boolean isDdoFragment;
    ArrayList<String> mUserId;
    private ArrayList<String> mPkList;
    private ArrayList<String> mDdoNames;
    private ArrayList<String> mDistrictNames;
    private boolean isBusy = false;
    private String TAG = "RecyclerViewAdapter";
    private ImageButton imageView6;

    public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                              ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> mPkList, ArrayList<String> mDdoNames, ArrayList<String> mDistrictNames) {
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mcontext = mcontext;
        this.isDdoFragment = isDdoFragment;
        this.mUserId = mUserId;
        this.mPkList = mPkList;
        this.mDdoNames = mDdoNames;
        this.mDistrictNames = mDistrictNames;

        System.out.println("dimple in constructor 1");
       // this.mtextview1_all = new ArrayList<>(mtextview1);
     //   this.mtextview1_all = new ArrayList<>(mDdoNames);
    }

    public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                              ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> pkList) {
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mUserId = mUserId;
        this.mcontext = mcontext;
        this.isDdoFragment = isDdoFragment;
        mPkList = pkList;

        System.out.println("dimple in constructor 2");
        //this.mtextview1_all = new ArrayList<>(mtextview1);
        this.mtextview1_all = new ArrayList<>(mtextview1);
        System.out.println("during assigning this is we have" + mtextview1_all);
    }

    @NonNull
    @Override
    public DistrictAdoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mcontext).inflate(R.layout.district_ado_adapter,parent,false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictAdoAdapter.ViewHolder holder, int position) {
        holder.tv1.setBackground(null);
        holder.tv2.setBackground(null);
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));
        if (!isDdoFragment) {
            holder.districtTextview.setText("DDA : " + mDdoNames.get(position).toUpperCase() + " (" + mDistrictNames.get(position) + ")");
            holder.districtTextview.setBackground(null);
        } else
            holder.districtTextview.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return  mtextview1.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filtered_list_ado = new ArrayList<>();
            if (isDdoFragment) {
                if (constraint.toString().isEmpty()) {
                    filtered_list_ado.addAll(mtextview1_all);
                } else {
                    for (String address_ddo : mtextview1_all) {
                        if (address_ddo.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filtered_list_ado.add(address_ddo);
                        }
                    }
                }
            }
            System.out.println("dimple all: "+ mtextview1_all);
            System.out.println("dimple not all: "+ mtextview1);
            System.out.println("dimple ddo names: "+ mDdoNames);

            FilterResults filterResults_ddo = new FilterResults();
            filterResults_ddo.count = filtered_list_ado.size();
            filterResults_ddo.values = filtered_list_ado;

            return filterResults_ddo;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            //todo ddo fragment
            if (isDdoFragment) {
                mtextview1.clear();
                mtextview1.addAll((Collection<? extends String>) results.values);
                notifyDataSetChanged();
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv1;
        TextView tv2;
        RelativeLayout relativeLayout;
        TextView districtTextview;
        ImageButton imageView6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv1= itemView.findViewById(R.id.tvuser);
            tv2= itemView.findViewById(R.id.tvinfo);
            relativeLayout = itemView.findViewById(R.id.relativeLayout2);
            districtTextview = itemView.findViewById(R.id.district_info);
            imageView6 = itemView.findViewById(R.id.dropdown);

            imageView6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mcontext,"you clicked",Toast.LENGTH_LONG).show();
                    PopupMenu popup = new PopupMenu(mcontext, imageView6);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.edit_delete, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(mcontext,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    popup.show();

                }
            });

        }
        @Override
        public void onClick(View v) {
            //Toast.makeText(mcontext,"you clicked an ado",Toast.LENGTH_LONG).show();
            //Toast.makeText(mcontext,"Sending user id "+ mUserId.get(this.getAdapterPosition()),Toast.LENGTH_LONG).show();
            /*
            Intent intent = new Intent(mcontext, AdoDdoActivity.class);
            intent.putExtra("Id", mUserId.get(this.getAdapterPosition()));
            if (isDdoFragment)
                intent.putExtra("isDdo", true);
            else
                intent.putExtra("isDdo", false);
            intent.putExtra("name", mtextview1.get(this.getAdapterPosition()));
            mcontext.startActivity(intent);

             */
            Bundle bundle = new Bundle();
            bundle.putString("Id", mUserId.get(this.getAdapterPosition()));
            if(isDdoFragment)
                bundle.putBoolean("isDdo",true);
            else
                bundle.putBoolean("isDdo",false);
            bundle.putString("name", mtextview1.get(this.getAdapterPosition()));
            AdoDdoActivityFragment abc = new AdoDdoActivityFragment();
            abc.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();


        }
    }

}
