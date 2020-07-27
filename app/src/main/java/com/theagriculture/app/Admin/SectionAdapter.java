package com.theagriculture.app.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> /*implements Filterable*/ {
    public ArrayList<Section> sectionList;
    public ArrayList<Section> sectionListall;
    private Context context;
    private ItemAdapter itemAdapter;

    public SectionAdapter(Context context, ArrayList<Section> sections) {
        sectionList = sections;
        this.context = context;

        this.sectionListall = new ArrayList<>(sections);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Section section = sectionList.get(position);
        holder.bind(section);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    //todo

   /* @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

           ArrayList<Section> filtered_list = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filtered_list.addAll(sectionListall);
            } else {
                for (Section list_address : sectionListall){
                    // if (list_address.toLowerCase().contains(constraint.toString().toLowerCase())){
                         //todo here for search suggestions
                         filtered_list.add(list_address);
                     //}
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered_list;

            return filterResults;
        }

        //run on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {

            sectionList.clear();
            sectionList.addAll((Collection<? extends Section>) filterResults.values);
            notifyDataSetChanged();
        }
    };*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView itemRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_item_text_view);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        }

        public void bind(Section section) {
            String fromDateFormat = "yyyy-MM-dd";
            String fromdate = section.getSectionTitle();
            String CheckFormat = "dd MMM yyyy";
            String dateStringFrom;
            DateFormat FromDF = new SimpleDateFormat(fromDateFormat);
            FromDF.setLenient(false);  // this is important!
            Date FromDate = null;
            try {
                FromDate = FromDF.parse(fromdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateStringFrom = new SimpleDateFormat(CheckFormat).format(FromDate);
            String xy = dateStringFrom.toString();
            sectionName.setText(xy);
            //sectionName.setText(section.getSectionTitle());
            //Toast.makeText(context, "Section name set", Toast.LENGTH_LONG).show();
            // RecyclerView for items
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            itemRecyclerView.setLayoutManager(linearLayoutManager);
            itemAdapter = new ItemAdapter(context,section.getDda(), section.getAda(), section.getAddress(), section.getId(), section.getAdapk(), section.getDdapk(), section.getPendingstatus(),section.getOngoingstatus(),section.getCompletedstatus());
            itemRecyclerView.setAdapter(itemAdapter);
            //itemAdapter.getFilter();
        }

    }
}