package com.theagriculture.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.utils.Utils;
import com.google.gson.JsonObject;
import com.theagriculture.app.Admin.AdminActivity;
import com.theagriculture.app.Ado.AdoActivity;
import com.theagriculture.app.Dda.DdaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.theagriculture.app.R.drawable.ic_visibility_black_24dp;
import static com.theagriculture.app.R.drawable.ic_visibility_off_black_24dp;
import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;
import static java.security.AccessController.getContext;


public class login_activity extends AppCompatActivity {

    private String token;
    private String typeOfUser;
    private String Name;
    private static final String TAG = "login_activity";
    private EditText editEmail, editPassword;
    private TextView signUpAdo, signUpDda,tvForgot;
//    private TextView tvReg;

    private String urlget;          // = "http://api.theagriculture.tk/api/get-user/";
    private String urlpost;         // = "http://api.theagriculture.tk/api-token-auth/";

    private AlertDialog dialog;
    private CheckBox checkBox;
    private Button btnLogin;
    private ImageButton imageButton;
    public boolean hide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_activity);

        urlget = Globals.userTypeURL;
        urlpost = Globals.urlPost_user;

        Log.d(TAG,urlget);
        Log.d(TAG,urlpost);

        //to handle soft keys on some phones
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            //int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            int option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();
/*

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           Window w = getWindow();
           w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
       }
*/

        final SharedPreferences sp = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String Usertype = sp.getString("role", "");
        if (sp.contains("key")) {
            Intent intent = null;
            if (Usertype.equals("1"))
                Toast.makeText(login_activity.this, "login for farmer", Toast.LENGTH_SHORT).show();
            if (Usertype.equals("2"))
                intent = new Intent(this, AdoActivity.class);
            if (Usertype.equals("3"))
                Toast.makeText(login_activity.this, "login for block admin", Toast.LENGTH_SHORT).show();
            if (Usertype.equals("4"))
                intent = new Intent(this, DdaActivity.class);
            if (Usertype.equals("5"))
                intent = new Intent(this, AdminActivity.class);
            if (Usertype.equals("6"))
                Toast.makeText(this, "login for super admin", Toast.LENGTH_SHORT).show();
            if (intent != null) {
                startActivity(intent);
                finish();
            }

        }

        editEmail = findViewById(R.id.editText);
        editPassword = findViewById(R.id.editText3);
        btnLogin = findViewById(R.id.button);
        tvForgot = findViewById(R.id.tvForgot);
//        tvReg = findViewById(R.id.tvReg);

        //function to go to register page
/*        String text = "Don't have an account Register";
        SpannableString ss1 = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent1=new Intent(login_activity.this,RegistrationActivity.class);
                startActivity(intent1);
                finish();//to prevent stack issue
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };
        ss1.setSpan(clickableSpan1,22,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvReg.setText(ss1);
//        tvReg.setMovementMethod(LinkMovementMethod.getInstance());*/

        //function for forgot password
        String text_forgot = "Forgot Password?";
        SpannableString ss2 = new SpannableString(text_forgot);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent2=new Intent(login_activity.this,ForgetPasswordActivity.class);
                startActivity(intent2);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
                ds.setUnderlineText(false);
            }
        };
        ss2.setSpan(clickableSpan2,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvForgot.setText(ss2);
        tvForgot.setMovementMethod(LinkMovementMethod.getInstance());

        //when login button is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = editEmail.getText().toString().trim();
                String mPass = editPassword.getText().toString().trim();

                if (!mEmail.isEmpty() && !mPass.isEmpty()) {
                    Login(mEmail, mPass);
                } else if (!mEmail.isEmpty() && mPass.isEmpty()) {
                    displayDialog("Please insert password");
                } else if (mEmail.isEmpty() && !mPass.isEmpty()) {
                    displayDialog("Please insert Email");
                } else {
                    displayDialog("Please insert Email and password");
                }
            }
        });
        //function to view password
        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hide==true){
                    int start= editPassword.getSelectionStart();
                    int end= editPassword.getSelectionEnd();
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide=false;
                    imageButton.setImageResource(R.drawable.show_password);
                    editPassword.setSelection(start,end);
                }
                else{
                    int start= editPassword.getSelectionStart();
                    int end= editPassword.getSelectionEnd();
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hide=true;
                    imageButton.setImageResource(R.drawable.hide_password);
                    editPassword.setSelection(start,end);
                }

            }
        });

        //function to change colour of login button when text is entered
        editEmail.addTextChangedListener(loginTextWatcher);//loginTextWatcher function is defined at last
        editPassword.addTextChangedListener(loginTextWatcher);

        //end of onCreate function
    }

    private void Login(final String email, final String password) {

        final ProgressDialog dialog = new ProgressDialog(login_activity.this,R.style.AlertDialog);

        // Set progress dialog style spinner
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Logging In....");
        // Set the progress dialog background color
        //pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        //pd.setIndeterminate(false);
        dialog.show();

        Log.d(TAG, "onResponse: login clicked");
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        JSONObject postparams = null;
        try {
            postparams = new JSONObject();
            postparams.put("username", email);
            postparams.put("password", password);
        } catch (JSONException e) {
            Log.d(TAG, "Login: Error:" + e);
            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            btnLogin.setEnabled(true);
        }

        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlget, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String pk;
                        try {
                            JSONObject c = new JSONObject(String.valueOf(response));
                            Log.d(TAG, "onResponse: " + c);
                            JSONObject a = c.getJSONObject("user");
                            Log.d(TAG, "onResponse: user:" + a);
                            typeOfUser = a.getString("role");
                            Name = a.getString("name");
                            //from here
                            String number="Not Available",email="Not Available",address="Not Available",image=null;
                            try{
                                number = a.getString("phone_number");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{
                                email = a.getString("email");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{
                                image = a.getString("image");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{/*
                                JSONArray village = c.getJSONArray("village_ado");
                                JSONObject villageDetails = village.getJSONObject(0);//check as empty
                                String villageName = villageDetails.getString("village");//check as empty
                                */
                                JSONObject state= a.getJSONObject("state");
                                address = state.getString("state");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ////till here
                            pk = a.getString("id");
                            Log.d(TAG, "onResponse: valuepk"+pk);
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                            editor.putString("role", typeOfUser);
                            editor.putString("Name", Name);
                            editor.putString("id", pk);
                            //from here
                            editor.putString("PhoneNumber",number);
                            editor.putString("Email",email);
                            editor.putString("Address",address);
                            editor.putString("Image",image);
                            ///to here
                            editor.apply();

                            Intent intent = null;

                            if (typeOfUser.equals("1")) {
                                Toast.makeText(login_activity.this, "login for farmer", Toast.LENGTH_SHORT).show();
                            }else if (typeOfUser.equals("2")) {
                                intent = new Intent(login_activity.this, AdoActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (typeOfUser.equals("3")) {
                                Toast.makeText(login_activity.this, "login for block admin", Toast.LENGTH_SHORT).show();
                            }else if (typeOfUser.equals("4")) {
                                btnLogin.setEnabled(false);
                                btnLogin.setClickable(false);
                                intent = new Intent(login_activity.this, DdaActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (typeOfUser.equals("5")) {
                                intent = new Intent(login_activity.this,AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (typeOfUser.equals("6")) {
                                Toast.makeText(login_activity.this, "login for super admin", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                //Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                                displayDialog("Incorrect Password or Email");
                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error in get catch block :" + e.getMessage());
                            //Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            displayDialog("Please try again");
                            btnLogin.setEnabled(true);
//
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            noInternetDialog();
                        else if (error instanceof ClientError)
                            displayDialog("Invalid User!");
                        else {
                            Log.d(TAG,"error.networkResponse.toString()" + error.networkResponse.toString());
                            displayDialog("Something went wrong,please try again");
                        }
                        btnLogin.setEnabled(true);
                        dialog.dismiss();
                        Log.d(TAG, "onErrorResponse: some error in get: " + error.getLocalizedMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                Log.d(TAG, "getHeaders: ");
                headers.put("Authorization", "Token " + token);
                Log.d(TAG,token);
                return headers;
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlpost, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //retrieve the token from server
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            token = jsonObject.getString("key");
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                            editor.putString("key", token);
                            editor.apply();
                            MyRequestQueue.add(jsonObjectRequest1);
                            Log.d(TAG, "onResponse: key:" + token);
                        } catch (JSONException e) {
                            dialog.dismiss();
                            btnLogin.setEnabled(true);
                            Log.d(TAG, "onResponse: error in post catch block: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            noInternetDialog();
                        else if (error instanceof ClientError)
                            displayDialog("Incorrect Password or Email");
                        else
                            displayDialog("Something went wrong,please try again");
                        Log.d(TAG, "onErrorResponse: invalid user : " + error);
                        dialog.dismiss();
                        btnLogin.setEnabled(true);
                    }
                });

        MyRequestQueue.add(jsonObjectRequest);

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

    }

    public void onClickregister(String url, boolean isAdo) {

        Intent intent = new Intent(login_activity.this,RegistrationActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isAdo", isAdo);
        startActivity(intent);
        //finish();
    }

    public void onClickForgetPassword(View view) {
        Intent intent = new Intent(login_activity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public final void displayDialog(String str){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(login_activity.this,R.style.AlertDialog);
        builder.setMessage(str);
        androidx.appcompat.app.AlertDialog alertDialog=builder.create();

        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
    }

    public void noInternetDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(login_activity.this, R.style.noInternetDialog);
        builder.setMessage("Not connected to Internet");
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        alertDialog.show();
    }
    //function to change colour of login button when text is entered
    public TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //btnLogin.setBackgroundResource(R.drawable.buttons_before_text);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String username = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if(!username.isEmpty() && !password.isEmpty())
            {
                btnLogin.setBackgroundResource(R.drawable.buttons);
            }
            else{
                btnLogin.setBackgroundResource(R.drawable.buttons_before_text);//this condition is added for when text was first added but now it is cleared
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}

