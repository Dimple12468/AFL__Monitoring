package com.theagriculture.app.Ado;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Admin.ongoingDetailsFragment;
import com.theagriculture.app.Admin.ongoing_details;
import com.theagriculture.app.Admin.pending_details;
import com.theagriculture.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ItemAdapter_ado extends RecyclerView.Adapter<ItemAdapter_ado.ViewHolder>{
    Context context;
    ArrayList<String> did;
    ArrayList<String> dlocation_name;
    ArrayList<String> dlocation_address;
    private ArrayList<String> dlatitude;
    private ArrayList<String> dlongitude;
    Boolean isPending, isCompleted;
    FusedLocationProviderClient fusedLocationProviderClient;
    final Location AdoCurrentLocation = new Location(" ");

    public ItemAdapter_ado(Context context, ArrayList<String> did, ArrayList<String> dlocation_name, ArrayList<String> dlocation_address, ArrayList<String> dlatitude, ArrayList<String> dlongitude, Boolean isPending, Boolean isCompleted) {
        this.context = context;
        this.did = did;
        this.dlocation_name = dlocation_name;
        this.dlocation_address = dlocation_address;
        this.dlatitude = dlatitude;
        this.dlongitude = dlongitude;
        this.isPending = isPending;
        this.isCompleted = isCompleted;
    }

    @NonNull
    @Override
    public ItemAdapter_ado.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ado, parent, false);
        return new ItemAdapter_ado.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter_ado.ViewHolder holder, int position) {
        String tv1 = dlocation_name.get(position);
        String tv2 = dlocation_address.get(position);
        String x = String.valueOf(dlocation_name.get(position).charAt(0));
        holder.bind(tv1, tv2, x);

    }

    @Override
    public int getItemCount() {
        return did.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_letter;
        TextView tv1;
        TextView tv2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_letter = itemView.findViewById(R.id.my_letter_ado);
            tv1 = itemView.findViewById(R.id.address_ado);
            tv2 = itemView.findViewById(R.id.address_full_ado);
        }

        public void bind(String tv1g, String tv2g, String letter) {
            tv1.setText(tv1g.toUpperCase());
            tv2.setText(tv2g.toUpperCase());
            tv_letter.setText(letter);
        }

        @Override
        public void onClick(View v) {
            if (isPending) {
                //if(!checkIfLocationEnabled()){
                // Toast.makeText(context, "Please enable location", Toast.LENGTH_SHORT).show();
                // }
                //else{
                Location test = ado_pending_fragment.userLocation;
                Log.d("test",test.toString());
                final int position = this.getAdapterPosition();
                Location incidentLocation = new Location(" ");
                incidentLocation.setLatitude(Double.parseDouble(dlatitude.get(position)));
                incidentLocation.setLongitude(Double.parseDouble(dlongitude.get(position)));
                final boolean inCircle = checkRange(test,incidentLocation);
                Log.d("test", String.valueOf(inCircle));
                final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(context);
                View sheetView = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.pending_options_ado, null);
                mBottomDialogNotificationAction.setContentView(sheetView);
                //mBottomDialogNotificationAction.setCancelable(false);
                mBottomDialogNotificationAction.show();

                // Remove default white color background
                FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheet.setBackground(null);

                LinearLayout for_check = (LinearLayout) sheetView.findViewById(R.id.for_check);
                LinearLayout for_navigation = (LinearLayout) sheetView.findViewById(R.id.for_navigation);
                LinearLayout for_cancel = (LinearLayout) sheetView.findViewById(R.id.for_cancel_ado);
                TextView check_in = sheetView.findViewById(R.id.check);
                if(inCircle)
                    check_in.setTextColor(Color.rgb(0,200,0));

                for_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomDialogNotificationAction.dismiss();
                        Toast.makeText(context,"in circle is "+inCircle,Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,get().toString(),Toast.LENGTH_SHORT).show();





                        Bundle bundle = new Bundle();
                        bundle.putString("id",did.get(position));
                        bundle.putString("lat",dlatitude.get(position));
                        bundle.putString("long",dlongitude.get(position));
                        PendingReport abc = new PendingReport();
                        abc.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_ado,abc).addToBackStack(null).commit();



                         /*
                        Intent intent = new Intent(context, CheckInActivity2.class);
                        intent.putExtra("id", did.get(position));
                        intent.putExtra("lat",dlatitude.get(position));
                        intent.putExtra("long",dlongitude.get(position));
                        intent.putExtra("village_name", dlocation_name.get(position));
                        //Log.d(TAG, "onClickCheckIn: " );
                        context.startActivity(intent);

                          */


                    }
                });

                for_navigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(mcontext,"clicked navigation with "+ longitude.get(position)+" and "+latitude.get(position),Toast.LENGTH_LONG).show();
                        //Snackbar.make(mView,"email here",Snackbar.LENGTH_LONG).show();
                        onClickNavigation(dlatitude.get(position).toString(), dlongitude.get(position).toString());
                    }
                });
                NotificationManagerCompat manager = NotificationManagerCompat.from(context);

                for_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Snackbar.make(mView,"Cancel here",Snackbar.LENGTH_LONG).show();
                        mBottomDialogNotificationAction.dismiss();
                    }
                });
                // }

            }

            if (isCompleted) {
                int position = this.getAdapterPosition();
                Bundle bundle = new Bundle();
                bundle.putString("id", did.get(position));
                bundle.putString("review_address_top", dlocation_name.get(position) + ", " + dlocation_address.get(position));
                /*
                bundle.putBoolean("isDdo", true);
                bundle.putBoolean("isAdmin", false);
                bundle.putBoolean("isComplete", true);

                ongoingDetailsFragment abc = new ongoingDetailsFragment();
                abc.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_ado, abc).addToBackStack(null).commit();

                 */
                Intent intent = new Intent(context,ado_complete_details.class);
                intent.putExtra("id", did.get(position));
                intent.putExtra("review_address_top",dlocation_name.get(position) + ", " + dlocation_address.get(position));
                context.startActivity(intent);

            }
        }
    }

    public void onClickNavigation(String lat, String lon) {
        //String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitude, longitude, "Destination Location");
        String uri = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lon;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        context.startActivity(intent);
    }

    public boolean checkIfLocationEnabled(){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(context,"An exception occured while checking GPS Location ",Toast.LENGTH_SHORT);
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(context,"An exception occured while checking Network Location ",Toast.LENGTH_SHORT);
        }

        if(!gps_enabled && !network_enabled) {
            //Toast.makeText(context,"Enable your location",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
            // Toast.makeText(context,"Location is enabled",Toast.LENGTH_SHORT).show();
        }
    }

    public Location get() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(context,"Returned from if",Toast.LENGTH_LONG).show();
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    AdoCurrentLocation.setLatitude(location.getLatitude());
                    AdoCurrentLocation.setLongitude(location.getLongitude());
                }
            });
        } else {
            Toast.makeText(context, "You denied permission to access location", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        //Toast.makeText(context,AdoCurrentLocation.toString(),Toast.LENGTH_SHORT).show();
        return AdoCurrentLocation;

    }

    public boolean checkRange(Location test,Location center){
        //Location test = get();
        float distanceInMeters = center.distanceTo(test);
        Log.d("test", String.valueOf(distanceInMeters));
        // Toast.makeText(context,"radius is  "+String.valueOf(distanceInMeters),Toast.LENGTH_SHORT).show();
        //boolean isWithin10km = distanceInMeters < 4168661;
        boolean isWithin10km = distanceInMeters < 20000;
        return isWithin10km;
    }
}




