package com.hp.ali.ecomerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hp.ali.ecomerceapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class UkakiAdapter extends PagerAdapter {
    private Context contextUkaki;
    private String[] title_arrayUkaki ={"Download UKAKI", "Print UKAKI", "Menguji Hasil Print", "Mengukur Kaki"};
    private String[] animation_arrayUkaki ={String.valueOf(R.drawable.ukaki), String.valueOf(R.drawable.print), String.valueOf(R.drawable.ukur_uang), String.valueOf(R.drawable.kaki)};

    private String[] desc_arrayUkaki ={
            "UKAKI (Ukur Kaki Sepatu AK49) adalah sebuah pengukur telapak kaki dalam satuan Euro dari ukuran 26 sampai 46. Agar memudahkan kamu untuk memilih ukuran sepatu di aplikasi ini. File akan tersimpan di folder Download dengan nama file UKAKI.pdf.",
            "Print lembaran UKAKI dengan memilih Custom Scale dengan skala 100% di pengaturan Print dengan ukuran kertas A4. Jangan memilih pilihan Fit, Shrink Oversized Pages, Actual Size, dan sebagainya.",
            "Untuk menguji apakah ukuran lembaran UKAKI tidak berubah, maka perlu menyamakan ukuran uang kertas kamu dengan gambar uang yang ada di UKAKI. Jika tidak sesuai, maka print ulang lembaran UKAKI dan print sesuai dengan instruksi pada swipe ke-2.",
            "Setelah ukuran uang kertas kamu telah sesuai dengan ukuran gambar uang yang ada di UKAKI, maka ukurlah telapak kaki kamu. Contoh seperti gambar di atas, ujung telapak kakinya menyentuh garis 43, sehingga ia dapat memilih ukuran 43 untuk membeli sepatu."
    };

    private ArrayList<String> list = new ArrayList<>();

    public UkakiAdapter(Context context){
        this.contextUkaki = context;
    }

    @Override
    public int getCount() {
        return animation_arrayUkaki.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(contextUkaki).inflate(R.layout.screen_ukaki,container,false);
        ImageView lottieUkaki;
        TextView titleUkaki, descUkaki;

        lottieUkaki = v.findViewById(R.id.lottieUkaki);
        titleUkaki = v.findViewById(R.id.titleUkaki);
        descUkaki = v.findViewById(R.id.descUkaki);

        lottieUkaki.setImageResource(Integer.parseInt(animation_arrayUkaki[position]));
        titleUkaki.setText(title_arrayUkaki[position]);
        descUkaki.setText(desc_arrayUkaki[position]);

        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
