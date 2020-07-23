package com.theagriculture.app.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.theagriculture.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import dmax.dialog.SpotsDialog;

import static com.theagriculture.app.AppNotificationChannels.CHANNEL_2_ID;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class map_fragemnt extends Fragment implements OnMapReadyCallback {//OnMapReady() function gives a call back to this OnMap ReadyCallback

    GoogleMap ngoogleMap;
    MapView mMapView;
    View mView;
    private File csvFile;
    private AlertDialog uploadingDialog;
    private LinearLayout for_upload,for_location,for_email,for_cancel;
    private NotificationManagerCompat manager;
    private String token;

    public map_fragemnt() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map_admin, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(mView,"FAB OK HAI",Snackbar.LENGTH_LONG).show();

                final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.activity_fab_onclick, null);
                mBottomDialogNotificationAction.setContentView(sheetView);
                //mBottomDialogNotificationAction.setCancelable(false);
                mBottomDialogNotificationAction.show();

                // Remove default white color background
                FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheet.setBackground(null);

                for_upload = (LinearLayout) sheetView.findViewById(R.id.for_upload);
                for_location = (LinearLayout) sheetView.findViewById(R.id.for_location);
                for_email = (LinearLayout) sheetView.findViewById(R.id.for_email);
                for_cancel = (LinearLayout) sheetView.findViewById(R.id.for_cancel);

                final String url_location = "http://18.224.202.135/api/upload/locations/";
                final String url_bulk = "http://18.224.202.135/api/upload/mail/";

                /*
                for_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(mView,"Upload here",Snackbar.LENGTH_LONG).show();
                    }
                });

                 */

                for_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCsvPicker(url_location);
                        //Snackbar.make(mView,"for location",Snackbar.LENGTH_LONG).show();
                    }
                });

                for_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCsvPicker(url_bulk);
                        //Snackbar.make(mView,"email here",Snackbar.LENGTH_LONG).show();
                    }
                });
                manager = NotificationManagerCompat.from(getActivity());

                for_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Snackbar.make(mView,"Cancel here",Snackbar.LENGTH_LONG).show();
                        mBottomDialogNotificationAction.dismiss();
                    }
                });


            }
        });
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        ngoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //View locationButton = ((View) mView.getView().findViewById(Integer.parseInt("1")).
        //      getParent()).findViewById(Integer.parseInt("4"));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        //TODO change location of google map toolbar
        googleMap.addMarker(new MarkerOptions().position(new LatLng(20.5937, 78.9629)).title("India").snippet("My country"));
        CameraPosition India = CameraPosition.builder().target(new LatLng(20.5937, 78.9629)).zoom(10).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(India));
    }

    private void openCsvPicker(final String url) {
        File file = Environment.getExternalStorageDirectory();
        String start = file.getAbsolutePath();
        new ChooserDialog(getActivity())
                .withStartFile(start)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String s, File file) {
                        csvFile = file;
                        uploadingDialog = new SpotsDialog.Builder().setContext(getActivity())
                                .setMessage("Uploading Csv...")
                                .setCancelable(false)
                                .setTheme(R.style.CustomDialog)
                                .build();
                        uploadingDialog.show();
                        uploadCsv(url);
                    }
                })
                .withOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                })
                .build()
                .show();
    }

    private void uploadCsv(String url) {
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("Uploading Csv")
                .setContentText("0/100")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress((int) csvFile.length(), 0, false);
        manager.notify(2, notificationBuilder.build());
        AndroidNetworking.upload(url)
                .addHeaders("Authorization", "Token " + token)
                .addMultipartFile("location_csv", csvFile)
                .setTag("Upload Csv")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //Log.d(TAG, "onProgress: " + bytesUploaded);
                        notificationBuilder.setContentText(((int) (bytesUploaded / totalBytes) * 100) + "/100")
                                .setProgress((int) totalBytes, (int) ((bytesUploaded / totalBytes) * 100), false);
                        manager.notify(2, notificationBuilder.build());

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "onResponse: " + response);
                        String count;
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            count = rootObject.getString("count");
                            Toast.makeText(getActivity(), "Successfully Uploaded " + count + " locations", Toast.LENGTH_LONG).show();
                            uploadingDialog.dismiss();
                            notificationBuilder.setContentText("Upload Successful!")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false);
                            manager.notify(2, notificationBuilder.build());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            uploadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.d(TAG, "onError: " + anError.getErrorDetail() + " " + anError.getErrorBody() +" " + anError.getMessage() + " " + anError.getErrorCode());
                        Toast.makeText(getActivity(), "Sorry something went wrong, please try again!",
                                Toast.LENGTH_LONG).show();
                        notificationBuilder.setContentText("Upload Failed!")
                                .setProgress(0, 0, false)
                                .setOngoing(false);
                        manager.notify(2, notificationBuilder.build());
                        uploadingDialog.dismiss();
                    }
                });
    }


}
