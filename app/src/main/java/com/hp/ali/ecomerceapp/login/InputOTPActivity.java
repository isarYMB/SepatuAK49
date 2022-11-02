package com.hp.ali.ecomerceapp.login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InputOTPActivity extends AppCompatActivity {
    EditText t2;
    Button b2, kirimUlang;
    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;
    SessionManager sessionManager;
    LottieAnimationView otpAnimation;
    TextView countText;
    TextView hubungiAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_otpactivity);
        sessionManager = new SessionManager(this);
        phonenumber=getIntent().getStringExtra("mobile").toString();
        t2=(EditText)findViewById(R.id.t2);
        b2=(Button)findViewById(R.id.b2);
        mAuth=FirebaseAuth.getInstance();
        countText=findViewById(R.id.countText);
        kirimUlang=findViewById(R.id.kirimUlang);
        hubungiAdmin =  findViewById(R.id.hubungiAdmin);

        otpAnimation = findViewById(R.id.otpAnimation);

        hubungiAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String pesan = "[Kendala OTP]";
                    PackageManager packageManager = getApplication().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                            + URLEncoder.encode(pesan+"\n"+"Saya memiliki kendala saat melakukan OTP"+"\n"+"\n", "UTF-8");
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

        String htmlText = "<label style='color:#DBBCBC'>Ada Kendala OTP?<label style='color:#FF7043'> Hubungi <u>Di Sini</u></label></label>";
        hubungiAdmin.setText(Html.fromHtml(htmlText));

        kirimUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(InputOTPActivity.this, OTPAuthActivity.class));
            }
        });

        initiateotp();

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countText.setText("Kode OTP Berlaku Dalam Waktu : " + millisUntilFinished / 1000 + " Detik");
            }

            public void onFinish() {
                countText.setText("Jika Belum Menerima OTP Klik Tombol KIRIM ULANG");
            }
        }.start();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(t2.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Kolom OTP tidak boleh kosong",Toast.LENGTH_LONG).show();
                else if(t2.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"OTP yang anda masukkan salah",Toast.LENGTH_LONG).show();
                else
                {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otpid,t2.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
    }

    private void initiateotp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            updateProfileNomor(phonenumber);
                            startActivity(new Intent(InputOTPActivity.this, MainActivity.class));
                            finish();

                        } else {

                        }
                    }
                });
    }
    private void updateProfileNomor(String nomor) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_UPDATENOMOR_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (resp.equals("true")) {
//                                Toast.makeText(InputOTPActivity.this, message, Toast.LENGTH_SHORT).show();
                                sessionManager.saveSPBoolean(SessionManager.SP_SUDAH_LOGIN, true);
                                sessionManager.saveSPString(SessionManager.SP_NOMOR, phonenumber);
                            }else {
                                Toast.makeText(InputOTPActivity.this, message, Toast.LENGTH_SHORT).show();
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
                HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
                int sid = id.get(sessionManager.SP_ID);
                params.put("ID", String.valueOf(sid));
                params.put("NOMOR_TELEPON", nomor);
                return params;
            }
        };

        VolleyConnection.getInstance(InputOTPActivity.this).addToRequestQue(stringRequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2000);

    }
}