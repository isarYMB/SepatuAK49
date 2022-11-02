package com.hp.ali.ecomerceapp.models;

public class MyListData {
    private String title,merek,deskripsi;
    private int imgId;
    private int harga;
    public MyListData(String title,String merek,String deskripsi,int harga, int imgId) {
        this.title = title;
        this.merek = merek;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.imgId = imgId;
    }




    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMerek() {
        return merek;
    }
    public void setMerek(String merek) {
        this.merek = merek;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    public void setDeskripsi(String description) {
        this.deskripsi = description;
    }
    public int getHarga() {
        return harga;
    }
    public void setHarga(int harga) {
        this.harga = harga;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
    public interface OnItemClickCallback {
        void onItemClicked(MyListData data);
    }
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
}
