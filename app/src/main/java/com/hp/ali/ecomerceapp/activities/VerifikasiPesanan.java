package com.hp.ali.ecomerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class VerifikasiPesanan extends AppCompatActivity {

    LinearLayout VerifikasiSekarang;
    ImageView verifikasiNanti;
    SessionManager sessionManager;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_pesanan);
        VerifikasiSekarang = findViewById(R.id.verifikasi_sekarang);
        verifikasiNanti = findViewById(R.id.verifikasi_nanti);
        sessionManager = new SessionManager(getApplication());
        Bundle bundle = getIntent().getExtras();
        String merek = bundle.getString("merek");
        String brand = bundle.getString("brand");
        String ukuran = bundle.getString("ukuran");
        String metodeBayar = bundle.getString("metodeBayar");
        int harga = bundle.getInt("harga");
        String hg = NumberFormat.getNumberInstance(Locale.US).format(harga);

        VerifikasiSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = sessionManager.getuserDetail();
                String Snama = user.get(sessionManager.SP_NAME);
                PackageManager packageManager = getApplication().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = null;
                try {
                    status =1;
                    url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                            + URLEncoder.encode("[Verifikasi Pesanan]"+"\n"+"Saya ingin melakukan verifikasi untuk pesanan saya "+"\n"+"\n"+"Atas Nama: "+Snama+"\n"+"Pesanan: "+merek+"("+brand+")"+"\n"+"Ukuran: "+ukuran+"\n"+"Total Pembayaran: Rp"+hg+"\n"+"Metode Pembayaran : "+metodeBayar, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }else {
                    Toast.makeText(VerifikasiPesanan.this, "Verifikasi tidak dapat dilakukan coba lagi nanti", Toast.LENGTH_SHORT).show();
                }
            }
        });

        verifikasiNanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifikasiPesanan.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status==1){
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
    }
}