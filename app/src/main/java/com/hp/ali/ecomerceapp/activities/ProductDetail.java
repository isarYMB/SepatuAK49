package com.hp.ali.ecomerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.developer.kalert.KAlertDialog;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.adapterDetail.GalleryAdapter;
import com.hp.ali.ecomerceapp.adapterDetail.ListImages;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.hp.ali.ecomerceapp.models.lottiedialogfragment;
import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CenterScrollListener;
import com.snapchat.kit.sdk.SnapCreative;
import com.snapchat.kit.sdk.creative.api.SnapCreativeKitApi;
import com.snapchat.kit.sdk.creative.media.SnapLensLaunchData;
import com.snapchat.kit.sdk.creative.models.SnapLensContent;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    ImageView img_back,img_wa;
    TextView brand,merek,deskripsi,harga,diskon,hargaawal;
    Button btn_beli,btn_ukaki;
    LinearLayout btn_coba,btn_tambah_keranjang;
    int ukuran = 0;
    int position,Idiskon;
    private ProgressDialog progressDialog;
    SessionManager sessionManager;
    List<ListImages>images;
    GalleryAdapter adapter;
    RecyclerView recyclerView;
    String Stotal;
    int idUser;
    int Iid,Iharga,total;
    String Smerek,Sbrad,Sdeskripsi,Sgambar,ShowGambar,lensUUID;
    lottiedialogfragment lottiedialogfragment;
    LottieAnimationView swipeGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        btn_ukaki = findViewById(R.id.btn_ukaki);
        img_back = findViewById(R.id.img_back);
        btn_beli = findViewById(R.id.btn_beli);
        swipeGambar= findViewById(R.id.swipeGambar);
        merek = findViewById(R.id.tv_title_detail);
        brand = findViewById(R.id.tv_merek_detail);
        deskripsi = findViewById(R.id.tv_deskripsi_detail);
        harga = findViewById(R.id.tv_harga_detail);
        btn_coba = findViewById(R.id.btn_cobaSepatu);
        diskon = findViewById(R.id.tv_diskon);
        hargaawal = findViewById(R.id.tv_harga_awal);
        img_wa = findViewById(R.id.chatWA);
        btn_tambah_keranjang = findViewById(R.id.btn_tambah_keranjang);
        progressDialog = new ProgressDialog(ProductDetail.this);
        sessionManager = new SessionManager(ProductDetail.this);
        lottiedialogfragment = new lottiedialogfragment(this);

        // Tampilkan produk
        Bundle bundle = getIntent().getExtras();
        Iid = bundle.getInt("id");
        Smerek =bundle.getString("merek");
        Sbrad = bundle.getString("brand");
        Sdeskripsi = bundle.getString("deskripsi");
        Iharga = bundle.getInt("harga");
        Idiskon = bundle.getInt("diskon");
        Sgambar = bundle.getString("gambar");
        lensUUID = bundle.getString("lens");


        ShowGambar = Sgambar.replace(Sgambar.substring(Sgambar.length()-4), "");
        String hg = NumberFormat.getNumberInstance(Locale.US).format(Iharga);

        total = Iharga-Idiskon;
        Stotal = NumberFormat.getNumberInstance(Locale.US).format(total);
        String disc = NumberFormat.getNumberInstance(Locale.US).format(Idiskon);

        merek.setText(Smerek);
        brand.setText(Sbrad);
        deskripsi.setText(Sdeskripsi);
        harga.setText("Rp"+Stotal);
        hargaawal.setText("Rp"+hg);
        diskon.setText("Rp"+disc);
        HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
        idUser = id.get(sessionManager.SP_ID);

        btn_tambah_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getSPSudahLogin()){

                    if (idUser!=0){
                        if (ukuran !=0){
                            tambahKeranjang(String.valueOf(Iid));
                        }else {
                            Toast.makeText(ProductDetail.this, "Mohon pilih ukuran terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ProductDetail.this, "Harap login terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Intent intent = new Intent(ProductDetail.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ukuran !=0){
                    Intent i = new Intent(ProductDetail.this, CheckoutActivity.class)
                            .putExtra("position",position)
                            .putExtra("id",Iid)
                            .putExtra("merek",Smerek)
                            .putExtra("brand",Sbrad)
                            .putExtra("harga",total)
                            .putExtra("gambar",Sgambar)
                            .putExtra("ukuran",ukuran);
                    startActivity(i);
                }else {
                    Toast.makeText(ProductDetail.this, "Mohon pilih ukuran terlebih dahulu", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_ukaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDetail.this, SwipeUkaki.class);
                startActivity(i);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        SnapLensLaunchData launchData = new SnapLensLaunchData.Builder()
                .build();
        SnapLensContent snapLensContent = SnapLensContent.createSnapLensContent(lensUUID, launchData);
        SnapCreativeKitApi snapCreativeKitApi = SnapCreative.getApi(this);

        btn_coba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lensUUID.equals("")){
                    KAlertDialog pDialog = new KAlertDialog(ProductDetail.this);
                    pDialog.setTitleText("Coming Soon");
                    pDialog.setContentText("Augmented Reality pada sepatu ini akan segera launching. Yuk pesan sepatu ini sekarang.");
                    pDialog.setConfirmText("  OKE  ");
                    pDialog.show();
                }else {
                    snapCreativeKitApi.send(snapLensContent);
                }
            }
        });

        img_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String mrk = merek.getText().toString();
                    String bran = brand.getText().toString();
                    String hrg = harga.getText().toString();
                    String pesan = "Apakah Produk Masih Ada ?";
                    PackageManager packageManager = getApplication().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                            + URLEncoder.encode("[Konfirmasi Produk]"+"\n"+"\n"+mrk+"("+bran+")"+"\n"+hrg+"\n"+"\n"+"\n"+pesan, "UTF-8");
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
        images = new ArrayList<>();
        setData();
        setAdapter();
    }
    private void tambahKeranjang(String idproduk) {
        lottiedialogfragment.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_TAMBAHKERANJANG_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (resp.equals("true")){
                                //Toast.makeText(ProductDetail.this, message, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ProductDetail.this, resp, Toast.LENGTH_SHORT).show();
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
                params.put("ID_USER", String.valueOf(sid));
                params.put("ID_PRODUK", idproduk);
                params.put("UKURAN_SEPATU", String.valueOf(ukuran));
                return params;
            }
        };

        VolleyConnection.getInstance(ProductDetail.this).addToRequestQue(stringRequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottiedialogfragment.cancel();
                Toast.makeText(ProductDetail.this, "Sepatu ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
            }
        },2000);

    }
    public void UkuranSepatu(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rb_30:
                if (checked)
                    // Pirates are the best
                    ukuran = 30;
                break;
            case R.id.rb_31:
                if (checked)
                    // Pirates are the best
                    ukuran = 31;
                break;
            case R.id.rb_32:
                if (checked)
                    // Pirates are the best
                    ukuran = 32;
                break;
            case R.id.rb_33:
                if (checked)
                    // Pirates are the best
                    ukuran = 33;
                break;
            case R.id.rb_34:
                if (checked)
                    // Pirates are the best
                    ukuran = 34;
                break;
            case R.id.rb_35:
                if (checked)
                    // Pirates are the best
                    ukuran = 35;
                break;
            case R.id.rb_36:
                if (checked)
                    // Pirates are the best
                    ukuran = 36;
                break;
            case R.id.rb_37:
                if (checked)
                    // Ninjas rule
                    ukuran = 37;
                break;
            case R.id.rb_38:
                if (checked)
                    // Pirates are the best
                    ukuran = 38;
                break;
            case R.id.rb_39:
                if (checked)
                    // Ninjas rule
                    ukuran = 39;
                break;
            case R.id.rb_40:
                if (checked)
                    // Pirates are the best
                    ukuran = 40;
                break;
            case R.id.rb_41:
                if (checked)
                    // Ninjas rule
                    ukuran = 41;
                break;
            case R.id.rb_42:
                if (checked)
                    // Pirates are the best
                    ukuran = 42;
                break;
            case R.id.rb_43:
                if (checked)
                    // Pirates are the best
                    ukuran = 43;
                break;
            case R.id.rb_44:
                if (checked)
                    // Pirates are the best
                    ukuran = 44;
                break;
            case R.id.rb_45:
                if (checked)
                    // Pirates are the best
                    ukuran = 45;
                break;
            case R.id.rb_46:
                if (checked)
                    // Pirates are the best
                    ukuran = 46;
                break;
        }
    }

    private void setAdapter() {
        recyclerView = findViewById(R.id.list_horizontal);

        recyclerView.setHasFixedSize(true);

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);

        adapter = new GalleryAdapter(this, images);

        recyclerView.setAdapter(adapter);

        //layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(0);
        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        // enable center post scrolling

        recyclerView.addOnScrollListener(new CenterScrollListener());


    }

    private void setData(){
        for (int i =1 ; i<19;i++){
            images.add(new ListImages(ShowGambar+i+".png"));
        }

    }
}