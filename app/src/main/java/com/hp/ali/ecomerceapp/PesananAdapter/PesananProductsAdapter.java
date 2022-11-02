package com.hp.ali.ecomerceapp.PesananAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.ProductDetail;
import com.hp.ali.ecomerceapp.activities.StatusPesanan;
import com.hp.ali.ecomerceapp.activities.VerifikasiPesanan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PesananProductsAdapter extends RecyclerView.Adapter<PesananProductsAdapter.ProductViewHolder>{
    private Context mCtx;
    public static List<PesananProduct> productList;
    String id_pesanan;
    int diskon;
    String statusDiskon;
    SessionManager sessionManager;

    public PesananProductsAdapter(Context mCtx, List<PesananProduct> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public PesananProductsAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.pesanan_item_layout, null);
        sessionManager = new SessionManager(mCtx);
        HashMap<String, String> cek = sessionManager.getMasaBerlakuDiskon();
        statusDiskon = cek.get(sessionManager.SP_DISKON);
        return new PesananProductsAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PesananProductsAdapter.ProductViewHolder holder, int position) {
        PesananProduct product = productList.get(position);
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_pesanan = String.valueOf(productList.get(position).getId());
               Intent intent = new Intent(mCtx, StatusPesanan.class);
               intent.putExtra("status",productList.get(position).getStatus());
               intent.putExtra("total",productList.get(position).getTotal());
                intent.putExtra("merek",productList.get(position).getMerek());
                intent.putExtra("brand",productList.get(position).getBrand());
                intent.putExtra("metodeBayar",productList.get(position).getMetodebayar());
                intent.putExtra("ukuran",productList.get(position).getUkuran());
               intent.putExtra("id",id_pesanan);
               mCtx.startActivity(intent);
            }
        });
        holder.btnBeliLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> user = sessionManager.getuserDetail();
                String Snama = user.get(sessionManager.SP_NAME);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(productList.get(position).getMerek());
                String merk = productList.get(position).getMerek();
                String brnd = productList.get(position).getBrand();
                String total = productList.get(position).getTotal();
                String ukrn = productList.get(position).getUkuran();
                String metbayar = productList.get(position).getMetodebayar();

                PackageManager packageManager = mCtx.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String url = null;
                try {
                    url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                            + URLEncoder.encode("[Verifikasi Ulang Pesanan]"+"\n"+"Saya ingin melakukan verifikasi untuk pesanan saya "+"\n"+"\n"+"Atas nama : "+Snama+"\n"+"Pesanan : "+merk+"("+brnd+")"+"\n"+"Ukuran : "+ukrn+"\n"+"Harga : "+total+"\n"+"Metode Pembayaran : "+metbayar, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    mCtx.startActivity(i);
                }else {
                    Toast.makeText(v.getContext(), "Verifikasi tidak dapat dilakukan coba lagi nanti", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //loading the image
        holder.merek.setText(product.getMerek());
        holder.brand.setText(product.getBrand());
        diskon = Integer.parseInt(product.getDiskon());
        String st = product.getStatus();
        String ver = product.getVerifikasi();
        if (st.equals("1")){
            holder.status.setText("Pendataan");
        }else if (st.equals("2")){
            holder.status.setText("Packaging");
        }else if (st.equals("3")){
            holder.status.setText("Pengiriman");
        }else {
            holder.status.setText("Selesai");
        }

        if (ver.equals("1")){
            holder.terverifikasi.setVisibility(View.GONE);
            holder.btnBeliLagi.setVisibility(View.VISIBLE);

        }else {
            holder.terverifikasi.setText("Telah Diverifikasi");
            holder.btnBeliLagi.setVisibility(View.GONE);
        }

//        if (statusDiskon.equals("berlaku")){
//            diskon = 50000;
//        }else {
//            diskon = 20000;
//        }
        int hrg = Integer.parseInt(product.getHarga())-diskon;
        String hg = NumberFormat.getNumberInstance(Locale.US).format(hrg);
        holder.harga.setText("Rp"+hg);
        holder.total.setText(" "+product.getTotal());
        holder.jumlah_pesanan.setText(product.getJumlah_pesanan()+" Produk");

        Glide.with(mCtx)
                .load(product.getGambar())
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView merek, brand, harga, status,total,jumlah_pesanan,terverifikasi;
        ImageView gambar;
        LinearLayout lyt_item;
        Button btnBeliLagi;

        public ProductViewHolder(View itemView) {
            super(itemView);
            lyt_item = itemView.findViewById(R.id.lyt_item_pesanan);
            merek = itemView.findViewById(R.id.merek_pesanan);
            brand = itemView.findViewById(R.id.brand_pesanan);
            harga = itemView.findViewById(R.id.harga_pesanan);
            status = itemView.findViewById(R.id.status_pesanan);
            total = itemView.findViewById(R.id.total_pesanan);
            gambar = itemView.findViewById(R.id.gambar_pesanan);
            jumlah_pesanan = itemView.findViewById(R.id.jumlah_pesanan);
            btnBeliLagi = itemView.findViewById(R.id.beli_lagi_pesanan);
            terverifikasi = itemView.findViewById(R.id.terverifikasi);
        }
    }
}
