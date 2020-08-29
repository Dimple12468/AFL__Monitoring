package com.theagriculture.app.Dda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.Admin.SectionAdapter;
import com.theagriculture.app.Ado.ItemAdapter_ado;
import com.theagriculture.app.Ado.SectionAdapter_ado;
import com.theagriculture.app.Ado.Section_ado;
import com.theagriculture.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SectionAdapter_DDA extends RecyclerView.Adapter<SectionAdapter_DDA.ViewHolder> {

    public ArrayList<Section_DDA> sectionList;
    private Context context;
    private ItemAdapter_DDA itemAdapter;

    public SectionAdapter_DDA(Context context, ArrayList<Section_DDA> sections) {
        sectionList = sections;
        this.context = context;
    }


    @NonNull
    @Override
    public SectionAdapter_DDA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_section_dda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter_DDA.ViewHolder holder, int position) {
        Section_DDA section = sectionList.get(position);
        holder.bind(section);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView itemRecyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_item_text_view_dda);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view_dda);
        }

        public void bind(Section_DDA section) {
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
            itemAdapter = new ItemAdapter_DDA(context,section.getDid(), section.getDlocation_name(), section.getDlocation_address(), section.getDlatitude(), section.getDlongitude(),section.getIsPending(),section.getIsCompleted(),section.getIsOngoing());
            itemRecyclerView.setAdapter(itemAdapter);

        }
    }
}


