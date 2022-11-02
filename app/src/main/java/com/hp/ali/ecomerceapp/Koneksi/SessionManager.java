package com.hp.ali.ecomerceapp.Koneksi;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE =0;
    public static final String SP_SUDAH_LOGIN = "spSudahLogin";
    public static final String SP_SUDAH_INSTALL = "spSudahInstall";
    public static final String PREF_NAME = "LOGIN";
    public static final Integer SP_ID = null;
    public static final String SP_NAME = "NAME";
    public static final String SP_ALAMAT = "ALAMAT";
    public static final String SP_NOMOR = "NOMOR";
    public static final String SP_TAG = "Belum di atur";
    public static final String SP_DISKON = "berlaku";
    public static final String SP_EMAIL = "email";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public HashMap<String,String> getuserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(SP_NAME, sharedPreferences.getString(SP_NAME,null));
        return user;
    }
    public HashMap<Integer, Integer> getuserKonsul(){
        HashMap<Integer, Integer> user = new HashMap<>();
        user.put(SP_ID, sharedPreferences.getInt(String.valueOf(SP_ID),0));
        return user;
    }
    public HashMap<String,String> getuserTag(){
        HashMap<String, String> tag = new HashMap<>();
        tag.put(SP_TAG, sharedPreferences.getString(SP_TAG,null));
        return tag;
    }

    public HashMap<String,String> getuserAlamat(){
        HashMap<String, String> talamat = new HashMap<>();
        talamat.put(SP_ALAMAT, sharedPreferences.getString(SP_ALAMAT,""));
        return talamat;
    }
    public HashMap<String,String> getMasaBerlakuDiskon(){
        HashMap<String, String> cek = new HashMap<>();
        cek.put(SP_DISKON, sharedPreferences.getString(SP_DISKON,""));
        return cek;
    }
    public HashMap<String,String> getEmail(){
        HashMap<String, String> email = new HashMap<>();
        email.put(SP_EMAIL, sharedPreferences.getString(SP_EMAIL,""));
        return email;
    }

    public HashMap<String,String> getuserNomor(){
        HashMap<String, String> tnomor = new HashMap<>();
        tnomor.put(SP_NOMOR, sharedPreferences.getString(SP_NOMOR,"0"));
        return tnomor;
    }
    public Boolean getSPSudahLogin(){
        return sharedPreferences.getBoolean(SP_SUDAH_LOGIN, false);
    }


    public void saveSPBoolean(String keySP, boolean value){
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public void saveSPString(String keySP, String value){
        editor.putString(keySP, value);
        editor.commit();
    }

    public void saveSPInt(String keySP, int value){
        editor.putInt(keySP, value);
        editor.commit();
    }
}