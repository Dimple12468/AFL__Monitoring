package com.theagriculture.app.Admin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;
    private ArrayList<String> url;
    public  DownloadImage(ImageView bmImage, ArrayList<String> image_URL) {
        this.imageView = bmImage;
        this.url = image_URL;
        Log.d("Image URL " , "in constructor: "+ url);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
//        String urldisplay = image_URL.;
//        Bitmap bmp = null;
        try {
            URL urlConnection = new URL(url.get(0));
            Log.d("Image URL " , "in doInBackground: "+ url);
//            InputStream in = new java.net.URL(urldisplay).openStream();
//            bmp = BitmapFactory.decodeStream(in);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }
}








