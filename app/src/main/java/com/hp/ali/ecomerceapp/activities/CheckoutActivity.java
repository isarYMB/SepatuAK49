package com.hp.ali.ecomerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.api.ApiService;
import com.hp.ali.ecomerceapp.api.ApiUrl;
import com.hp.ali.ecomerceapp.fragments.WishFragment;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.hp.ali.ecomerceapp.model.cost.ItemCost;
import com.hp.ali.ecomerceapp.models.MyListData;
import com.hp.ali.ecomerceapp.models.ProductsAdapter;
import com.hp.ali.ecomerceapp.models.lottiedialogfragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class
CheckoutActivity extends AppCompatActivity {
    ImageView img_back, btn_kurang, btn_tambah,img_editAddress, img_produk;
    TextView alamat_checkout,nomor_checkout,rp;
    TextView tv_jumlah, tv_brand,tv_merek, tv_total,tv_ongkir;
    Integer jumlah = 1;
    int total,position,id_produk,Iongkir,id_Keranjang;
    Button btn_checkout;
    RadioButton rbt_cod,rbt_bank;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    private String sTag,estimasi;
    private String Metode_Pembayaran, Ukuran;
    lottiedialogfragment lottiedialogfragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        img_back = findViewById(R.id.img_back);
        btn_kurang = findViewById(R.id.btn_kurang);
        btn_tambah = findViewById(R.id.btn_tambah);
        tv_jumlah = findViewById(R.id.tv_jumlah);
        img_editAddress = findViewById(R.id.edit_alamat);
        img_produk = findViewById(R.id.img_produk_checkout);
        tv_merek = findViewById(R.id.tv_title_checkout);
        tv_brand = findViewById(R.id.tv_merek_checkout);
        tv_total = findViewById(R.id.tv_total);
        tv_ongkir = findViewById(R.id.tv_ongkir);
        btn_checkout = findViewById(R.id.btn_checkout);
        rbt_cod = findViewById(R.id.rbt_cod);
        rbt_bank = findViewById(R.id.rbt_bank);
        rp = findViewById(R.id.rp);

        nomor_checkout = findViewById(R.id.nomor_checkout);
        alamat_checkout = findViewById(R.id.alamat_checkout);

        progressDialog = new ProgressDialog(CheckoutActivity.this);
        sessionManager = new SessionManager(CheckoutActivity.this);
        lottiedialogfragment = new lottiedialogfragment(this);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah >1){
                    jumlah -= 1;
                    String jml = jumlah.toString();
                    tv_jumlah.setText(jml);
                    hitungTotal();
                }
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah +=1;
                String jml = jumlah.toString();
                tv_jumlah.setText(jml);
                hitungTotal();
            }
        });

        img_editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getSPSudahLogin()){
                    RadioGroup x=findViewById(R.id.rb_group);
                    x.clearCheck();
                    Intent intent = new Intent(CheckoutActivity.this, EditAlamat.class);
                    startActivity(intent);

                }else {
                    RadioGroup x=findViewById(R.id.rb_group);
                    x.clearCheck();
                    Intent intent = new Intent(CheckoutActivity.this, LogInActivity.class);
                    startActivity(intent);
                }

            }
        });
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ongkir.getText().toString();
                if (!tv_ongkir.equals("0")){
                    lottiedialogfragment.show();
                    checkOut();
                }else {
                    Toast.makeText(CheckoutActivity.this, "Harap memilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getPorduct();
        hitungTotal();
    }
    int sharga;
    protected void onStart() {
        HashMap<String, String> tag = sessionManager.getuserTag();
        sTag = tag.get(sessionManager.SP_TAG);

        HashMap<String, String> talamat = sessionManager.getuserAlamat();
        String Salamat = talamat.get(sessionManager.SP_ALAMAT);

        HashMap<String, String> tnomor = sessionManager.getuserNomor();
        String Snomor = tnomor.get(sessionManager.SP_NOMOR);

        String defaul = "Alamat belum diatur";
        String defaulNomor = "Nomor telepone belum diatur";
        if (Salamat.equals("")){
            alamat_checkout.setText(defaul);
        }else {
            alamat_checkout.setText(Salamat);
        }

        if (Snomor.equals("0")){
            nomor_checkout.setText(defaulNomor);
        }else {
            nomor_checkout.setText(Snomor);
        }

        super.onStart();
    }

    private void getPorduct() {
        Bundle bundle = getIntent().getExtras();
        id_Keranjang = bundle.getInt("idKeranjang");
        id_produk = bundle.getInt("id");
        tv_merek.setText(bundle.getString("merek"));
        tv_brand.setText(bundle.getString("brand"));
        Ukuran = String.valueOf(bundle.getInt("ukuran"));
        sharga = bundle.getInt("harga");
        Glide.with(getBaseContext())
                .load(bundle.getString("gambar"))
                .into(img_produk);

    }
    private void hitungTotal() {

        int jumlah = Integer.parseInt(tv_jumlah.getText().toString());
        total = sharga * jumlah + Iongkir;
        String stotal = NumberFormat.getNumberInstance(Locale.US).format(total);
        tv_total.setText("Rp"+stotal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_ongkir.setText("0");
    }

    @SuppressLint("WrongViewCast")
    public void MetodePembayaran(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rbt_cod:
                if (checked) {
                    if (sessionManager.getSPSudahLogin()) {
                        if (sTag.equals("Belum di atur")) {
                            Toast.makeText(CheckoutActivity.this, "Mohon mengatur alamat terlebih dahulu", Toast.LENGTH_SHORT).show();
                            RadioGroup x=findViewById(R.id.rb_group);
                            x.clearCheck();
                            Intent intent = new Intent(this, EditAlamat.class);
                            startActivity(intent);
                        } else {
                            getCost("254", sTag, "2", "jne");
                            Metode_Pembayaran = "Cash On Delivery";
                        }

                    } else {
                        RadioGroup x=findViewById(R.id.rb_group);
                        x.clearCheck();
                        Intent intent = new Intent(this, LogInActivity.class);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.rbt_bank:
                if (checked)
                    if (sessionManager.getSPSudahLogin()){
                        if (sTag.equals("Belum di atur")){
                            Toast.makeText(this, "Mohon mengatur alamat terlebih dahulu", Toast.LENGTH_SHORT).show();
                            RadioGroup x=findViewById(R.id.rb_group);
                            x.clearCheck();
                            Intent intent = new Intent(this, EditAlamat.class);
                            startActivity(intent);
                        }else {
                            getCost("254",sTag,"2","jne");
                            Metode_Pembayaran = "Transfer Bank";
                        }

                    }else{
                        RadioGroup x=findViewById(R.id.rb_group);
                        x.clearCheck();
                        Intent intent = new Intent(this, LogInActivity.class);
                        startActivity(intent);
                    }
                break;
        }
    }
    public void getCost(String origin,
                        String destination,
                        String weight,
                        String courier) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ItemCost> call = service.getCost(
                "769116a1c62f16076ed1f5b8cfec0acd",
                origin,
                destination,
                weight,
                courier
        );
        lottiedialogfragment.show();
        call.enqueue(new Callback<ItemCost>() {

            @Override
            public void onResponse(Call<ItemCost> call, retrofit2.Response<ItemCost> response) {
                Log.v("wow", "json : " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    int statusCode = response.body().getRajaongkir().getStatus().getCode();

                    if (statusCode == 200){
                        lottiedialogfragment.cancel();
                        if (destination.equals(origin)){
                            Iongkir =0;
                            rp.setVisibility(View.GONE);
                            tv_ongkir.setText("Gratis Ongkir");
                        }else {
                            Iongkir = Integer.parseInt(response.body().getRajaongkir().getResults().get(0).getCosts().get(0).getCost().get(0).getValue().toString());
                            String hg = NumberFormat.getNumberInstance(Locale.US).format(Iongkir);
                            tv_ongkir.setText(hg);
                        }
                        estimasi =response.body().getRajaongkir().getResults().get(0).getCosts().get(0).getCost().get(0).getEtd()+" Hari";
                        hitungTotal();
                    } else {
                        String message = response.body().getRajaongkir().getStatus().getDescription();
                        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(CheckoutActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemCost> call, Throwable t) {
                lottiedialogfragment.dismiss();
                Toast.makeText(CheckoutActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkOut(){
        if (checkNetworkConnection()){
            if (sessionManager.getSPSudahLogin()){

                lottiedialogfragment.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_PESAN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String resp = jsonObject.getString("success");
                                    if (resp.equals("true")){
                                        Toast.makeText(CheckoutActivity.this, "Pesanan dimasukkan kedalam daftar pesanan", Toast.LENGTH_SHORT).show();
                                        if (id_Keranjang!=0){
                                            deleteCart(id_Keranjang);
                                        }
                                        Intent i = new Intent(CheckoutActivity.this, VerifikasiPesanan.class)
                                                .putExtra("position",position)
                                                .putExtra("merek",tv_merek.getText().toString())
                                                .putExtra("brand",tv_brand.getText().toString())
                                                .putExtra("harga",total)
                                                .putExtra("ukuran",Ukuran)
                                                .putExtra("metodeBayar", Metode_Pembayaran);
                                        startActivity(i);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Gagal memesan coba lagi nanti", Toast.LENGTH_SHORT).show();
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
                        HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
                        int sid = id.get(sessionManager.SP_ID);
                        params.put("ID_USER", String.valueOf(sid));
                        params.put("ID_PRODUK", String.valueOf(id_produk));
                        params.put("UKURAN_SEPATU",Ukuran);
                        params.put("JUMLAH_PESANAN",tv_jumlah.getText().toString());
                        params.put("ONGKIR",tv_ongkir.getText().toString());
                        params.put("METODE_PEMBAYARAN",Metode_Pembayaran);
                        params.put("ESTIMASI",estimasi);
                        params.put("TOTAL_PESANAN",tv_total.getText().toString());
                        return params;
                    }
                };

                VolleyConnection.getInstance(CheckoutActivity.this).addToRequestQue(stringRequest);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottiedialogfragment.cancel();
                    }
                },4000);
            }else {
                Toast.makeText(this, "Harap Login Terlebih dahulu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
            }
        }else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteCart(int delId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_DELETECART_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (resp.equals("true")) {

                            }else {

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
                params.put("ID", String.valueOf(delId));
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
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}