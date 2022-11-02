package com.hp.ali.ecomerceapp.ChartAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.CartActivity;
import com.hp.ali.ecomerceapp.activities.CheckoutActivity;
import com.hp.ali.ecomerceapp.activities.EditAlamat;
import com.hp.ali.ecomerceapp.activities.ProductDetail;
import com.hp.ali.ecomerceapp.models.lottiedialogfragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ProductViewHolder>{
    private Context mCtx;
    public static List<ChartProduct> productList;
    final static String custom = "CartProductAdapter";
    lottiedialogfragment lottiedialogfragment;
    int idP,diskon;
    String statusDiskon;
    SessionManager sessionManager;

    public CartProductAdapter(Context mCtx, List<ChartProduct> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public CartProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.wish_item_layout, null);
        sessionManager = new SessionManager(mCtx);

        HashMap<String, String> cek = sessionManager.getMasaBerlakuDiskon();
        statusDiskon = cek.get(sessionManager.SP_DISKON);
        return new CartProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartProductAdapter.ProductViewHolder holder, int position) {
        ChartProduct product = productList.get(position);
        holder.btn_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(productList.get(position).getMerek());
                int idKeranjang = productList.get(position).getIdCart();
                int id = productList.get(position).getId();
                String merk = productList.get(position).getMerek();
                String brnd = productList.get(position).getBrand();
                int harg = productList.get(position).getHarga();
                int ukuran = Integer.parseInt(product.getUkuran());
                String gbr = productList.get(position).getGambar();
                Intent i = new Intent(mCtx, CheckoutActivity.class)
                        .putExtra("position",position)
                        .putExtra("idKeranjang",idKeranjang)
                        .putExtra("id",id)
                        .putExtra("merek",merk)
                        .putExtra("brand",brnd)
                        .putExtra("harga",harg-diskon)
                        .putExtra("gambar",gbr)
                        .putExtra("ukuran",ukuran);
                mCtx.startActivity(i);
            }
        });
        //loading the image
        holder.merek.setText(product.getMerek());
        holder.brand.setText(product.getBrand());
        diskon = product.getDiskon();

        int hrg = product.getHarga()-diskon;
        String hg = NumberFormat.getNumberInstance(Locale.US).format(hrg);
        holder.harga.setText("Rp"+hg);

        Glide.with(mCtx)
                .load(product.getGambar())
                .into(holder.gambar);

        holder.lyt_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ids = productList.get(position).getIdCart();
                KAlertDialog pDialog = new KAlertDialog(mCtx,KAlertDialog.WARNING_TYPE);
                pDialog.setContentText("Apakah anda yakin ingin menghapus daftar ini dari keranjang anda");
                pDialog.setConfirmText("  Ya  ");
                pDialog.setCancelText("Batal");
                pDialog.show();
                pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        deleteCart(ids);
                        pDialog.dismissWithAnimation();
                    }
                });
                pDialog.setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        pDialog.cancel();
                    }
                });

                return true;
            }
        });
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
                                //cuma contoh
                                Toast.makeText(mCtx.getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(mCtx.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

        VolleyConnection.getInstance(mCtx.getApplicationContext()).addToRequestQue(stringRequest);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                lottiedialogfragment.cancel();
            }
        }, 4000);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView merek, brand, harga;
        ImageView gambar;
        LinearLayout lyt_item;
        Button btn_checkOut;

        public ProductViewHolder(View itemView) {
            super(itemView);
            lyt_item = itemView.findViewById(R.id.beli_lagi);
            merek = itemView.findViewById(R.id.merek_Cart);
            brand = itemView.findViewById(R.id.brand_cart);
            harga = itemView.findViewById(R.id.harga_cart);
            gambar = itemView.findViewById(R.id.gambar_cart);
            btn_checkOut = itemView.findViewById(R.id.chekout_cart);
        }
    }
}
