package com.hp.ali.ecomerceapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.EditAlamat;
import com.hp.ali.ecomerceapp.activities.MainActivity;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.hp.ali.ecomerceapp.login.OTPAuthActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    ScrollView profile_view;
    GoogleSignInClient mGoogleSignInClient;
    CardView button_sign_out;
    Button login_profile;
    TextView nameTV;
    TextView emailTV;
    TextView idTV,simpan,batalsimpan,username,alamat,nomor_telepon;
    ImageView photoIV;
    SessionManager sessionManager;
    CardView edit_nama,edit_nomor,edit_alamat,edit_password;
    private AlertDialog.Builder alert;
    private AlertDialog ad;
    private EditText editText;
    LottieAnimationView no_account;
    LinearLayout no_profile;
    String Salamat;
    String Snomor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity)getActivity()).top_toolbar.setVisibility(View.GONE);
        button_sign_out = v.findViewById(R.id.log_out);
        nameTV = v.findViewById(R.id.name);
        emailTV = v.findViewById(R.id.email);
        alamat = v.findViewById(R.id.alamat);
        nomor_telepon = v.findViewById(R.id.nomor_telepon);
        photoIV = v.findViewById(R.id.photo);
        edit_alamat= v.findViewById(R.id.edt_alamat_profil);
        edit_nomor= v.findViewById(R.id.edt_nomor_profil);

        no_account= v.findViewById(R.id.no_account);
        no_profile= v.findViewById(R.id.no_profil);

        login_profile = v.findViewById(R.id.login_profile);

        profile_view=v.findViewById(R.id.profile_view);

        ((MainActivity)getActivity()).updateStatusBarColor("#F5F5F8");

        //Mengubah warna status bar ke dark
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        edit_alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),EditAlamat.class);
                startActivity(intent);
            }
        });
        edit_nomor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OTPAuthActivity.class);
                startActivity(intent);
            }
        });

        sessionManager = new SessionManager(getContext());
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();



        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            nameTV.setText(personName);
            emailTV.setText(personEmail);
            if (personPhoto!=null){
                Glide.with(this).load(personPhoto).into(photoIV);
            }

        }else {
            HashMap<String, String> user = sessionManager.getuserDetail();
            String Snama = user.get(sessionManager.SP_NAME);
            nameTV.setText(Snama);
            HashMap<String, String> email = sessionManager.getEmail();
            String Semail = email.get(sessionManager.SP_EMAIL);
            emailTV.setText(Semail);
        }

        login_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LogInActivity.class);
                startActivity(intent);
            }
        });

        button_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.saveSPBoolean(SessionManager.SP_SUDAH_LOGIN, false);
                sessionManager.saveSPInt(String.valueOf(SessionManager.SP_ID), 0);
                sessionManager.saveSPString(SessionManager.SP_NAME, null);
                sessionManager.saveSPString(SessionManager.SP_TAG, null);
                sessionManager.saveSPString(SessionManager.SP_ALAMAT, null);
                sessionManager.saveSPString(SessionManager.SP_NOMOR, null);
                signOut();
            }
        });

        if (sessionManager.getSPSudahLogin()){
            HashMap<String, String> talamat = sessionManager.getuserAlamat();
            Salamat = talamat.get(sessionManager.SP_ALAMAT);

            HashMap<String, String> tnomor = sessionManager.getuserNomor();
            Snomor = tnomor.get(sessionManager.SP_NOMOR);
            String defaul = "Belum di atur";
            if (Salamat.equals("")){

                alamat.setText(defaul);
            }else {
                alamat.setText(Salamat);
            }

            if (Snomor.equals("0")){
                nomor_telepon.setText(defaul);
            }else {
                nomor_telepon.setText(Snomor);
            }

            no_profile.setVisibility(View.GONE);
        }else{
            no_profile.setVisibility(View.VISIBLE);
            profile_view.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> talamat = sessionManager.getuserAlamat();
        Salamat = talamat.get(sessionManager.SP_ALAMAT);
        String defaul = "Belum di atur";
        if (Salamat.equals("")){

            alamat.setText(defaul);
        }else {
            alamat.setText(Salamat);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(),
                        task -> {
                            Intent intent = new Intent(getActivity(), LogInActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        });
    }
}