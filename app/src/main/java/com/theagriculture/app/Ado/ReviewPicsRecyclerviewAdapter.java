package com.theagriculture.app.Ado;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.theagriculture.app.R;

import java.util.ArrayList;


public class ReviewPicsRecyclerviewAdapter extends RecyclerView.Adapter<ReviewPicsRecyclerviewAdapter.customViewHolder> {
    private Context mContext;
    private ArrayList<String> imagesUrl;

    public ReviewPicsRecyclerviewAdapter(Context mContext, ArrayList<String> imagesUrl) {
        this.mContext = mContext;
        this.imagesUrl = imagesUrl;
    }

    @NonNull
    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ado_report_image_item, parent, false);
        return new customViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return imagesUrl.size();
    }

    @Override
    public void onBindViewHolder(@NonNull customViewHolder holder, int position) {
        Glide.with(mContext)
                .load(imagesUrl.get(imagesUrl.size() - position - 1))
                .into(holder.imageView);
    }

    public class customViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;

        public customViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.ado_report_image);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            //Toast.makeText(mContext,imagesUrl.get(position),Toast.LENGTH_LONG).show();
            /*
            Bundle bundle = new Bundle();
            bundle.putString("url", imagesUrl.get(position));
            BigImage abc = new BigImage();
            abc.setArguments(bundle);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();

             */
        }
    }
}
