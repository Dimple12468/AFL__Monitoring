package com.theagriculture.app.Dda;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Admin.CompleteDetailsFragment;
import com.theagriculture.app.Admin.complete_details;
import com.theagriculture.app.Admin.ongoingDetailsFragment;
import com.theagriculture.app.Ado.ItemAdapter_ado;
import com.theagriculture.app.Ado.PendingReport;
import com.theagriculture.app.Ado.ReviewReport;
import com.theagriculture.app.R;

import java.util.ArrayList;

public class ItemAdapter_DDA extends RecyclerView.Adapter<ItemAdapter_DDA.ViewHolder> {

    View view;
    Context context;
    ArrayList<String> did;
    ArrayList<String> dlocation_name;
    ArrayList<String> dlocation_address;
    private ArrayList<String> dlatitude;
    private ArrayList<String> dlongitude;
    private ArrayList<String> dAdoID;
    Boolean isPending,isCompleted,isOngoing;
//    final ItemAdapter_DDA.ViewHolder viewHolderAssignedDda;

    public ItemAdapter_DDA(Context context, ArrayList<String> did, ArrayList<String> dlocation_name, ArrayList<String> dlocation_address, ArrayList<String> dlatitude, ArrayList<String> dlongitude,/*ArrayList<String> dAdoID,*/ Boolean isPending, Boolean isCompleted, Boolean isOngoing) {
        this.context=context;
        this.did=did;
        this.dlocation_name=dlocation_name;
        this.dlocation_address=dlocation_address;
        this.dlatitude=dlatitude;
        this.dlongitude=dlongitude;
//        this.dAdoID=dAdoID;
        this.isPending=isPending;
        this.isCompleted=isCompleted;
        this.isOngoing=isOngoing;
    }

    public ItemAdapter_DDA(Context context, ArrayList<String> did, ArrayList<String> dlocation_name, ArrayList<String> dlocation_address, ArrayList<String> dlatitude, ArrayList<String> dlongitude,ArrayList<String> dAdoID, Boolean isPending, Boolean isCompleted, Boolean isOngoing) {
        this.context=context;
        this.did=did;
        this.dlocation_name=dlocation_name;
        this.dlocation_address=dlocation_address;
        this.dlatitude=dlatitude;
        this.dlongitude=dlongitude;
        this.dAdoID=dAdoID;
        this.isPending=isPending;
        this.isCompleted=isCompleted;
        this.isOngoing=isOngoing;
    }

    @NonNull
    @Override
    public ItemAdapter_DDA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dda, parent, false);
//        viewHolderAssignedDda = new ItemAdapter_DDA.ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter_DDA.ViewHolder holder, int position) {
        String tv1 = dlocation_name.get(position);
        String tv2 = dlocation_address.get(position);
        String x= String.valueOf(dlocation_name.get(position).charAt(0));
        holder.bind(tv1,tv2,x);
    }

    @Override
    public int getItemCount() {
        return did.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_letter;
        TextView tv1;
        TextView tv2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_letter = itemView.findViewById(R.id.my_letter_dda);
            tv1 = itemView.findViewById(R.id.address_dda);
            tv2 = itemView.findViewById(R.id.address_full_dda);
        }

        public void bind(String tv1g,String tv2g,String letter){
            tv1.setText(tv1g.toUpperCase());
            tv2.setText(tv2g.toUpperCase());
            tv_letter.setText(letter);
        }

        @Override
        public void onClick(View v) {
            if(isPending){
                final int position = this.getAdapterPosition();

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.assign_alert_dda, null);

                Button bt1 = promptsView.findViewById(R.id.yes_assign);
                Button bt2 = promptsView.findViewById(R.id.no_assign);

                final AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.AlertDialog)
                        .setView(promptsView)
                        .create();

                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("Id_I_Need", did.get(position));
//                        bundle.putString("adoId", dAdoID.get(position));
//                        Intent intent = new Intent(context, DdaselectAdo.class);
//                        intent.putExtras(bundle);
//                        DdaselectAdo abc = new DdaselectAdo();

                        Intent intent = new Intent(context, DdaselectAdo.class);
                        intent.putExtra("Id_I_Need", did.get(position));
                        intent.putExtra("adoId", dAdoID.get(position));
                        System.out.println("did: " + did  + "dAdoID: " + dAdoID);
                        context.startActivity(intent);
                        alertDialog.dismiss();
                    }
                });

                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

            }

            if(isOngoing){
                int position = this.getAdapterPosition();
                Intent intent = new Intent(context, complete_details.class);
                intent.putExtra("id", did.get(position));
                intent.putExtra("review_address_top",dlocation_name.get(position)+", "+dlocation_address.get(position));
                intent.putExtra("isDdo", true);
                intent.putExtra("isAdmin", false);
                intent.putExtra("isComplete", false);
                intent.putExtra("isOngoing",true);
                intent.putExtra("isDDA", true);
                context.startActivity(intent);

//                Bundle bundle = new Bundle();
//                bundle.putString("id", did.get(position));
//                bundle.putString("review_address_top",dlocation_name.get(position)+", "+dlocation_address.get(position));
//                bundle.putBoolean("isDdo", true);
//                bundle.putBoolean("isAdmin", false);
//                bundle.putBoolean("isComplete", false);
//                bundle.putBoolean("isOngoing",true);
//                bundle.putBoolean("isDDA", true);

//                CompleteDetailsFragment abc = new CompleteDetailsFragment();
//                abc.setArguments(bundle);
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                activity.getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_dda,abc).addToBackStack(null).commit();
            }

            if(isCompleted){
                int position = this.getAdapterPosition();
                Intent intent = new Intent(context,complete_details.class);
                intent.putExtra("id", did.get(position));
                intent.putExtra("review_address_top",dlocation_name.get(position)+", "+dlocation_address.get(position));
                intent.putExtra("isDdo", true);
                intent.putExtra("isAdmin", false);
                intent.putExtra("isComplete", true);
                intent.putExtra("isDDA", true);
                intent.putExtra("isOngoing",false);
                context.startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putString("id", did.get(position));
//                bundle.putString("review_address_top",dlocation_name.get(position)+", "+dlocation_address.get(position));
//                bundle.putBoolean("isDdo", true);
//                bundle.putBoolean("isAdmin", false);
//                bundle.putBoolean("isComplete", true);
//                bundle.putBoolean("isDDA", true);
//                bundle.putBoolean("isOngoing",false);

//                CompleteDetailsFragment abc = new CompleteDetailsFragment();
//                abc.setArguments(bundle);
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                activity.getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_dda,abc).addToBackStack(null).commit();
            }
        }
    }
}
