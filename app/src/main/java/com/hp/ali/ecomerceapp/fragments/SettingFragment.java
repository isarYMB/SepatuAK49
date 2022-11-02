package com.hp.ali.ecomerceapp.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.KebijakanPrivasi;
import com.hp.ali.ecomerceapp.activities.MainActivity;
import com.hp.ali.ecomerceapp.activities.TentangAplikasi;
import com.hp.ali.ecomerceapp.login.OTPAuthActivity;

import java.net.URLEncoder;

import static maes.tech.intentanim.CustomIntent.customType;


public class SettingFragment extends Fragment {
    //ImageView img_back;
    CardView input_saran,kebijakan_privasi;
    CardView tentang_aplikasi;

    public SettingFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);
        ((MainActivity)getActivity()).updateStatusBarColor("#F5F5F8");

        input_saran = view.findViewById(R.id.input_saran);
        kebijakan_privasi = view.findViewById(R.id.kebijakan_privasi);
        tentang_aplikasi = view.findViewById(R.id.tentang_aplikasi);

        //Mengubah warna status bar ke dark
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        /*
        ((MainActivity)getActivity()).top_toolbar.setVisibility(View.GONE);
        img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                customType(getContext(),"right-to-left");
                startActivity(intent);
            }
        });

         */

        kebijakan_privasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KebijakanPrivasi.class);
                startActivity(intent);
            }
        });

        tentang_aplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TentangAplikasi.class);
                startActivity(intent);
            }
        });

        input_saran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String pesan = "[Bantuan/Saran]";
                    PackageManager packageManager = getActivity().getApplication().getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+ "+62895361139913" +"&text="
                            + URLEncoder.encode(pesan+"\n"+"Saya Ingin Bertanya atau Memberikan Saran Mengenai Aplikasi Sepatu AK49:"+"\n"+"\n", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }else {

                    }
                } catch(Exception e) {
                    Log.e("ERROR WHATSAPP",e.toString());
                }
            }
        });

        return view;

    }
}