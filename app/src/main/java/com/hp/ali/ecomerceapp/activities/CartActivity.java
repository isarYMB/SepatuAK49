package com.hp.ali.ecomerceapp.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hp.ali.ecomerceapp.ChartAdapter.CartProductAdapter;
import com.hp.ali.ecomerceapp.ChartAdapter.ChartProduct;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.PesananAdapter.PesananProduct;
import com.hp.ali.ecomerceapp.PesananAdapter.PesananProductsAdapter;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.models.MyListData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btn_checkout;
    List<PesananProduct> productList;
    SessionManager sessionManager;
    Context context;
    String idProduk;
    ImageView img_back;
    LottieAnimationView no_cart;
    LinearLayout noCartLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    String merek,brand,harga,gambar,jumlah_pesanan;
    PesananProductsAdapter adapter;
    int sid,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        btn_checkout =  findViewById(R.id.btn_checkout);
        recyclerView =  findViewById(R.id.recyclerView);
        productList = new ArrayList<>();
        no_cart = findViewById(R.id.no_order_lotti);
        noCartLayout = findViewById(R.id.noCartLayout);
        sessionManager = new SessionManager(CartActivity.this);
        HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
        sid = id.get(sessionManager.SP_ID);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        img_back = findViewById(R.id.img_back);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        productList.clear();
                        recyclerView.setAdapter(adapter);
                        if (sid!=0){
                            loadAwal(sid);
                        }
                    }
                },2000);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
        sid = id.get(sessionManager.SP_ID);
        if (sid!=0){
            productList.clear();
            loadAwal(sid);
        }
    }

    public void loadAwal (int nomor){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_CART_URL+nomor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            if (resp.equals("true")) {
                                JSONArray array = new JSONArray(data);
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {
                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);
                                    id = Integer.parseInt(product.getString("ID"));
                                    idProduk = product.getString("ID_PRODUK");
                                    String sstatus = product.getString("STATUS");
                                    String stotal_pesanan = product.getString("TOTAL_PESANAN");
                                    String sjumlah_pesanan = product.getString("JUMLAH_PESANAN");
                                    String sverisikasi = product.getString("VERIFIKASI");
                                    String metodeBayar = product.getString("METODE_PEMBAYARAN");
                                    String ukuran = product.getString("UKURAN_SEPATU");
                                    loadProduk(id,idProduk,sstatus,sjumlah_pesanan,stotal_pesanan,sverisikasi,metodeBayar,ukuran);
                                    no_cart.setVisibility(View.GONE);
                                    noCartLayout.setVisibility(View.GONE);
                                }

                            }else {
                                no_cart.setVisibility(View.VISIBLE);
                                noCartLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyConnection.getInstance(CartActivity.this).addToRequestQue(stringRequest);
    }

    public void loadProduk (int idP,String nomor,String sta,String jml,String tot,String verifikasi, String metodeBayar, String ukuran){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_CARTLIST_URL+nomor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            if (resp.equals("true")) {
                                JSONArray array = new JSONArray(data);
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {
                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);
                                    int Sid = Integer.parseInt(product.getString("ID"));
                                    merek = product.getString("MEREK");
                                    brand = product.getString("BRAND");
                                    harga = product.getString("HARGA");
                                    String diskon = product.getString("DISKON");
                                    String deskripsi = product.getString("DESKRIPSI");
                                    gambar = product.getString("GAMBAR");
                                    //adding the product to product list
                                    buatList(idP,merek,brand,harga,diskon,sta,deskripsi,tot,jml,gambar,verifikasi,metodeBayar,ukuran);
                                }
                                //creating adapter object and setting it to recyclerview
                            }else {
                                Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyConnection.getInstance(CartActivity.this).addToRequestQue(stringRequest);
    }

    private void buatList(int id, String merek, String brand, String harga,String diskon, String status,String deskripsi, String total_pesanan,String jumlah_pesanan, String gambar, String verifikasi, String metodebayar, String ukuran) {
        productList.add(new PesananProduct(id,
                merek,
                brand,
                harga,
                diskon,
                status,
                deskripsi,
                total_pesanan,
                jumlah_pesanan,
                gambar,
                verifikasi,
                metodebayar,
                ukuran
        ));
        adapter = new PesananProductsAdapter(CartActivity.this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this,LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}