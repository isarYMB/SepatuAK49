package com.hp.ali.ecomerceapp.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.CheckoutActivity;
import com.hp.ali.ecomerceapp.activities.MainActivity;
import com.hp.ali.ecomerceapp.activities.ProductDetail;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.hp.ali.ecomerceapp.login.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{
    private Context mCtx;
    public static List<Product> productList;
    int idP;
    String statusDiskon;
    SessionManager sessionManager;
    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.home_top_item_layout, null);
        sessionManager = new SessionManager(mCtx);

        HashMap<String, String> cek = sessionManager.getMasaBerlakuDiskon();
        statusDiskon = cek.get(sessionManager.SP_DISKON);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getSPSudahLogin()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(productList.get(position).getMerek());
                    int id = productList.get(position).getId();
                    String merk = productList.get(position).getMerek();
                    String brnd = productList.get(position).getBrand();
                    int harg = productList.get(position).getHarga();
                    int disk = productList.get(position).getDiskon();
                    String gbr = productList.get(position).getGambar();
                    String desc = productList.get(position).getDeskripsi();
                    String lensUUID = productList.get(position).getLensUUID();
                    Intent i = new Intent(mCtx, ProductDetail.class)
                            .putExtra("position", position)
                            .putExtra("id", id)
                            .putExtra("merek", merk)
                            .putExtra("brand", brnd)
                            .putExtra("harga", harg)
                            .putExtra("diskon", disk)
                            .putExtra("gambar", gbr)
                            .putExtra("deskripsi", desc)
                            .putExtra("lens", lensUUID);
                    mCtx.startActivity(i);
                }else {
                    mCtx.startActivity(new Intent(mCtx, LogInActivity.class));
                }
            }
        });

        //loading the image
        idP = product.getId();
        int hrg = product.getHarga();
        int diskon = product.getDiskon();
        holder.merek.setText(product.getMerek());
        holder.brand.setText(product.getBrand());
        //holder.deskripsi.setText(product.getDeskripsi());


        int disc = hrg - diskon;

        String Sdiskon = NumberFormat.getNumberInstance(Locale.US).format(disc);
        String hg = NumberFormat.getNumberInstance(Locale.US).format(hrg);
        holder.hargaawal.setText("Rp"+hg);
        holder.harga.setText("Rp"+Sdiskon);

        Glide.with(mCtx)
                .load(product.getGambar())
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView merek, brand, deskripsi, harga,hargaawal;
        ImageView gambar,keranjang;
        LinearLayout lyt_item;

        public ProductViewHolder(View itemView) {
            super(itemView);
            lyt_item = itemView.findViewById(R.id.lyt_item_home);
            merek = itemView.findViewById(R.id.tv_merek);
            brand = itemView.findViewById(R.id.tv_brand);
            //deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            harga = itemView.findViewById(R.id.tv_harga);
            gambar = itemView.findViewById(R.id.gambar_sepatu);
            hargaawal = itemView.findViewById(R.id.tv_potongan_harga);
            hargaawal.setPaintFlags(harga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

}
