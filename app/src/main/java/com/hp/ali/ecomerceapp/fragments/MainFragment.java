package com.hp.ali.ecomerceapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.activities.CartActivity;
import com.hp.ali.ecomerceapp.activities.Other;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainFragment extends Fragment {
    ImageView img_home,img_wishlist,img_setting,img_profile;
    ImageButton menu_btn;
    Fragment fragmentClass = new HomeFragment();
    FloatingActionButton btn_cart;
    TextView tv_home, tv_keranjang, tv_lainnya, tv_profile;
    RelativeLayout searchProduk;
    public MainFragment() {
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_main, container, false);

        btn_cart =  v.findViewById(R.id.btn_cart);
        searchProduk = v.findViewById(R.id.edt_search);


        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getContext(), CartActivity.class);
                startActivity(intent);
                customType(getContext(),"left-to-right");
            }
        });



        img_home =  v.findViewById(R.id.img_home);
        img_wishlist =  v.findViewById(R.id.img_wishlist);
        img_setting =  v.findViewById(R.id.img_setting);
        img_profile =  v.findViewById(R.id.img_profile);
        tv_home =  v.findViewById(R.id.tv_home);
        tv_keranjang =  v.findViewById(R.id.tv_keranjang);
        tv_lainnya =  v.findViewById(R.id.tv_lainnya);
        tv_profile =  v.findViewById(R.id.tv_profile);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment, fragmentClass)
                .commit();
        img_home.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        tv_home.setTextColor(getResources().getColor(R.color.dark_blue));

        img_wishlist.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
        img_setting.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
        img_profile.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
        searchProduk.setVisibility(View.VISIBLE);

        img_home.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Fragment fragmentClass = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, fragmentClass)
                        .commit();

                img_home.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_home.setTextColor(getResources().getColor(R.color.dark_blue));

                img_wishlist.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_keranjang.setTextColor(getResources().getColor(R.color.grey_500));

                img_setting.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_lainnya.setTextColor(getResources().getColor(R.color.grey_500));

                img_profile.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_profile.setTextColor(getResources().getColor(R.color.grey_500));

                searchProduk.setVisibility(View.VISIBLE);
            }
        });
        img_wishlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Fragment fragmentClass = new WishFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, fragmentClass)
                        .commit();

                img_home.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_home.setTextColor(getResources().getColor(R.color.grey_500));

                img_wishlist.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_keranjang.setTextColor(getResources().getColor(R.color.dark_blue));

                img_setting.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_lainnya.setTextColor(getResources().getColor(R.color.grey_500));

                img_profile.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_profile.setTextColor(getResources().getColor(R.color.grey_500));

                searchProduk.setVisibility(View.GONE);

            }
        });
        img_setting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Fragment fragmentClass = new SettingFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, fragmentClass)
                        .commit();

                img_home.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_home.setTextColor(getResources().getColor(R.color.grey_500));

                img_wishlist.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_keranjang.setTextColor(getResources().getColor(R.color.grey_500));

                img_setting.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_lainnya.setTextColor(getResources().getColor(R.color.dark_blue));

                img_profile.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_profile.setTextColor(getResources().getColor(R.color.grey_500));

                searchProduk.setVisibility(View.GONE);

            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Fragment fragmentClass = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, fragmentClass)
                        .commit();

                img_home.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_home.setTextColor(getResources().getColor(R.color.grey_500));

                img_wishlist.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_keranjang.setTextColor(getResources().getColor(R.color.grey_500));

                img_setting.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_500), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_lainnya.setTextColor(getResources().getColor(R.color.grey_500));

                img_profile.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                tv_profile.setTextColor(getResources().getColor(R.color.dark_blue));

                searchProduk.setVisibility(View.GONE);

            }
        });

        searchProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Other.class);
                String cariFokus = null;
                i.putExtra("fokus", cariFokus);
                startActivity(i);
            }
        });

        return v;
    }
}