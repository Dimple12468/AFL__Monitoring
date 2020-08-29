package com.theagriculture.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theagriculture.app.Dda.checkListItemClickListener;
import com.theagriculture.app.Dda.checkListVillage;

import java.util.ArrayList;

public class villageNameFragmentAdapter extends RecyclerView.Adapter<villageNameFragmentAdapter.ViewHolder>{
    Context context;
    ArrayList<String> villageName;
    ArrayList<Integer> villageId;
    public ArrayList<String> checkedVillageName= new ArrayList<>();
    public ArrayList<Integer> checkedVillageId= new ArrayList<>();


    public villageNameFragmentAdapter(Context context,ArrayList<String> villageName, ArrayList<Integer> villageId){
        this.context = context;
        this.villageName=villageName;
        this.villageId = villageId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View view = LayoutInflater.from(context).inflate(R.layout.village_name_fragment_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.villageTxt.setText(villageName.get(position));
        if(position%2==0)
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.recycler_color));
        else
            holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));

        holder.setCheckListItemClickListener(new checkListItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox chk = (CheckBox)v;
                if(chk.isChecked()){
                    checkedVillageName.add(villageName.get(pos));
                    checkedVillageId.add(villageId.get(pos));
                }
                else if(!chk.isChecked()){
                    checkedVillageName.remove(villageName.get(pos));
                    checkedVillageId.remove(villageId.get(pos));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        //return 0;
        return villageName.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView villageTxt;
        CheckBox chk;
        LinearLayout layout;
        checkListItemClickListener checkListItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            layout = itemView.findViewById(R.id.villagename);
            villageTxt = itemView.findViewById(R.id.villageTxt_name);
            chk = itemView.findViewById(R.id.chk_name);
            chk.setOnClickListener(this);
        }

        public void setCheckListItemClickListener(checkListItemClickListener ic){
            this.checkListItemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.checkListItemClickListener.onItemClick(v,getLayoutPosition());
        }

    }
}