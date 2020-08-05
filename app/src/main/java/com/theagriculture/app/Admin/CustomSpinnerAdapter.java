package com.theagriculture.app.Admin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.theagriculture.app.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] states;
    int[] images;
    Drawable[] draw;

    public CustomSpinnerAdapter(Context context, String[] states, int[] image, Drawable[] drawable)
    {
        super(context, R.layout.custom_spinner_stat,states);
        this.context = context;
        this.states = states;
        this.images = image;
        this.draw = drawable;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //if (convertView==null)
        //{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_spinner_stat,null);
            TextView tv = (TextView)view.findViewById(R.id.textview);
            ImageView Iv = (ImageView) view.findViewById(R.id.imageview);

            tv.setText(states[position]+" ");
            Iv.setImageResource(images[position]);
            Iv.setBackground(draw[position]);
        //}
        //return super.getDropDownView(position, convertView, parent);
        return view;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_spinner_stat,null);
        TextView tv = (TextView)view.findViewById(R.id.textview);
        ImageView Iv = (ImageView) view.findViewById(R.id.imageview);

        tv.setText(states[position]);
        Iv.setImageResource(images[position]);
        Iv.setBackground(draw[position]);
        return view;
    }
}
