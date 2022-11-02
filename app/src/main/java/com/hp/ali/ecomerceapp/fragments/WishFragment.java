package com.hp.ali.ecomerceapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.hp.ali.ecomerceapp.ChartAdapter.CartProductAdapter;
import com.hp.ali.ecomerceapp.ChartAdapter.ChartProduct;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.PesananAdapter.PesananProduct;
import com.hp.ali.ecomerceapp.PesananAdapter.PesananProductsAdapter;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.CartActivity;
import com.hp.ali.ecomerceapp.activities.CheckoutActivity;
import com.hp.ali.ecomerceapp.activities.MainActivity;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.hp.ali.ecomerceapp.models.MyListData;
import com.hp.ali.ecomerceapp.models.Product;
import com.hp.ali.ecomerceapp.models.ProductsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static maes.tech.intentanim.CustomIntent.customType;


public class WishFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayout no_order;
    List<ChartProduct> productList;
    SessionManager sessionManager;
    int sid,id;
    String idProduk,merek,brand,harga,gambar,jumlah_pesanan;
    Context context;
    LottieAnimationView no_orderlotti;
    SwipeRefreshLayout swipeRefreshLayout;
    CartProductAdapter adapter;
    public WishFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_wish, container, false);
        recyclerView =  v.findViewById(R.id.recyclerView);
        no_order =  v.findViewById(R.id.no_order);
        no_orderlotti = v.findViewById(R.id.no_order_lotti);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        productList.clear();
                        recyclerView.setAdapter(adapter);
                        loadAwal(sid);
                    }
                },2000);

            }
        });

        ((MainActivity)getActivity()).updateStatusBarColor("#F5F5F8");

        //Mengubah warna status bar ke dark
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ((MainActivity)getActivity()).top_toolbar.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
        sid = id.get(sessionManager.SP_ID);
        //initializing the productlist
        if (sid!=0){
            productList.clear();
            loadAwal(sid);
        }
        return v;
    }

    public void loadAwal (int nomor){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_KERANJANG_URL+nomor,
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
                                    int idPesanan = Integer.parseInt(product.getString("ID"));
                                    idProduk = product.getString("ID_PRODUK");
                                    String ukuran = product.getString("UKURAN_SEPATU");
                                    loadProduk(idPesanan,idProduk,ukuran);
                                }
                                no_order.setVisibility(View.INVISIBLE);
                                no_orderlotti.setVisibility(View.INVISIBLE);
                            }else {
                                no_order.setVisibility(View.VISIBLE);
                                no_orderlotti.setVisibility(View.VISIBLE);
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
        VolleyConnection.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    public void loadProduk(int idPesanan,String nomor,String ukuran){
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

                                    //adding the product to product list
                                    productList.add(new ChartProduct(
                                            idPesanan,
                                            product.getInt("ID"),
                                            product.getString("MEREK"),
                                            product.getString("BRAND"),
                                            product.getString("DESKRIPSI"),
                                            product.getInt("HARGA"),
                                            product.getInt("DISKON"),
                                            product.getString("GAMBAR"),
                                            ukuran
                                    ));
                                }
                                //creating adapter object and setting it to recyclerview
                                adapter = new CartProductAdapter(getContext(), productList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(adapter);
                            }else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
        VolleyConnection.getInstance(getContext()).addToRequestQue(stringRequest);
    }

}