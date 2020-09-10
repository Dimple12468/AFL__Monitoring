package com.theagriculture.app.Dda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.Admin.RecyclerViewAdapter_district;
import com.theagriculture.app.AlternateColorAdapter;
import com.theagriculture.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DdaAdoListAdapter extends RecyclerView.Adapter<DdaAdoListAdapter.AdoListViewHolder> {
    private static final String TAG = "DdaAdoListAdapter";

    private ArrayList<String> mtextview1;
    private Map<Integer, ArrayList<String>> mtextview2;
    private Context mcontext;
    private String locationid;
    private ArrayList<String> adoid;
    private String urlpatch;
    private String token;
    private int currentAdoPos = -1;

    public DdaAdoListAdapter(Context mcontext, ArrayList<String> mtextview1, Map<Integer, ArrayList<String>> mtextview2) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        adoid = new ArrayList<>();
    }

    @NonNull
    @Override
    public AdoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mcontext).inflate(R.layout.ddaselectadolist,parent,false);
        final AdoListViewHolder adoListViewHolder = new AdoListViewHolder(view);
        Log.d(TAG, "onCreateViewHolder:");

        /*

        adoListViewHolder.btnassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogbox("", "Press OK for confirmation", "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences = mcontext.getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
                        token = preferences.getString("token", "");
                        Log.d(TAG, "onClick: TOKEN"+token);
                        try {
                            final JSONObject postbody = new JSONObject();
                            postbody.put("ado", adoid.get(adoListViewHolder.getAdapterPosition()));
                            final RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
                            //urlpatch = "http://18.224.202.135/api/location/" + locationid + "/";
                            urlpatch = "http://api.theagriculture.tk/api/location/" + locationid + "/";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, urlpatch, postbody, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(mcontext,"Location assigned",Toast.LENGTH_SHORT).show();
                                    ((Activity)mcontext).finish();
                                    try {
                                        JSONObject c = new JSONObject(String.valueOf(response));
                                        Log.d(TAG, "onResponse: " + c);
                                    }catch (JSONException e){
                                        Log.d(TAG, "onResponse: "+e);
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "onErrorResponse: "+error);
                                    Toast.makeText(mcontext,"Ado not assigned.Please try again",Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    HashMap<String, String> headers = new HashMap<>();
                                    headers.put("Authorization", "Token " + token);
                                    return headers;
                                }
                            };
                            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                                @Override
                                public int getCurrentTimeout() {
                                    return 50000;
                                }

                                @Override
                                public int getCurrentRetryCount() {
                                    return 50000;
                                }

                                @Override
                                public void retry(VolleyError error) throws VolleyError {

                                }
                            });
                            requestQueue.add(jsonObjectRequest);
                        }catch (JSONException e){
                            Log.d(TAG, "onClick: "+e);
                            e.printStackTrace();
                        }
                    }
                }, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                },false);
            }
        });

        adoListViewHolder.adolistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, VillagesUnderAdo.class);
                int pos = adoListViewHolder.getAdapterPosition();
                intent.putExtra("listArray", mtextview2.get(pos));
                intent.putExtra("adoName", mtextview1.get(pos));
                mcontext.startActivity(intent);
            }
        });

         */
        return adoListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdoListViewHolder holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        if (currentAdoPos == position) {
            holder.tv1.setTextColor(mcontext.getResources().getColor(R.color.app_color));
            /*
            holder.btnassign.setText("Assigned");
            holder.btnassign.setEnabled(false);
            holder.btnassign.setAlpha(0.7f);

             */
        }
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class AdoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv1;
        RelativeLayout adolistlayout;
        //LinearLayout adolistlayout;
        //////Button btnassign;

        public AdoListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mcontext = itemView.getContext();
            adolistlayout = (RelativeLayout)itemView.findViewById(R.id.adolistlayout);
            tv1 = itemView.findViewById(R.id.nameofado);
            //btnassign = itemView.findViewById(R.id.btnAssign);
        }

        /*
        @Override
        public void onItemClick(View v, int pos) {

        }

         */

        @Override
        public void onClick(View v) {
            //Toast.makeText(mcontext,"You clicked",Toast.LENGTH_LONG).show();
            DetailsDialog detailsDialog;
            int pos = this.getAdapterPosition();
            ArrayList<String> items =  mtextview2.get(pos);
            // Toast.makeText(mcontext,adoid.toString(),Toast.LENGTH_LONG).show();
            //RecyclerViewAdapter_district dataAdapter = new RecyclerViewAdapter_district(mcontext, items);
            AlternateColorAdapter dataAdapter = new AlternateColorAdapter(mcontext, items);
            detailsDialog = new DetailsDialog(mcontext, dataAdapter,mtextview1.get(pos),locationid,adoid.get(pos));

            detailsDialog.show();
            detailsDialog.setCanceledOnTouchOutside(false);
        }
    }


    //made function to show dialogbox
    private AlertDialog showdialogbox(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                      String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
                                      boolean isCancelable){

        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        Log.d(TAG, "showdialogbox: "+alert);
        alert.show();
        return alert;
    }

    public void getlocationid(String lid){
        this.locationid = lid;
    }

    public void getadoid(String adoid){
        this.adoid.add(adoid);
    }

    public void getCurrentAdo(int pos) {
        currentAdoPos = pos;
    }
}
