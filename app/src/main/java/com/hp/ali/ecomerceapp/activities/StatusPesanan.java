package com.hp.ali.ecomerceapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.developer.kalert.KAlertDialog;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.models.lottiedialogfragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class StatusPesanan extends AppCompatActivity {

    ImageView img_status,img_back;
    SessionManager sessionManager;
    TextView status_1, status_2, status_3, status_4,tv_total_pesanan;
    Button btn_batalkan;
    int sudahHapus;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pesanan);
        img_status = findViewById(R.id.img_status);
        img_back = findViewById(R.id.img_back);
        status_1 = findViewById(R.id.status_1);
        status_2 = findViewById(R.id.status_2);
        status_3 = findViewById(R.id.status_3);
        status_4 = findViewById(R.id.status_4);
        sessionManager = new SessionManager(getApplication());
        tv_total_pesanan = findViewById(R.id.tv_total_pesanan);
        btn_batalkan = findViewById(R.id.btn_batalkan_pesanan);

        Bundle bundle = getIntent().getExtras();
        String stitle = bundle.getString("status");
        String total = bundle.getString("total");
        String id_pesanan = bundle.getString("id");
        String merek = bundle.getString("merek");
        String brand = bundle.getString("brand");
        String metodeBayar = bundle.getString("metodeBayar");
        String ukuran = bundle.getString("ukuran");

        tv_total_pesanan.setText(total);

        if (stitle.equals("1")){
            img_status.setImageResource(R.drawable.ic_status1);
            status_1.setTextColor(getColor(R.color.colorStatus));
        }else if (stitle.equals("2")){
            img_status.setImageResource(R.drawable.ic_status2);
            status_1.setTextColor(getColor(R.color.colorStatus));
            status_2.setTextColor(getColor(R.color.colorStatus));
        }else if (stitle.equals("3")){
            img_status.setImageResource(R.drawable.ic_status3);
            status_1.setTextColor(getColor(R.color.colorStatus));
            status_2.setTextColor(getColor(R.color.colorStatus));
            status_3.setTextColor(getColor(R.color.colorStatus));
        }else {
            img_status.setImageResource(R.drawable.ic_status4);
            status_1.setTextColor(getColor(R.color.colorStatus));
            status_2.setTextColor(getColor(R.color.colorStatus));
            status_3.setTextColor(getColor(R.color.colorStatus));
            status_4.setTextColor(getColor(R.color.colorStatus));
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_batalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stitle.equals("1") || stitle.equals("2")){
                    KAlertDialog pDialog = new KAlertDialog(StatusPesanan.this,KAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("Batalkan Pesanan?");
                    pDialog.setContentText("Mohon konfirmasi kepada kami jika anda ingin membatalkan pesanan, agar barang anda tidak kami kirimkan. Jika pesanan sedang dalam pengiriman ke alamat tujuan, anda tidak dapat membatalkan pesanan ini lagi."+"\n"+"\n");
                    pDialog.setCancelText("Konfirmasi");
                    pDialog.setConfirmText("Batal");
                    pDialog.show();
                    pDialog.setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            hapusPesanan(id_pesanan);
                            pDialog.dismissWithAnimation();
                            sudahHapus =1;

                            HashMap<String, String> user = sessionManager.getuserDetail();
                            String Snama = user.get(sessionManager.SP_NAME);
                            PackageManager packageManager = getApplication().getPackageManager();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            String url = null;
                            try {
                                url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                                        + URLEncoder.encode("[Pesanan Dibatalkan]"+"\n"+"\n"+"Saya melakukan pembatalan pesanan"+"\n"+"\n"+"Atas Nama: "+Snama+"\n"+"Pesanan: "+merek+"("+brand+")"+"\n"+"Ukuran: "+ukuran+"\n"+"Total Pembayaran: "+total+"\n"+"Metode Pembayaran : "+metodeBayar, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                startActivity(i);
                            }else {
                                Toast.makeText(StatusPesanan.this, "Tidak dapat melakukan konfirmasi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            pDialog.cancel();
                        }
                    });
                }else {
                    Toast.makeText(StatusPesanan.this, "Pesanan yang telah dikirimkan tidak dapat dibatalkan", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void hapusPesanan(String delId) {
//        lottiedialogfragment = new lottiedialogfragment(mCtx.getApplicationContext());
//        lottiedialogfragment.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETEPESANAN_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = "Pesanan Dibatalkan";
                            if (resp.equals("true")) {
                                Toast.makeText(StatusPesanan.this, message, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(StatusPesanan.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("ID", delId);
                return params;
            }
        };

        VolleyConnection.getInstance(this).addToRequestQue(stringRequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                lottiedialogfragment.cancel();
            }
        }, 4000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sudahHapus==1){
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
    }
}