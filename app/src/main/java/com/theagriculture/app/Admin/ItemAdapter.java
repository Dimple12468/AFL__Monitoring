package com.theagriculture.app.Admin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> /*implements Filterable*/ {
    /*
    List<String> items;
    public ItemAdapter(List<String> items) {
        this.items = items;

        }

     */
    String cv;
    char[] ab;
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    ArrayList<String> mtextview3_address;
    private ArrayList<String> mIds;
    private ArrayList<String> mpkado;
    private ArrayList<String> mpkdda;
    private boolean isComplete = false;
    private boolean isOngoing = false;
    private boolean isPending = false;
//    private boolean is_DDA_user = false;
    Context mcontext;
//    private String sectionTitle_dda,villagename_dda,blockname_dda,district_dda,state_dda;


    //itemAdapter = new ItemAdapter(context, section.getDda(), section.getAda(), section.getAddress(), section.getId(), section.getAdapk(), section.getDdapk(), section.getPendingstatus(), section.getOngoingstatus(), section.getCompletedstatus());
    public  ItemAdapter(Context mcontext,ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3, ArrayList<String> mIds,ArrayList<String> ado_pk, ArrayList<String> dda_pk,boolean isPending,boolean isOngoing,boolean isComplete) {
        this.mcontext = mcontext;
        // this.mtextview_letter = mtextview_letter;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.isPending = isPending;
        this.isOngoing = isOngoing;
        this.isComplete = isComplete;
        mpkado = ado_pk;
        mpkdda = dda_pk;
        this.mIds = mIds;

        this.mtextview3_address = new ArrayList<>(mtextview3);
    }

    //itemAdapter_DDA = new ItemAdapter(context,section.getSectionTitle_DDA(),section.getVillagename_DDA(),section.getBlockname_DDA(),section.getDistrict_DDA(),section.getState_DDA(),
    //                    section.getId_DDA(),section.getAddress_DDA(),section.getName_DDA(),section.getmAdoIds_DDA(),is_DDA_User);
    /*public ItemAdapter(Context context, String sectionTitle_dda, String villagename_dda, String blockname_dda, String district_dda, String state_dda, ArrayList<String> id_dda, ArrayList<String> address_dda, ArrayList<String> name_dda, ArrayList<String> getmAdoIds_dda, Boolean is_dda_user) {
        System.out.println("Dimple in item adapter constructor for dda");
        this.mcontext = context;
        this.mtextview1 = name_dda;
        this.mtextview2 = name_dda;
        this.mtextview3 = address_dda;
        this.is_DDA_user = is_dda_user;

        this.mIds = id_dda;
        this.mpkado = getmAdoIds_dda;

        this.sectionTitle_dda=sectionTitle_dda;
        this.villagename_dda=villagename_dda;
        this.blockname_dda=blockname_dda;
        this.district_dda=district_dda;
        this.state_dda=state_dda;

    }*/


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*
        holder.tv1.setBackground(null);
        holder.tv2.setBackground(null);
        holder.tv3.setBackground(null);
        String x= String.valueOf(mtextview3.get(position).charAt(0));
        holder.tv_letter.setText(x);
        holder.tv1.setText("DDA:     " + mtextview1.get(position).toUpperCase());
        holder.tv2.setText("ADO:     " + mtextview2.get(position).toUpperCase());
        holder.tv3.setText(mtextview3.get(position));
        */
        //String itemName = items.get(position);
        String tv1 = mtextview1.get(position);
        String tv2 = mtextview2.get(position);
        String tv3 = mtextview3.get(position);
        String x= String.valueOf(mtextview3.get(position).charAt(0));
        holder.bind(tv1,tv2,tv3,x);

    }
    @Override
    public int getItemCount() {
        //return items.size();
        return  mtextview1.size();
    }

    /*@Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filtered_list = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filtered_list.addAll(mtextview3_address);
            } else {
                for (String list_address : mtextview3_address){
                     if (list_address.toLowerCase().contains(constraint.toString().toLowerCase())){
                         //todo here for search suggestions
                         filtered_list.add(list_address);
                     }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered_list;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            //todo
            mtextview3.clear();
            mtextview3.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private TextView itemName;
        TextView tv_letter;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        RelativeLayout parentnotassigned;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentnotassigned = itemView.findViewById(R.id.adminlocation);
            tv_letter = itemView.findViewById(R.id.my_letter);
            tv1 = itemView.findViewById(R.id.dda_name);
            tv2 = itemView.findViewById(R.id.ada_name);
            tv3 = itemView.findViewById(R.id.address);
            itemView.setOnClickListener(this);
            //itemName = itemView.findViewById(R.id.list_item_text_view);
        }
        public void bind(String tv1g,String tv2g, String tv3g,String letter){
            tv1.setText("DDA:     " + tv1g.toUpperCase());
            tv2.setText("ADO:     " + tv2g.toUpperCase());
            tv3.setText(tv3g);
            tv_letter.setText(letter);
//            if(is_DDA_user){
//                tv2.setVisibility(View.GONE);
//            }else{
//                tv2.setText("ADO:     " + tv2g.toUpperCase());
//            }

        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            //Toast.makeText(mcontext,"You clicked "+ mIds.get(position)+ "ddo_name"+mtextview1.get(position)+"ado_name"+mtextview2.get(position),Toast.LENGTH_LONG).show();
            //Toast.makeText(mcontext,"You clicked "+ mtextview3.get(position)+ "ado_pke"+mpkado.get(position)+"ddao_pke"+mpkdda.get(position),Toast.LENGTH_LONG).show();
            if(isPending) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mIds.get(position));
                bundle.putString("ado_name", mtextview1.get(position));
                bundle.putString("dda_name", mtextview2.get(position));
                bundle.putString("address_big", mtextview3.get(position));
                bundle.putString("ado_pk", mpkado.get(position));
                bundle.putString("dda_pk", mpkdda.get(position));
                bundle.putBoolean("isPending",true);
                bundle.putBoolean("isOngoing", false);
                PendingDetailsFragment abc = new PendingDetailsFragment();
                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, abc).addToBackStack(null).commit();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).commit();
            }
            if(isOngoing) {
                Bundle bundle = new Bundle();
                bundle.putString("id", mIds.get(position));
                bundle.putString("ado_name", mtextview1.get(position));
                bundle.putString("dda_name", mtextview2.get(position));
                bundle.putString("address_big", mtextview3.get(position));
                bundle.putString("ado_pk", mpkado.get(position));
                bundle.putString("dda_pk", mpkdda.get(position));
                bundle.putBoolean("isPending",false);
                bundle.putBoolean("isOngoing", true);
                PendingDetailsFragment abc = new PendingDetailsFragment();
                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, abc).addToBackStack(null).commit();
                /*
                Bundle bundle = new Bundle();

                bundle.putString("id", mIds.get(position));
                bundle.putString("review_address_top",mtextview3.get(position));
                bundle.putBoolean("isDdo", true);
                bundle.putBoolean("isAdmin", true);
                bundle.putBoolean("isComplete", true);

                ongoingDetailsFragment abc = new ongoingDetailsFragment();
                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();

                 */
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, abc).commit();
            }
            if(isComplete) {
                //Toast.makeText(mcontext,"sending "+ mIds.get(position)+" "+mtextview3.get(position),Toast.LENGTH_LONG).show();

                Bundle bundle = new Bundle();

                bundle.putString("id", mIds.get(position));
                bundle.putString("review_address_top",mtextview3.get(position));
               // bundle.putBoolean("isDdo", true);
               // bundle.putBoolean("isAdmin", true);
                //bundle.putBoolean("isComplete", true);

                CompleteDetailsFragment abc = new CompleteDetailsFragment();
                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();


                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, abc).commit();
            }
        }

/*
        public void bind(String item) {
            itemName.setText(item);
        }

 */
    }
}

