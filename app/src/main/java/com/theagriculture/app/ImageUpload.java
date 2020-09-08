package com.theagriculture.app;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class ImageUpload extends AppCompatActivity {
    Button camera, done;
    boolean isFirstPic = true;
    private static String TAG = "CheckInActivity2";
    private static int IMAGE_CAPTURE_RC = 123;
    private ArrayList<File> mImages;
    private String imageFilePath;
    ///////public Ado.ReportImageRecyAdapter adapter;
    RecyclerView recyclerView;
    private ArrayList<String> mImagesPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);

        Button abc = findViewById(R.id.camera);
        abc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadingPhotos();
                if (isFirstPic) {
                    openCameraIntent();
                    isFirstPic = false;
                } else if (mImages.size() < 4)
                    openCameraIntent();
                else
                    Toast.makeText(ImageUpload.this, "Max Photos Reached...images are " + mImagesPath, Toast.LENGTH_SHORT).show();

            }
        });

        /*
        mImagesPath = new ArrayList<>();
        mImages = new ArrayList<>();
        adapter = new Ado.ReportImageRecyAdapter(this, mImagesPath);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

         */

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.d(TAG, "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pictureIntent, IMAGE_CAPTURE_RC);
        }
    }

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
        if (requestCode == IMAGE_CAPTURE_RC) {
            if (resultCode == RESULT_OK) {
                File file = new File(imageFilePath);
                try {
                    mImagesPath.add(imageFilePath);
                    File compressedFile = new Compressor(ImageUpload.this).compressToFile(file);
                    mImages.add(compressedFile);
                    ////////adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to load Image, please try again!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
