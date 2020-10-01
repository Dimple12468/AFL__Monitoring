package com.theagriculture.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theagriculture.app.Ado.ReportImageRecyAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import static com.theagriculture.app.AppNotificationChannels.CHANNEL_1_ID;

public class ImageUploadPage extends AppCompatActivity {
    Location userLocation;
    AlertDialog reportSubmitLoading;
    String reportSubmitUrl = "https://api.aflmonitoring.com/api/report-user/add/";
    Button submitImages;
    FloatingActionButton opencamera;

    //recycler view
    RecyclerView recyclerView;
    public ReportImageRecyAdapter adapter;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static int IMAGE_CAPTURE_RC = 123;
    boolean isFirstPic = true;

    int number_of_images=0;
    private ArrayList<File> mImages;
    private String imageFilePath;
    private ArrayList<String> mImagesPath;
    private int photosUploadedCount = 0;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;
    private int limitPhotos = 100;
    private ProgressDialog pDialog;

    String reportId;
    int PhotosUploadedCount =0;
    String imageUploadUrl = "https://api.aflmonitoring.com/api/report-user/images/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload_page);

        notificationManager = NotificationManagerCompat.from(this);
        pDialog=new ProgressDialog(this);

        //getUserLocation();myc4
        //uncommets later

        // Check if any provider (network or gps) is enabled
       // Toast.makeText(getApplicationContext(),(SmartLocation.with(getApplicationContext()).location().state().isAnyProviderAvailable()),Toast.LENGTH_LONG).show();



        reportSubmitLoading = new SpotsDialog.Builder().setContext(ImageUploadPage.this).setMessage("Submitting Report")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        ///reportSubmitLoading.show();
        if(checkIfLocationEnabled()){
            SmartLocation.with(getApplicationContext()).location().oneFix().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                        userLocation = location;
                        Toast.makeText(getApplicationContext(), userLocation.toString(), Toast.LENGTH_LONG).show();
                        sendReport();
                    }
                });
        }
        else{
            Toast.makeText(getApplicationContext(),"Your locations are disabled.Please enable it to send report",Toast.LENGTH_LONG).show();
            finish();
        }

        /*
        SmartLocation.with(getApplicationContext()).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if(checkIfLocationEnabled()) {
                    userLocation = location;
                    Toast.makeText(getApplicationContext(), userLocation.toString(), Toast.LENGTH_LONG).show();
                    sendReport();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enable locations to submit report",Toast.LENGTH_LONG).show();
                    reportSubmitLoading.dismiss();
                    Intent intent= new Intent(getApplicationContext(),Initial_page.class);
                    startActivity(intent);
                }
            }
        });

         */

        mImagesPath = new ArrayList<>();
        mImages = new ArrayList<>();


        submitImages = findViewById(R.id.submit_report);
        opencamera = findViewById(R.id.camera);

        submitImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Synjnd",Toast.LENGTH_LONG).show();
                if(number_of_images==0)
                    Toast.makeText(getApplicationContext(),"You have not uploaded any images",Toast.LENGTH_LONG).show();
                else
                    uploadingPhotos();
                showProgress("Uploading media ...");
            }
        });


        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(number_of_images<limitPhotos) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {

                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                    //number_of_images++;
                                } catch (IOException e) {
                                    Log.d("exception", "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                                    e.printStackTrace();
                                    return;
                                }
                                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", photoFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                ///startActivityForResult(pictureIntent, IMAGE_CAPTURE_RC);
                            }
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                    else{//lower spi
                        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException e) {
                                Log.d("else", "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                                e.printStackTrace();
                                return;
                            }
                            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", photoFile);
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(pictureIntent, IMAGE_CAPTURE_RC);
                        }
                    }
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Max Photos Reached..", Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(getApplicationContext(), "Max Photos Reached...images are " + mImagesPath, Toast.LENGTH_SHORT).show();
//                }

            }
        });


        recyclerView = findViewById(R.id.rvimages);
        adapter = new ReportImageRecyAdapter(this, mImagesPath);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        //sendReport();//myc3


    }

    /*myc2
    public void getUserLocation(){
        SmartLocation.with(getApplicationContext()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                userLocation = location;
            }
        });
    }

     */

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == IMAGE_CAPTURE_RC) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                File file = new File(imageFilePath);
                try {
                    mImagesPath.add(imageFilePath);
                    if (mImages.size()==1)
                        submitImages.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    number_of_images++;
                    File compressedFile = new Compressor(getApplicationContext()).compressToFile(file);
                    mImages.add(compressedFile);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to load Image, please try again!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void sendReport() {


        // Toast.makeText(getApplicationContext(), "Entered reprt function", Toast.LENGTH_LONG).show();
        /*myc1
        reportSubmitLoading = new SpotsDialog.Builder().setContext(ImageUploadPage.this).setMessage("Submitting Report")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        reportSubmitLoading.show();

         */
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject postParams = new JSONObject();
        try {
            //postParams.put("longitude", "76.8498");
            //postParams.put("latitude", "28.2314");
            postParams.put("longitude", String.valueOf(userLocation.getLongitude()));
            postParams.put("latitude", String.valueOf(userLocation.getLatitude()));
            postParams.put("name", "Nmae");
            postParams.put("phone_number", "Phonenumber");


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "An exception occured", Toast.LENGTH_LONG).show();
            reportSubmitLoading.dismiss();
            Log.d("catch", "submitReport: " + e);
        }
        Toast.makeText(getApplicationContext(),"posting values "+postParams.toString(),Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(reportSubmitUrl, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                            JSONObject singleObject = new JSONObject(String.valueOf(response));
                            reportId = singleObject.getString("NormalUserReport_id");
                            Toast.makeText(getApplicationContext(),"report id is "+reportId,Toast.LENGTH_LONG).show();
                            Log.d("response", "onResponse: " + singleObject);
                            reportSubmitLoading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            reportSubmitLoading.dismiss();
                            Log.d("catchq", "jsonexception: reportSubmitRequest " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error is "+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError){
                            Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is case3", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        reportSubmitLoading.dismiss();
                    }
                });
        /*
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
               /// map.put("Authorization", "Token " +mytoken);
                return map;
            }
        };

         */


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
    }

    //caamera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showProgress(String str){
        try{
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMax(100); // Progress Dialog Max Value
            pDialog.setMessage(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();
        }catch (Exception e){

        }
    }

    public void updateProgress(long val, String title){
        pDialog.setTitle(title);
//        pDialog.setMessage(msg);
//        pDialog.setProgress(val);
    }

/*    private void uploadPhotos()
    {
        final int progressMax = mImages.size() - photosUploadedCount;
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle("Uploading Photos")
                .setContentText("0/" + progressMax)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress((int) mImages.get(photosUploadedCount).length(), 0, false);
        notificationManager.notify(1, notificationBuilder.build());
        uploadingPhotos();

    }*/



    public void uploadingPhotos(){
        // Toast.makeText(getApplicationContext(),"Eneterd uploading photos function",Toast.LENGTH_LONG).show();
//        reportSubmitLoading = new SpotsDialog.Builder().setContext(getApplicationContext()).setMessage("Uploading Images")
//                .setTheme(R.style.CustomDialog)
//                .setCancelable(false)
//                .build();
//        reportSubmitLoading.show();

        AndroidNetworking.upload(imageUploadUrl)
                //.addHeaders("Authorization", "Token " + token)//not needed as jatin has removed the authorisation
                .addMultipartParameter("NormalUserReport",reportId)
                .addMultipartFile("image", mImages.get(PhotosUploadedCount))
                .setTag("Upload Images")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        if (bytesUploaded == totalBytes) {
                        PhotosUploadedCount++;
                        long totalpercent = (bytesUploaded /totalBytes)*100;
                        updateProgress(totalpercent,"Uploading file ");
                        }
                        // Toast.makeText(getApplicationContext(),"uploading image "+PhotosUploadedCount+"bytesUploaded are "+String.valueOf(bytesUploaded)+"total bytes are "+String.valueOf(totalBytes),Toast.LENGTH_LONG).show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         Log.d("upload", "onResponse: " + response);
                                         PhotosUploadedCount++;
                                         Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                                         if(PhotosUploadedCount==mImages.size()) {
                                             reportSubmitLoading.dismiss();
                                             //submit_btn.setText("Submitted");
                                         }
                                     }

                                     @Override
                                     public void onError(ANError anError) {
                                         Log.d("upload", "onError: " + anError.getErrorBody());
                                         Toast.makeText(getApplicationContext(), "Photos Upload failed, please try again ", Toast.LENGTH_SHORT).show();
                                         reportSubmitLoading.dismiss();
                                     }

                                 }
                );
    }

    public boolean checkIfLocationEnabled(){
        //Toast.makeText(getApplicationContext(),"enterd",Toast.LENGTH_LONG).show();
        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(getApplicationContext(),"An exception occured while checking GPS Location ",Toast.LENGTH_SHORT);
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(getApplicationContext(),"An exception occured while checking Network Location ",Toast.LENGTH_SHORT);
        }

        Toast.makeText(getApplicationContext(),gps_enabled+" and "+network_enabled,Toast.LENGTH_LONG).show();
        if(!gps_enabled && !network_enabled) {
            //Toast.makeText(getApplicationContext(),"Enable your location",Toast.LENGTH_SHORT).show();
            return false;
        }
        /*
        else if(!gps_enabled && network_enabled) {
            Toast.makeText(getApplicationContext(),"Network is enabled",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(gps_enabled && !network_enabled) {
            Toast.makeText(getApplicationContext(),"Gps is enabled",Toast.LENGTH_SHORT).show();
            return false;
        }

         */
        else{
            //Toast.makeText(getApplicationContext(),"Location is enabled",Toast.LENGTH_SHORT).show();
            return true;

        }
    }


    /*site wla
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            /// imageView.setImageBitmap(photo);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(),"onActivyt",Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK ) {
                File file = new File(imageFilePath);
                try {
                    mImagesPath.add(imageFilePath);
                    File compressedFile = new Compressor(getApplicationContext()).compressToFile(file);
                    mImages.add(compressedFile);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to load Image, please try again!", Toast.LENGTH_SHORT).show();
            }
        }

    }

     */


}



