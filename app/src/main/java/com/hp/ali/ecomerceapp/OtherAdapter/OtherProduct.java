package com.hp.ali.ecomerceapp.OtherAdapter;

public class OtherProduct {
    private int id;
    private String merek;
    private String brand;
    private String deskripsi;
    private int harga;
    private int diskon;
    private String gambar;
    private String lensUUID;

    public OtherProduct(int id, String merek, String brand, String deskripsi,int harga,int diskon, String gambar,String lensUUID) {
        this.id = id;
        this.merek = merek;
        this.brand = brand;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.diskon = diskon;
        this.gambar = gambar;
        this.lensUUID = lensUUID;
    }

    public int getId() {
        return id;
    }

    public String getMerek() {
        return merek;
    }

    public String getBrand() {
        return brand;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getHarga(){
        return harga;
    }
    public int getDiskon(){
        return diskon;
    }

    public String getGambar() {
        return gambar;
    }
    public String getLensUUID() {
        return lensUUID;
    }
}
