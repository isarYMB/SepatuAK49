package com.hp.ali.ecomerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.hp.ali.ecomerceapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SwipeAdapter extends PagerAdapter {
    private Context context;
    private String[] title_array ={"Selamat Datang", "Mencoba Sepatu", "Ukur Kaki, Lihat, dan Beli ", "Pembayaran Debit", "Gratis Ongkir dan COD", "7 Hari Garansi Retur"};
    private String[] animation_array ={"animation_1.json", "animation_2.json", "animation_3.json", "animation_4.json", "animation_5.json", "animation_6.json"};

    private String[] desc_array ={
            "Selamat datang di aplikasi Sepatu AK49",
            "Dengan pemanfaatan teknologi Augmented Reality, kamu dapat mencoba sepatu secara virtual 3 dimensi hanya melalui smartphone kamu.",
            "Kamu dapat mengukur kakimu, melihat sepatu secara 360 derajat, serta membelinya dengan sangat mudah.",
            "Kamu dapat melakukan pembayaran dengan menggunakan kartu debit dengan pembayaran 100% terjamin aman.",
            "Sepatu kamu akan dikirimkan ke alamat tujuan dan gratis ongkir untuk seluruh kota Makassar dan potongan ongkir untuk di luar kota Makassar. Kamu juga dapat melakukan pembayaran secara Cash on Delivery.",
            "Jika sepatu yang kamu terima tidak sesuai dengan gambar di aplikasi atau ada masalah pada sepatu, maka kamu dapat melakukan retur. Belanja makin aman dan nyaman."
    };

    private ArrayList<String> list = new ArrayList<>();

    public SwipeAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return animation_array.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.screen1,container,false);
        LottieAnimationView lottie1;
        TextView title1, desc1;

        lottie1 = v.findViewById(R.id.lottie1);
        title1 = v.findViewById(R.id.title1);
        desc1 = v.findViewById(R.id.desc1);

        lottie1.setAnimation(animation_array[position]);
        title1.setText(title_array[position]);
        desc1.setText(desc_array[position]);

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
