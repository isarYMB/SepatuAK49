package com.hp.ali.ecomerceapp.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.Toast;

import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.R;
import com.hp.ali.ecomerceapp.adapter.SwipeAdapter;
import com.hp.ali.ecomerceapp.adapter.UkakiAdapter;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class SwipeUkaki extends AppCompatActivity {
    ViewPager swipePagerUkaki;
    PageIndicatorView swipeIndicatorUkaki;
    Button finishSwipeUkaki,downloadUkaki;
    UkakiAdapter swipeAdapterUkaki;
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_ukaki);
        swipePagerUkaki = findViewById(R.id.swipePagerUkaki);
        swipeIndicatorUkaki = findViewById(R.id.swipeIndicatorUkaki);
        finishSwipeUkaki = findViewById(R.id.finishSwipeUkaki);
        downloadUkaki = findViewById(R.id.downloadUkaki);

        swipeAdapterUkaki = new UkakiAdapter(this);
        swipePagerUkaki.setAdapter(swipeAdapterUkaki);

        swipeIndicatorUkaki.setAnimationType(AnimationType.DROP);
        swipeIndicatorUkaki.setCount(4);

        //Full Screen Fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Mengembalikan warna status bar ke Light
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        finishSwipeUkaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        downloadUkaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUrlUkaki = "https://bluedevision.online/ECOMMERSE/ukaki/UKAKI.pdf";
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrlUkaki));
                String title = URLUtil.guessFileName(getUrlUkaki,null,null);
                request.setTitle(title);
                request.setDescription("UKAKI sedang didownload...");
                String cookie = CookieManager.getInstance().getCookie(getUrlUkaki);
                request.addRequestHeader("cookie",cookie);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                Toast.makeText(SwipeUkaki.this,"UKAKI.pdf tersimpan di folder Download",Toast.LENGTH_SHORT).show();
            }
        });

        swipePagerUkaki.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        requestAppPermissions();
    }
    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}