package com.hp.ali.ecomerceapp.activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.adapter.SwipeAdapter;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

public class SwipeActivity extends AppCompatActivity {
    ViewPager swipePager;
    PageIndicatorView swipeIndicator;
    Button finishSwipe;
    SwipeAdapter swipeAdapter;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_screen);
        swipePager = findViewById(R.id.swipePager);
        swipeIndicator = findViewById(R.id.swipeIndicator);
        finishSwipe = findViewById(R.id.finishSwipe);
        sessionManager = new SessionManager(this);
        swipeAdapter = new SwipeAdapter(this);
        swipePager.setAdapter(swipeAdapter);

        swipeIndicator.setAnimationType(AnimationType.DROP);
        swipeIndicator.setCount(6);

        //Full Screen Fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Mengembalikan warna status bar ke Light
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        finishSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SwipeActivity.this, MainActivity.class));
            }
        });

        swipePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                swipeIndicator.setSelection(position);

                if(position == 5){
                    finishSwipe.setVisibility(View.VISIBLE);
                }else {
                    finishSwipe.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        //Mengecek apakah aplikasi pertama kali dijalankan
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall","");

        if(FirstTime.equals("Yes")){
            //Jika aplikasi dijalankan pada pertama kali
            Intent intent = new Intent(SwipeActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }
        super.onStart();
    }
}