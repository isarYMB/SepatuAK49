package com.hp.ali.ecomerceapp.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.fragments.MainFragment;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;


public class MainActivity extends AppCompatActivity {
    Fragment fragmentClass = new MainFragment();
    SlidingRootNav slidingRootNav;
    ImageButton menu_btn;
    Context context;
    Boolean isOpened = false;
    ImageView img_history,img_wa;
    GoogleSignInClient mGoogleSignInClient;
    public LinearLayout top_toolbar;
    String nama,email;
    LottieAnimationView splashLottie;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        top_toolbar =  findViewById(R.id.top_toolbar);
        context = getApplicationContext();
        menu_btn =  findViewById(R.id.menu_btn);
        img_history = findViewById(R.id.keranjang);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity(); // or finish();
                } else {
                    Toast.makeText(getBaseContext(), "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show();
                }
                pressedTime = System.currentTimeMillis();

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                customType(MainActivity.this,"left-to-right");
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragmentClass)
                .commit();


    }


    private void registerUser() {
        if (checkNetworkConnection(MainActivity.this)){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                if (resp.equals("true")){

                                }else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("NAMA_DEPAN", nama);
                    params.put("EMAIL", email);
                    return params;
                }
            };

            VolleyConnection.getInstance(MainActivity.this).addToRequestQue(stringRequest);

        }else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection(MainActivity mainActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.no_connection, findViewById(R.id.no_internetLayout));
        view.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkNetworkConnection(MainActivity.this)){
                    showInternetDialog();
                }else {
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                }
            }
        });
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}