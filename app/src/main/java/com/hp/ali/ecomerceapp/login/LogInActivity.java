package com.hp.ali.ecomerceapp.login;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static maes.tech.intentanim.CustomIntent.customType;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.activities.CartActivity;
import com.hp.ali.ecomerceapp.activities.MainActivity;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.models.lottiedialogfragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    Button btn_login,btn_register;
    String nama,email;
    TextView txt_dnt_hav_acc;
    EditText edt_username,edt_password;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    SignInButton signInButton;
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    lottiedialogfragment lottiedialogfragment;
    LottieAnimationView splashLottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        txt_dnt_hav_acc =  findViewById(R.id.txt_dnt_hav_acc);
        edt_username = findViewById(R.id.edt_email_login);
        edt_password = findViewById(R.id.edt_password_login);
        signInButton = (SignInButton)findViewById(R.id.googlebtn);
        progressDialog = new ProgressDialog(LogInActivity.this);
        sessionManager = new SessionManager(this);
        lottiedialogfragment = new lottiedialogfragment(this);
        splashLottie = findViewById(R.id.lottie_noConnection);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LogInActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        if (!checkNetworkConnection(LogInActivity.this)){
            showInternetDialog();
        }
        if (sessionManager.getSPSudahLogin()){
            startActivity(new Intent(LogInActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        txt_dnt_hav_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String pesan = "[Kendala Login/Register]";
                    PackageManager packageManager = getApplication().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                            + URLEncoder.encode(pesan+"\n"+"Saya memiliki kendala saat melakukan login/register"+"\n"+"\n", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }else {

                    }
                } catch(Exception e) {
                    Log.e("ERROR WHATSAPP",e.toString());
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);
                customType(LogInActivity.this,"left-to-right");
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUsername = edt_username.getText().toString();
                String sPassword = edt_password.getText().toString();
                if (sUsername!=null && sPassword!=null){
                    SignInServer(sUsername, sPassword);
                }else {
                    Toast.makeText(LogInActivity.this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });


        String htmlText = "<label style='color:#DBBCBC'>Kendala Saat Login?<label style='color:#FF7043'> Hubungi <u>Di Sini</u></label></label>";
        txt_dnt_hav_acc.setText(Html.fromHtml(htmlText));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    public void SignInServer (final String username, final String password){
        if (checkNetworkConnection(LogInActivity.this)){
            lottiedialogfragment.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String data = jsonObject.getString("data");
                                if (resp.equals("true")) {
                                    JSONObject jsonObject2 = new JSONObject(data);
                                    Integer idUser = Integer.valueOf(jsonObject2.getString("ID"));
                                    String nama_depan = jsonObject2.getString("NAMA_DEPAN");
                                    String email = jsonObject2.getString("EMAIL");
                                    String tag = jsonObject2.getString("TAG_KOTA");
                                    String SSalamat = jsonObject2.getString("ALAMAT_LENGKAP");
                                    String SSnomor = jsonObject2.getString("NOMOR_TELEPON");

                                    if (SSnomor.equals("Belum di atur")){
                                        sessionManager.saveSPInt(String.valueOf(SessionManager.SP_ID), idUser);
                                        sessionManager.saveSPString(SessionManager.SP_TAG, tag);
                                        sessionManager.saveSPString(SessionManager.SP_ALAMAT, SSalamat);
                                        sessionManager.saveSPString(SessionManager.SP_NAME, nama_depan);
                                        sessionManager.saveSPString(SessionManager.SP_EMAIL, email);
                                        Intent i = new Intent(LogInActivity.this, OTPAuthActivity.class);
                                        customType(LogInActivity.this,"left-to-right");
                                        startActivity(i);
                                    }else {
                                        sessionManager.saveSPInt(String.valueOf(SessionManager.SP_ID), idUser);
                                        sessionManager.saveSPString(SessionManager.SP_TAG, tag);
                                        sessionManager.saveSPString(SessionManager.SP_ALAMAT, SSalamat);
                                        sessionManager.saveSPString(SessionManager.SP_NAME, nama_depan);
                                        sessionManager.saveSPString(SessionManager.SP_NOMOR, SSnomor);
                                        sessionManager.saveSPString(SessionManager.SP_EMAIL, email);
                                        sessionManager.saveSPBoolean(SessionManager.SP_SUDAH_LOGIN, true);
                                        Intent i = new Intent(LogInActivity.this, MainActivity.class);
                                        customType(LogInActivity.this,"left-to-right");
                                        startActivity(i);
                                    }

                                }else {
                                    Toast.makeText(getApplicationContext(), "Email atau Password tidak sesuai", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("EMAIL", username);
                    params.put("PASS", password);
                    return params;
                }
            };

            VolleyConnection.getInstance(LogInActivity.this).addToRequestQue(stringRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottiedialogfragment.cancel();
                }
            }, 5000);

        }else {
            showInternetDialog();
        }
    }

    private void signIn() {
        if (checkNetworkConnection(LogInActivity.this)){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }else {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(LogInActivity.this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LogInActivity.this);
            if (acct != null) {
                nama = acct.getDisplayName();
                email = acct.getEmail();
                sessionManager.saveSPString(SessionManager.SP_NAME, nama);
                cekAkun(email);
            }

        } catch (ApiException e) {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LogInActivity.this, "Login Google dibatalkan", Toast.LENGTH_LONG).show();
        }
    }

    public void cekAkun (final String email){
        if (checkNetworkConnection(LogInActivity.this)){
            lottiedialogfragment.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_CEKAKUN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String data = jsonObject.getString("data");
                                if (resp.equals("true")) {
                                    signOut(nama,email);
                                }else {
                                    SignInServer(email,"client160721");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("EMAIL", email);
                    return params;
                }
            };

            VolleyConnection.getInstance(LogInActivity.this).addToRequestQue(stringRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottiedialogfragment.cancel();
                }
            }, 4000);

        }else {
            showInternetDialog();
        }
    }

    private void signOut(final String nama, final String email) {
        if (checkNetworkConnection(LogInActivity.this)){
            lottiedialogfragment.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_REGISTER_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String data = jsonObject.getString("data");
                                if (resp.equals("true")) {
                                    SignInServer(email,"client160721");
                                }else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                    params.put("NAMA_BELAKANG", "Belum di atur");
                    params.put("EMAIL", email);
                    params.put("PASS", "client160721");
                    params.put("PROVINSI", "Belum di atur");
                    params.put("KOTA", "Belum di atur");
                    params.put("TAG_KOTA", "Belum di atur");
                    params.put("ALAMAT_LENGKAP", "Belum di atur");
                    params.put("KODE_POS", "Belum di atur");
                    params.put("NOMOR_TELEPON", "Belum di atur");
                    return params;
                }
            };

            VolleyConnection.getInstance(LogInActivity.this).addToRequestQue(stringRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottiedialogfragment.cancel();
                }
            },4000);
        }else {
            lottiedialogfragment.show();
            showInternetDialog();
        }
    }
    public boolean checkNetworkConnection(LogInActivity logInActivity){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private void showInternetDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(LogInActivity.this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(this).inflate(R.layout.no_connection, findViewById(R.id.no_internetLayout));
        view.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkNetworkConnection(LogInActivity.this)){
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
}