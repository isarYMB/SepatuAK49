package com.hp.ali.ecomerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.OtherAdapter.OtherProduct;
import com.hp.ali.ecomerceapp.OtherAdapter.OtherProductsAdapter;
import com.hp.ali.ecomerceapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Other extends AppCompatActivity {
    RecyclerView randomRecyclerView;
    ImageView imgBack;
    List<OtherProduct> productList;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    OtherProductsAdapter adapter;
    LottieAnimationView loadingOther;
    SearchView searchProduk;
    View layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        randomRecyclerView = findViewById(R.id.randomRecyclerView);
        productList = new ArrayList<>();
        loadingOther = findViewById(R.id.loadingOther);
        imgBack = findViewById(R.id.img_back);
        searchProduk = findViewById(R.id.search_produk);
        layout= findViewById(R.id.layoutOther);

        String cariFokus;
        if (savedInstanceState != null)
            cariFokus= (String) savedInstanceState.getSerializable("fokus");
        else
            cariFokus = getIntent().getExtras().getString("fokus");

        if (cariFokus==null){
            layout.requestFocus();
        }else {
            searchProduk.setFocusable(false);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchProduk.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productList.clear();
                searchProduk(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("Air Jordan One Mid")){
                    productList.clear();
                    loadProduk();
                }
                return true;
            }
        });

        searchProduk.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(Other.this, "close", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        loadProduk();
    }

    public void searchProduk (String newText){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_SEARCH_URL+newText,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resp = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            if (resp.equals("true")) {
                                //randomRecyclerView.setVisibility(View.VISIBLE);
                                JSONArray array = new JSONArray(data);
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    //adding the product to product list
                                    productList.add(new OtherProduct(
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
                                //creating adapter object and setting it to recyclerview
                                loadingOther.setVisibility(View.INVISIBLE);
                                randomRecyclerView.setVisibility(View.VISIBLE);
                                adapter = new OtherProductsAdapter(Other.this, productList);
                                randomRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
                                randomRecyclerView.setAdapter(adapter);
                            }else {
                                Toast.makeText(Other.this, "Sepatu yang anda cari tidak ada", Toast.LENGTH_SHORT).show();
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
                params.put("search", newText);
                return params;
            }
        };
        VolleyConnection.getInstance(getApplication()).addToRequestQue(stringRequest);
    }

    public void loadProduk (){
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
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject product = array.getJSONObject(i);

                                    //adding the product to product list
                                    productList.add(new OtherProduct(
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
                                //creating adapter object and setting it to recyclerview
                                loadingOther.setVisibility(View.INVISIBLE);
                                randomRecyclerView.setVisibility(View.VISIBLE);
                                adapter = new OtherProductsAdapter(Other.this, productList);
                                randomRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
                                randomRecyclerView.setAdapter(adapter);
                            }else {
                                loadingOther.setVisibility(View.VISIBLE);
                                randomRecyclerView.setVisibility(View.INVISIBLE);
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
        VolleyConnection.getInstance(getApplication()).addToRequestQue(stringRequest);
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) Other.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
