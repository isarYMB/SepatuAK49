package com.hp.ali.ecomerceapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.CheckoutActivity;
import com.hp.ali.ecomerceapp.activities.KebijakanPrivasi;
import com.hp.ali.ecomerceapp.activities.MainActivity;
import com.hp.ali.ecomerceapp.activities.Other;
import com.hp.ali.ecomerceapp.activities.ProductDetail;
import com.hp.ali.ecomerceapp.models.MyListData;
import com.hp.ali.ecomerceapp.models.Product;
import com.hp.ali.ecomerceapp.models.ProductsAdapter;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class HomeFragment extends Fragment {
    RecyclerView featuredRecyclerView;
    TextView toOther;
    ImageView imageView2;
    LottieAnimationView loadinglist,noconnection;
    List<Product> productList;
    LinearLayout layoutShow,noInternet;
    CarouselView carouselView;
    ProductsAdapter adapter;
    SessionManager sessionManager;
    String url1 = "http://192.168.1.8/ECOMMERSE/image/banner/banner_1.png";
    String url2 = "http://192.168.1.8/ECOMMERSE/image/banner/banner_2.png";
    String url3 = "http://192.168.1.8/ECOMMERSE/image/banner/banner_3.png";

    String[] sampleImages = {
            url1,url2,url3
    };

    public HomeFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).top_toolbar.setVisibility(View.GONE);
        ((MainActivity)getActivity()).updateStatusBarColor("#2D2942");
        productList = new ArrayList<>();
        toOther = view.findViewById(R.id.other);
        imageView2 = view.findViewById(R.id.imageView2);
        loadinglist = view.findViewById(R.id.loadinglist);
        featuredRecyclerView = view.findViewById(R.id.recyclerView_home);
        layoutShow = view.findViewById(R.id.layoutShow);
        noconnection = view.findViewById(R.id.noInternet);
        sessionManager = new SessionManager(getContext());

        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        toOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Other.class)
                        .putExtra("fokus","0");
                startActivity(i);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Other.class)
                        .putExtra("fokus","0");
                startActivity(i);
            }
        });

        //Mengembalikan warna status bar ke Light
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        loadProduk();
        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getActivity())
                    .load(sampleImages[position])
                    .into(imageView);
        }
    };

    public void loadProduk (){
        if (checkNetworkConnection()){
            noconnection.setVisibility(View.INVISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, DbContract.SERVER_PRODUK_URL,
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
                                    for (int i = 0; i < 7; i++) {
                                        //getting product object from json array
                                        JSONObject product = array.getJSONObject(i);
                                        productList.add(new Product(
                                                product.getInt("ID"),
                                                product.getString("MEREK"),
                                                product.getString("BRAND"),
                                                product.getString("DESKRIPSI"),
                                                product.getInt("HARGA"),
                                                product.getInt("DISKON"),
                                                product.getString("GAMBAR"),
                                                product.getString("lensUUID")
                                        ));
                                    }
                                    loadinglist.setVisibility(View.INVISIBLE);
                                    adapter = new ProductsAdapter(getContext(), productList);
                                    featuredRecyclerView.setAdapter(adapter);
                                }else {
                                    loadinglist.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            VolleyConnection.getInstance(getActivity()).addToRequestQue(stringRequest);
        }else {
            layoutShow.setVisibility(View.INVISIBLE);
            noconnection.setVisibility(View.VISIBLE);
        }
    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}