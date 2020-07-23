package com.theagriculture.app.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.theagriculture.app.Admin.PendingDetailsFragment;
import com.theagriculture.app.Admin.ado_fragment;
import com.theagriculture.app.Admin.detailsActivity;
import com.theagriculture.app.Admin.map_fragemnt;
import com.theagriculture.app.Admin.ongoingDetailsFragment;
import com.theagriculture.app.Ado.ReviewReport;
import com.theagriculture.app.R;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder>{
    String cv;
    char[] ab;
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    private ArrayList<String> mIds;
    private ArrayList<String> mpkado;
    private ArrayList<String> mpkdda;
    private ArrayList<String> mdate;
    private boolean isComplete = false;
    private boolean isOngoing = false;
    private boolean isPending = false;
    public boolean mShowShimmer = true;
    private int SHIMMER_ITEM_NO = 5;
    Context mcontext;
    private String TAG= "adminLocationAdapter";



   /* private ArrayList<String> set_date(ArrayList<String> mdate) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd MM,yyyy");
        Date d = simpleDate.parse(mdate);

    }
   private ArrayList<String> set_date(ArrayList<String> mdate){
       SimpleDateFormat simpleDate = new SimpleDateFormat("dd MM,yyyy");
       Date d = simpleDate.parse();

   }*/

    /*public View getView(int position, View convertView, ViewGroup parent) {
        TextView first_letter = convertView.findViewById(R.id.address);
        TextView image_letter = convertView.findViewById(R.id.my_letter);
        convertView = LayoutInflater.from(context).inflate(R.id.my_letter, parent, false);
        serialNum = convertView.findViewById(R.id.serailNumber);
        name = convertView.findViewById(R.id.studentName);
        contactNum = convertView.findViewById(R.id.mobileNum);
        serialNum.setText(" " + arrayList.get(position).getNum());
        name.setText(arrayList.get(position).getName());
        contactNum.setText(arrayList.get(position).getMobileNumber());
        return convertView;
        image_letter.setText(String.valueOf(first_letter.getText().toString().charAt(0)));
        return image_letter;

    }*/

    public PendingAdapter(Context mcontext,ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3, ArrayList<String> mIds, boolean isOngoing,ArrayList<String> mdate) {
        this.mcontext = mcontext;
        //this.mtextview_letter = mtextview_letter;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.isOngoing = isOngoing;
        this.mIds = mIds;
        this.mdate = mdate;
        Log.d(TAG, "AdminLocationAdapter: "+this.mdate.size());
    }

    public PendingAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3, boolean isComplete, ArrayList<String> mIds,ArrayList<String> mdate) {
        this.mcontext = mcontext;
        // this.mtextview_letter = mtextview_letter;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.mIds = mIds;
        this.mdate = mdate;
        this.isComplete = isComplete;
        Log.d(TAG, "AdminLocationAdapter: "+this.mdate.size());
    }

    public  PendingAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, boolean isPending, ArrayList<String> mtextview3, ArrayList<String> mIds,ArrayList<String> ado_pk, ArrayList<String> dda_pk,ArrayList<String> mdate) {
        this.mcontext = mcontext;
        // this.mtextview_letter = mtextview_letter;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.isPending = isPending;
        this.mdate = mdate;
        mpkado = ado_pk;
        mpkdda = dda_pk;
        this.mIds = mIds;
        Log.d(TAG, "AdminLocationAdapter: "+this.mdate.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(mcontext).inflate(R.layout.adminlocationlist,parent,false);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.pending_adapter,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*
        if (mShowShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            */
        //holder.tv_letter.setBackground(null);
        holder.tv1.setBackground(null);
        holder.tv2.setBackground(null);
        holder.tv3.setBackground(null);
        holder.tv4.setBackground(null);
        String fromDateFormat = "yyyy-MM-dd";
        String fromdate = mdate.get(position);
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
            /*
            cv= mtextview3.get(position);
            ab = cv.toCharArray();
            String x = String.valueOf(ab[0]);*/
        String x= String.valueOf(mtextview3.get(position).charAt(0));
        holder.tv_letter.setText(x);
        holder.tv1.setText("DDA:     " + mtextview1.get(position).toUpperCase());
        holder.tv2.setText("ADO:     " + mtextview2.get(position).toUpperCase());
        holder.tv3.setText(mtextview3.get(position));
        holder.tv4.setText(xy);

        // }
    }

    @Override
    public int getItemCount() {
        //return mShowShimmer ? SHIMMER_ITEM_NO : mtextview1.size();
        return  mtextview1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_letter;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        RelativeLayout parentnotassigned;
        //ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            parentnotassigned = itemView.findViewById(R.id.adminlocation);
            tv_letter = itemView.findViewById(R.id.my_letter);
            tv1 = itemView.findViewById(R.id.dda_name);
            tv2 = itemView.findViewById(R.id.ada_name);
            tv3 = itemView.findViewById(R.id.address);
            tv4 = itemView.findViewById(R.id.date);
            //shimmerFrameLayout = itemView.findViewById(R.id.locations_shimmer);
            //tv1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            //Log.d("click","click" + position);
            //Toast.makeText(mcontext,"The position is " + position,Toast.LENGTH_LONG).show();

            if(isPending){
                /*

                Intent intent1 = new Intent(mcontext,detailsActivity.class);
                intent1.putExtra("ado_name",mtextview1.get(position));
                intent1.putExtra("dda_name",mtextview2.get(position));
                intent1.putExtra("address_big",mtextview3.get(position));
                intent1.putExtra("ado_pk",mpkado.get(position));
                intent1.putExtra("dda_pk",mpkdda.get(position));
                mcontext.startActivity(intent1);

                 */
                Bundle bundle = new Bundle();
               PendingDetailsFragment abc = new PendingDetailsFragment();

                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).commit();

            }

            if(isOngoing) {
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
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, abc).commit();
            }
            if(isComplete){
                Intent intent2  = new Intent(mcontext,ReviewReport.class);
                intent2.putExtra("id", mIds.get(position));
                intent2.putExtra("isDdo", true);
                intent2.putExtra("isAdmin", true);
                intent2.putExtra("review_address_top",mtextview3.get(position));
                intent2.putExtra("isComplete", true);
                mcontext.startActivity(intent2);

            }
            /*

            if (isOngoing || isComplete){
                Intent intent2  = new Intent(mcontext,ReviewReport.class);
                intent2.putExtra("id", mIds.get(position));
                intent2.putExtra("isDdo", true);
                intent2.putExtra("isAdmin", true);
                intent2.putExtra("review_address_top",mtextview3.get(position));

                if (isComplete) {
                    intent2.putExtra("isComplete", true);
                }
                mcontext.startActivity(intent2);
            }

             */





        }
    }

}


    /*public void setDate (TextView view){

        //Date today = Calendar.getInstance().getTime();//getting date
        //SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");//formating according to my need
        //String date = formatter.format(today);
        //view.setText(date);

        //String date = '15/2/2014'
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd MM,yyyy");
        Date d = simpleDate.parse(tv4);
    }*/
